
package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorBackendSubmissionService;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistAllBigDataOfMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * 
 */
@Service
public class ReceiveMessageFromBackendService implements DomibusConnectorBackendSubmissionService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReceiveMessageFromBackendService.class);
    
    @Autowired
    DomibusConnectorMessageIdGenerator messageIdGenerator;
    
    @Autowired
    DomibusConnectorMessagePersistenceService messagePersistenceService;
        
    @Autowired
    DomibusConnectorPersistAllBigDataOfMessageService bigDataPersistence;
            
    @Autowired
    DomibusConnectorBackendDeliveryService toBackendDeliveryService;
            
    @Override
    public void submitToController(DomibusConnectorMessage message) {
        LOGGER.error("not yet implemented!");
        
        message.setConnectorMessageId(messageIdGenerator.generateDomibusConnectorMessageId());        
        message = messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.NAT_TO_GW);        
        bigDataPersistence.persistAllBigFilesFromMessage(message);
        
        
        String testPingConnectorMessage = "TEST-ping-connector";
        if ("TEST-ping-connector".equals(message.getMessageDetails().getService())) {
            LOGGER.debug("message is " + testPingConnectorMessage + " so just sending message back to backend!");
            toBackendDeliveryService.deliverMessageToBackend(message);
            
            //also generate evidences?
            
        }
        
        //TODO: put message on internal queue for further processing
    }

}
