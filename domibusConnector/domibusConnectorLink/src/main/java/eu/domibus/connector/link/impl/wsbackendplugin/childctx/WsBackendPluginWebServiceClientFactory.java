
package eu.domibus.connector.link.impl.wsbackendplugin.childctx;

import eu.domibus.connector.link.common.MerlinPropertiesFactory;
import eu.domibus.connector.link.common.WsPolicyLoader;
import eu.domibus.connector.link.impl.wsbackendplugin.WsBackendPluginActiveLinkPartner;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWSService;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWebService;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.policy.WSPolicyFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import static eu.domibus.connector.tools.logging.LoggingMarker.Log4jMarker.CONFIG;

/**
 * Creates a web service client for pushing messages to backend client
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class WsBackendPluginWebServiceClientFactory {

    private static final Logger LOGGER = LogManager.getLogger(WsBackendPluginWebServiceClientFactory.class);



    @Autowired
    private WsBackendPluginConfigurationProperties config;

//    @Autowired
//    @Qualifier(POLICY_LOADER_NAME)
//    private WsPolicyLoader policyUtil;

    @Autowired
    MerlinPropertiesFactory merlinPropertiesFactory;


//    public DomibusConnectorGatewaySubmissionWebService createGateway(DCWsActiveLinkPartner linkPartner) {
//        LOGGER.debug("#createWsClient: creating WS endpoint for backendClient [{}]", linkPartner);
//        String pushAddress = linkPartner.getPushAddress();
//        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
//        jaxWsProxyFactoryBean.setServiceClass(DomibusConnectorGatewaySubmissionWebService.class);
//
//        jaxWsProxyFactoryBean.setFeatures(Arrays.asList(new Feature[]{policyUtil.loadPolicyFeature()}));
//        jaxWsProxyFactoryBean.setAddress(pushAddress);
//        jaxWsProxyFactoryBean.setWsdlLocation(DomibusConnectorGatewaySubmissionWSService.WSDL_LOCATION.toString());
////        jaxWsProxyFactoryBean.setWsdlURL(pushAddress + "?wsdl"); //maybe load own wsdl instead of remote one?
//        jaxWsProxyFactoryBean.setServiceName(DomibusConnectorGatewaySubmissionWSService.SERVICE);
//        jaxWsProxyFactoryBean.setEndpointName(DomibusConnectorGatewaySubmissionWSService.DomibusConnectorGatewaySubmissionWebService);
//
//        HashMap<String, Object> props = new HashMap<>();
//        props.put("security.encryption.properties", config.getWssProperties());
//        props.put("security.signature.properties", config.getWssProperties());
//        props.put("security.encryption.username", linkPartner.getEncryptionAlias());
//        props.put("security.signature.username", config.getSoap().getPrivateKey().getAlias());
//        LOGGER.debug("#createWsClient: Configuring WsClient with following properties: [{}]", props);
//        jaxWsProxyFactoryBean.setProperties(props);
//        //jaxWsProxyFactoryBean.set
//        return (DomibusConnectorGatewaySubmissionWebService) jaxWsProxyFactoryBean.create();
//    }

    public DomibusConnectorBackendDeliveryWebService createBackendWsClient(WsBackendPluginActiveLinkPartner linkPartner) {
        LOGGER.debug(CONFIG, "#createWsClient: creating WS endpoint for backendClient [{}]", linkPartner);
        WsBackendPluginLinkPartnerConfigurationProperties linkPartnerConfig = linkPartner.getConfig();
        String pushAddress = linkPartnerConfig.getPushAddress();
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(DomibusConnectorBackendDeliveryWebService.class);

        if (config.isCxfLoggingEnabled()) {
            jaxWsProxyFactoryBean.getFeatures().add(new LoggingFeature());
        }

        WSPolicyFeature wsPolicyFeature = new WsPolicyLoader(config.getWsPolicy()).loadPolicyFeature();
        jaxWsProxyFactoryBean.getFeatures().add(wsPolicyFeature);
        jaxWsProxyFactoryBean.setAddress(pushAddress);
        jaxWsProxyFactoryBean.setWsdlURL(DomibusConnectorBackendDeliveryWSService.WSDL_LOCATION.toString());
//        jaxWsProxyFactoryBean.setWsdlURL(pushAddress + "?wsdl"); //maybe load own wsdl instead of remote one?

        Properties properties = merlinPropertiesFactory.mapCertAndStoreConfigPropertiesToMerlinProperties(this.config.getSoap(), ".");

        HashMap<String, Object> jaxWsFactoryBeanProperties = new HashMap<>();
        jaxWsFactoryBeanProperties.put("security.encryption.username", linkPartnerConfig.getEncryptionAlias());
        jaxWsFactoryBeanProperties.put("mtom-enabled", true);
        jaxWsFactoryBeanProperties.put("security.encryption.properties", properties);
        jaxWsFactoryBeanProperties.put("security.signature.properties", properties);

        LOGGER.debug(CONFIG, "#createWsClient: Configuring WsClient with following properties: [{}]", jaxWsFactoryBeanProperties);

        jaxWsProxyFactoryBean.setProperties(jaxWsFactoryBeanProperties);

        return (DomibusConnectorBackendDeliveryWebService) jaxWsProxyFactoryBean.create();
    }


}
