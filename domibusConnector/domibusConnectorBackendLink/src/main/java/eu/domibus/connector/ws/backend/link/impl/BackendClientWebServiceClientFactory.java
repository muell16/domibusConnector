
package eu.domibus.connector.ws.backend.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.ws.helper.WsPolicyLoader;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWebService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.xml.stream.XMLStreamException;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.staxutils.StaxUtils;
import org.apache.cxf.ws.policy.PolicyBuilderImpl;
import org.apache.cxf.ws.policy.WSPolicyFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

/**
 * Creates a web service client for pushing messages to backend client
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
public class BackendClientWebServiceClientFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(BackendClientWebServiceClientFactory.class);
    
    @Autowired
    WsPolicyLoader policyUtil;
    
    @Value("${connector.backend.ws.security.encryption.properties:'/eu/domibus/connector/ws/backend/link/ws/decrypt.properties'}")
    ClassPathResource encryptPropertiesFileLocation;
    
    
    public DomibusConnectorBackendDeliveryWebService createWsClient(DomibusConnectorBackendClientInfo backendClientInfoByName) {
        LOGGER.debug("#createWsClient: creating WS endpoint for backendClient [{}]", backendClientInfoByName);
        String pushAddress = backendClientInfoByName.getBackendPushAddress();
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(DomibusConnectorBackendDeliveryWebService.class);
       
        jaxWsProxyFactoryBean.setFeatures(Arrays.asList(new Feature[]{policyUtil.loadPolicyFeature()}));
        jaxWsProxyFactoryBean.setAddress(pushAddress);
        jaxWsProxyFactoryBean.setWsdlURL(pushAddress + "?wsdl");
        Properties encryptionProperties = loadEncryptionProperties();
        Properties signatureProperties = loadEncryptionProperties();
        HashMap<String, Object> props = new HashMap<>();
        props.put("security.encryption.properties", encryptionProperties);
        props.put("security.signature.properties", signatureProperties);
        props.put("security.encryption.username", backendClientInfoByName.getBackendKeyAlias());
        props.put("security.signature.username", "connector");
        jaxWsProxyFactoryBean.setProperties(props);
        //jaxWsProxyFactoryBean.set
        DomibusConnectorBackendDeliveryWebService webServiceClientEndpoint = (DomibusConnectorBackendDeliveryWebService) jaxWsProxyFactoryBean.create();
        return webServiceClientEndpoint;
    }

    private Properties loadEncryptionProperties() {
        try {
            Properties props = new Properties();
            //InputStream is = encryptPropertiesFileLocation.getInputStream();
            InputStream is = getClass().getResourceAsStream("/eu/domibus/connector/ws/backend/link/ws/decrypt.properties");
            if (is == null) {
                throw new RuntimeException("is is null!");
            }
            props.load(is);
            return props;
        } catch (IOException ioe) {
            LOGGER.debug("IOError occured while loading default encryption properties from [{}]", encryptPropertiesFileLocation);
            LOGGER.error("IOException: ", ioe);
            throw new RuntimeException(ioe);
        }
    }

}
