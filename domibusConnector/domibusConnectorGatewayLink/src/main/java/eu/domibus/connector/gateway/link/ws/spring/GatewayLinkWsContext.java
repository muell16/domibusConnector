package eu.domibus.connector.gateway.link.ws.spring;

import eu.domibus.connector.gateway.link.ws.impl.DomibusConnectorDeliveryWSImpl;
import eu.domibus.connector.lib.spring.configuration.CxfTrustKeyStoreConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import eu.domibus.connector.link.common.DefaultWsCallbackHandler;
import eu.domibus.connector.link.common.WsPolicyLoader;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWSService;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWebService;
import eu.domibus.connector.ws.gateway.submission.webservice.DomibusConnectorGatewaySubmissionWSService;
import eu.domibus.connector.ws.gateway.submission.webservice.DomibusConnectorGatewaySubmissionWebService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.policy.WSPolicyFeature;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.xml.ws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.Properties;

import static eu.domibus.connector.gateway.link.ws.spring.GatewayLinkWsContext.GW_LINK_WS_PROFILE;

@Configuration
@Profile(GW_LINK_WS_PROFILE)
public class GatewayLinkWsContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayLinkWsContext.class);

    public static final String GW_LINK_WS_PROFILE = "gwlink-ws";

    @Autowired
    private Bus cxfBus;

    @Autowired
    GatewayLinkWsServiceProperties gatewayLinkWsServiceProperties;

    @Autowired
    private DomibusConnectorDeliveryWSImpl domibusConnectorDeliveryService;


    @Bean
    public WsPolicyLoader gwWsLinkPolicyLoader() {
        WsPolicyLoader wsPolicyLoader = new WsPolicyLoader(gatewayLinkWsServiceProperties.getWsPolicy());
        return wsPolicyLoader;
    }


    @Bean
    public DomibusConnectorGatewaySubmissionWebService gwSubmissionClient() {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(DomibusConnectorGatewaySubmissionWebService.class);
        jaxWsProxyFactoryBean.setBus(cxfBus);
        jaxWsProxyFactoryBean.setAddress(gatewayLinkWsServiceProperties.getSubmissionEndpointAddress());
        jaxWsProxyFactoryBean.setServiceName(DomibusConnectorGatewaySubmissionWSService.SERVICE);
        jaxWsProxyFactoryBean.setEndpointName(DomibusConnectorGatewaySubmissionWSService.DomibusConnectorGatewaySubmissionWebService);
        jaxWsProxyFactoryBean.setWsdlURL(DomibusConnectorGatewaySubmissionWSService.WSDL_LOCATION.toString());
        jaxWsProxyFactoryBean.setBindingId(SOAPBinding.SOAP12HTTP_MTOM_BINDING);
//        jaxWsProxyFactoryBean.getOutInterceptors().add(new WSS4JOutInterceptor());

        jaxWsProxyFactoryBean.getFeatures().add(gwWsLinkPolicyLoader().loadPolicyFeature());

        if (jaxWsProxyFactoryBean.getProperties() == null) {
            jaxWsProxyFactoryBean.setProperties(new HashMap<>());
        }
        jaxWsProxyFactoryBean.getProperties().put("mtom-enabled", true);
        jaxWsProxyFactoryBean.getProperties().put("security.encryption.properties", gwWsLinkEncryptProperties());
        jaxWsProxyFactoryBean.getProperties().put("security.encryption.username", gatewayLinkWsServiceProperties.getEncryptAlias());
        jaxWsProxyFactoryBean.getProperties().put("security.signature.properties", gwWsLinkEncryptProperties());
        jaxWsProxyFactoryBean.getProperties().put("security.callback-handler", new DefaultWsCallbackHandler());

        return jaxWsProxyFactoryBean.create(DomibusConnectorGatewaySubmissionWebService.class);
    }


    @Bean
    public EndpointImpl domibusConnectorDeliveryServiceEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(cxfBus, domibusConnectorDeliveryService);
        endpoint.setAddress(gatewayLinkWsServiceProperties.getPublishAddress());
        endpoint.setWsdlLocation(DomibusConnectorGatewayDeliveryWSService.WSDL_LOCATION.toString());
        endpoint.setServiceName(DomibusConnectorGatewayDeliveryWSService.SERVICE);
        endpoint.setEndpointName(DomibusConnectorGatewayDeliveryWSService.DomibusConnectorGatewayDeliveryWebService);

        WSPolicyFeature wsPolicyFeature = gwWsLinkPolicyLoader().loadPolicyFeature();
        endpoint.getFeatures().add(wsPolicyFeature);
//        endpoint.getFeatures().add(new MTOMFeature());

        endpoint.getProperties().put("mtom-enabled", true);
        endpoint.getProperties().put("security.encryption.properties", gwWsLinkEncryptProperties());
        endpoint.getProperties().put("security.signature.properties", gwWsLinkEncryptProperties());
        endpoint.getProperties().put("security.encryption.username", "useReqSigCert");

        endpoint.publish();
        LOGGER.debug("Published WebService {} under {}", DomibusConnectorGatewayDeliveryWebService.class, endpoint.getPublishedEndpointUrl());

        return endpoint;
    }

    @Bean
    public Properties gwWsLinkEncryptProperties() {
        Properties props = new Properties();

        CxfTrustKeyStoreConfigurationProperties cxf = gatewayLinkWsServiceProperties; //.getCxf();
        StoreConfigurationProperties cxfKeyStore = gatewayLinkWsServiceProperties.getKeyStore();

        props.put("org.apache.wss4j.crypto.provider", "org.apache.wss4j.common.crypto.Merlin");
        props.put("org.apache.wss4j.crypto.merlin.keystore.type", cxf.getType());
        props.put("org.apache.wss4j.crypto.merlin.keystore.file", cxfKeyStore.getPathUrlAsString());
        props.put("org.apache.wss4j.crypto.merlin.keystore.password", cxfKeyStore.getPassword());
        props.put("org.apache.wss4j.crypto.merlin.keystore.alias", cxf.getPrivateKey().getAlias());
        props.put("org.apache.wss4j.crypto.merlin.keystore.private.password", cxf.getPrivateKey().getPassword());

        props.put("org.apache.wss4j.crypto.merlin.truststore.type", cxf.getType());
        props.put("org.apache.wss4j.crypto.merlin.truststore.file", cxf.getTrustStore().getPathUrlAsString());
        props.put("org.apache.wss4j.crypto.merlin.truststore.password", cxf.getTrustStore().getPassword());

        return props;
    }

}
