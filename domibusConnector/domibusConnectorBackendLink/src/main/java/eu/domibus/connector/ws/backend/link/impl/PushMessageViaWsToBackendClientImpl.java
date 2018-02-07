
package eu.domibus.connector.ws.backend.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformer;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistAllBigDataOfMessageService;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWebService;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Properties;
import javax.xml.ws.soap.MTOMFeature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
public class PushMessageViaWsToBackendClientImpl implements PushMessageToBackendClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(PushMessageViaWsToBackendClientImpl.class);
    
    private BackendClientInfoPersistenceService backendClientPersistenceService;
    
    private DomibusConnectorMessagePersistenceService messagePersistenceService;
    
    private DomibusConnectorPersistAllBigDataOfMessageService bigDataMessageService;
    
    //SETTER
    @Autowired
    public void setBackendClientPersistenceService(BackendClientInfoPersistenceService backendClientPersistenceService) {
        this.backendClientPersistenceService = backendClientPersistenceService;
    }

    @Autowired
    public void setMessagePersistenceService(DomibusConnectorMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    @Autowired
    public void setBigDataMessageService(DomibusConnectorPersistAllBigDataOfMessageService bigDataMessageService) {
        this.bigDataMessageService = bigDataMessageService;
    }
    
    @Value("${connector.backend.ws.security.encryption.properties:'classpath:/eu/domibus/connector/ws/backend/link/ws/encrypt.properties'}")
    private Resource encryptPropertiesFileLocation;
    
    @Override
    public void push(String connectorMessageId, String backendClientName) {
        
        //TODO: load message from persistence
        DomibusConnectorMessage message = messagePersistenceService.findMessageByConnectorMessageId(connectorMessageId);
        
        //TODO: load get backend address, cert, 
        DomibusConnectorBackendClientInfo backendClientInfoByName = backendClientPersistenceService.getBackendClientInfoByName(backendClientName);
        
        //initialize BigData
        message = bigDataMessageService.loadAllBigFilesFromMessage(message);
        
        //transform message to transition
        DomibusConnectorMessageType transitionMessage = DomibusConnectorDomainMessageTransformer.transformDomainToTransition(message);
        
        //send message
        

    }
    
    
    private DomibusConnectorBackendDeliveryWebService createWsClient(DomibusConnectorBackendClientInfo backendClientInfoByName) {        
        LOGGER.debug("#createWsClient: creating WS endpoint for backendClient [{}]", backendClientInfoByName);
        String pushAddress = backendClientInfoByName.getBackendPushAddress();
        
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean
                = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(DomibusConnectorBackendWebService.class);

        //MTOMFeature mtom = new MTOMFeature();
                                
        jaxWsProxyFactoryBean.setAddress(pushAddress);
        jaxWsProxyFactoryBean.setWsdlURL(pushAddress + "?wsdl");
        
        Properties encryptionProperties = loadEncryptionProperties();
        encryptionProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.alias", backendClientInfoByName.getBackendKeyAlias());

                                
        HashMap<String, Object> props = new HashMap<>();
        props.put("security.encryption.properties", props);
            
        jaxWsProxyFactoryBean.setProperties(props);
        
        
        //jaxWsProxyFactoryBean.set
        DomibusConnectorBackendDeliveryWebService webServiceClientEndpoint = (DomibusConnectorBackendDeliveryWebService) jaxWsProxyFactoryBean.create();        
        return webServiceClientEndpoint;
    }
    
    private Properties loadEncryptionProperties() {
        try {
            Properties props = new Properties();
            InputStream is = encryptPropertiesFileLocation.getInputStream();
            props.load(is);
            return props;
        } catch (IOException ioe) {
            LOGGER.debug("IOError occured while loading default encryption properties from [{}]", encryptPropertiesFileLocation);
            throw new RuntimeException(ioe);
        }
    }

}
