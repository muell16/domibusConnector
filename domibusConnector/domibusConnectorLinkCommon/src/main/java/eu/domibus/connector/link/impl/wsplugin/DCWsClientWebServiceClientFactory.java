
package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.link.common.WsPolicyLoader;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWebService;
import eu.domibus.connector.ws.gateway.submission.webservice.DomibusConnectorGatewaySubmissionWebService;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;

import static eu.domibus.connector.link.impl.wsplugin.DCWsPluginConfiguration.POLICY_LOADER_NAME;

/**
 * Creates a web service client for pushing messages to backend client
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DCWsClientWebServiceClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DCWsClientWebServiceClientFactory.class);
    

    @Autowired
    private DCWsPluginConfigurationProperties config;

    @Autowired
    @Qualifier(POLICY_LOADER_NAME)
    private WsPolicyLoader policyUtil;


    public void setPolicyUtil(WsPolicyLoader policyUtil) {
        this.policyUtil = policyUtil;
    }

    public void setConfig(DCWsPluginConfigurationProperties config) {
        this.config = config;
    }

    public DomibusConnectorGatewaySubmissionWebService createGateway(DCWsActiveLinkPartner linkPartner) {
        LOGGER.debug("#createWsClient: creating WS endpoint for backendClient [{}]", linkPartner);
        String pushAddress = linkPartner.getPushAddress();
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(DomibusConnectorBackendDeliveryWebService.class);

        jaxWsProxyFactoryBean.setFeatures(Arrays.asList(new Feature[]{policyUtil.loadPolicyFeature()}));
        jaxWsProxyFactoryBean.setAddress(pushAddress);
        jaxWsProxyFactoryBean.setWsdlURL(pushAddress + "?wsdl"); //maybe load own wsdl instead of remote one?

        HashMap<String, Object> props = new HashMap<>();
        props.put("security.encryption.properties", config.getWssProperties());
        props.put("security.signature.properties", config.getWssProperties());
        props.put("security.encryption.username", linkPartner.getEncryptionAlias());
        props.put("security.signature.username", config.getSoap().getPrivateKey().getAlias());
        LOGGER.debug("#createWsClient: Configuring WsClient with following properties: [{}]", props);
        jaxWsProxyFactoryBean.setProperties(props);
        //jaxWsProxyFactoryBean.set
        return (DomibusConnectorGatewaySubmissionWebService) jaxWsProxyFactoryBean.create();
    }

    public DomibusConnectorBackendDeliveryWebService createBackendWsClient(DCWsActiveLinkPartner linkPartner) {
        LOGGER.debug("#createWsClient: creating WS endpoint for backendClient [{}]", linkPartner);
        String pushAddress = linkPartner.getPushAddress();
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(DomibusConnectorBackendDeliveryWebService.class);

        jaxWsProxyFactoryBean.setFeatures(Arrays.asList(new Feature[]{policyUtil.loadPolicyFeature()}));
        jaxWsProxyFactoryBean.setAddress(pushAddress);
        jaxWsProxyFactoryBean.setWsdlURL(pushAddress + "?wsdl"); //maybe load own wsdl instead of remote one?

        HashMap<String, Object> props = new HashMap<>();
        props.put("security.encryption.properties", config.getWssProperties());
        props.put("security.signature.properties", config.getWssProperties());
        props.put("security.encryption.username", linkPartner.getEncryptionAlias());
        props.put("security.signature.username", config.getSoap().getPrivateKey().getAlias());
        LOGGER.debug("#createWsClient: Configuring WsClient with following properties: [{}]", props);
        jaxWsProxyFactoryBean.setProperties(props);
        //jaxWsProxyFactoryBean.set
        return (DomibusConnectorBackendDeliveryWebService) jaxWsProxyFactoryBean.create();
    }


}
