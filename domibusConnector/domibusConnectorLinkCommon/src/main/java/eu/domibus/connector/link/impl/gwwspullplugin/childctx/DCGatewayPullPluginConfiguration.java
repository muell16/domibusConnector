package eu.domibus.connector.link.impl.gwwspullplugin.childctx;

import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.lib.spring.configuration.CxfTrustKeyStoreConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import eu.domibus.connector.link.common.DefaultWsCallbackHandler;
import eu.domibus.connector.link.common.WsPolicyLoader;
import eu.domibus.connector.ws.gateway.webservice.DomibusConnectorGatewayWSService;
import eu.domibus.connector.ws.gateway.webservice.DomibusConnectorGatewayWebService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Configuration for the spring childContext for
 * the pullGatewayPlugin
 */
@Configuration
@Profile(DCGatewayPullPluginConfiguration.DC_GATEWAY_PULL_PLUGIN_PROFILE)
@EnableConfigurationProperties(DCGatewayPullPluginConfigurationProperties.class)
@ComponentScan(basePackageClasses = DCGatewayPullPluginConfiguration.class)
public class DCGatewayPullPluginConfiguration {

    public static final String DC_GATEWAY_PULL_PLUGIN_PROFILE = "link.gwwspullplugin";

    @Autowired
    DCGatewayPullPluginConfigurationProperties configurationProperties;

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    DCGatewayWebServiceClient dcGatewayWebServiceClient() {
        return new DCGatewayWebServiceClient();
    }

    @Bean
    DomibusConnectorGatewayWebService testGwWebServiceProxy() {
//        JaxWsClientProxy jaxWsClientProxy = new JaxWsClientProxy();
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(DomibusConnectorGatewayWebService.class);

        WsPolicyLoader wsPolicyLoader = new WsPolicyLoader(configurationProperties.getWsPolicy());
        jaxWsProxyFactoryBean.getFeatures().add(wsPolicyLoader.loadPolicyFeature());

        jaxWsProxyFactoryBean.setAddress(configurationProperties.getGwAddress());
        jaxWsProxyFactoryBean.setWsdlURL(DomibusConnectorGatewayWSService.WSDL_LOCATION.toString());
        jaxWsProxyFactoryBean.setServiceName(DomibusConnectorGatewayWSService.SERVICE);
        jaxWsProxyFactoryBean.setEndpointName(DomibusConnectorGatewayWSService.DomibusConnectorGatewayWebService);

        Map<String, Object> props = jaxWsProxyFactoryBean.getProperties();
        if (props == null) {
            props = new HashMap<>();
        }

        props.put("mtom-enabled", true);
        props.put("security.encryption.properties", gwWsLinkEncryptProperties());
        props.put("security.encryption.username", configurationProperties.getSoap().getEncryptAlias());
        props.put("security.signature.properties", gwWsLinkEncryptProperties());
        props.put("security.callback-handler", new DefaultWsCallbackHandler());

        jaxWsProxyFactoryBean.setProperties(props);


        return (DomibusConnectorGatewayWebService) jaxWsProxyFactoryBean.create();

    }


    public Properties gwWsLinkEncryptProperties() {
        Properties props = new Properties();

        CxfTrustKeyStoreConfigurationProperties cxf = configurationProperties.getSoap(); //.getCxf();
        StoreConfigurationProperties cxfKeyStore = cxf.getKeyStore();

        props.put("org.apache.wss4j.crypto.provider", "org.apache.wss4j.common.crypto.Merlin");
        props.put("org.apache.wss4j.crypto.merlin.keystore.type", cxf.getKeyStore().getType());
        props.put("org.apache.wss4j.crypto.merlin.keystore.file", cxfKeyStore.getPathUrlAsString());
        props.put("org.apache.wss4j.crypto.merlin.keystore.password", cxfKeyStore.getPassword());
        props.put("org.apache.wss4j.crypto.merlin.keystore.alias", cxf.getPrivateKey().getAlias());
        props.put("org.apache.wss4j.crypto.merlin.keystore.private.password", cxf.getPrivateKey().getPassword());

        props.put("org.apache.wss4j.crypto.merlin.truststore.type", cxf.getTrustStore().getType());
        props.put("org.apache.wss4j.crypto.merlin.truststore.file", cxf.getTrustStore().getPathUrlAsString());
        props.put("org.apache.wss4j.crypto.merlin.truststore.password", cxf.getTrustStore().getPassword());

        return props;
    }



}
