
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.ws.helper.WsPolicyLoader;
import eu.domibus.connector.backend.ws.link.spring.WSBackendLinkConfigurationProperties;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWebService;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Creates a web service client for pushing messages to backend client
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
public class BackendClientWebServiceClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendClientWebServiceClientFactory.class);
    
    @Autowired
    private WsPolicyLoader policyUtil;

    @Autowired
    private WSBackendLinkConfigurationProperties backendLinkConfigurationProperties;

    //setter
    public void setPolicyUtil(WsPolicyLoader policyUtil) {
        this.policyUtil = policyUtil;
    }

    public void setBackendLinkConfigurationProperties(WSBackendLinkConfigurationProperties backendLinkConfigurationProperties) {
        this.backendLinkConfigurationProperties = backendLinkConfigurationProperties;
    }

    public DomibusConnectorBackendDeliveryWebService createWsClient(DomibusConnectorBackendClientInfo backendClientInfoByName) {
        LOGGER.debug("#createWsClient: creating WS endpoint for backendClient [{}]", backendClientInfoByName);
        String pushAddress = backendClientInfoByName.getBackendPushAddress();
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(DomibusConnectorBackendDeliveryWebService.class);
       
        jaxWsProxyFactoryBean.setFeatures(Arrays.asList(new Feature[]{policyUtil.loadPolicyFeature()}));
        jaxWsProxyFactoryBean.setAddress(pushAddress);
        jaxWsProxyFactoryBean.setWsdlURL(pushAddress + "?wsdl"); //maybe load own wsdl instead of remote one?

        HashMap<String, Object> props = new HashMap<>();
        props.put("security.encryption.properties", backendLinkConfigurationProperties.getWssProperties());
        props.put("security.signature.properties", backendLinkConfigurationProperties.getWssProperties());
        props.put("security.encryption.username", backendClientInfoByName.getBackendKeyAlias());
        props.put("security.signature.username", backendLinkConfigurationProperties.getKey().getKey().getAlias());
        LOGGER.debug("#createWsClient: Configuring WsClient with following properties: [{}]", props);
        jaxWsProxyFactoryBean.setProperties(props);
        //jaxWsProxyFactoryBean.set
        return (DomibusConnectorBackendDeliveryWebService) jaxWsProxyFactoryBean.create();
    }


}
