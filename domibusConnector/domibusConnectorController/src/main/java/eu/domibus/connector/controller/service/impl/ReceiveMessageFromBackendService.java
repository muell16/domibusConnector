
package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorBackendSubmissionService;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistAllBigDataOfMessageService;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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
        LOGGER.error("#submitToController: not yet implemented!");

        LOGGER.debug("#submitToController: message [{}]", message);

        message.setConnectorMessageId(messageIdGenerator.generateDomibusConnectorMessageId());
        LOGGER.debug("#submitToController: start to process message with message id [{}]", message.getConnectorMessageId());
        MDC.put(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, message.getConnectorMessageId());
        message = messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.NAT_TO_GW);
        LOGGER.debug("#submitToController: message persisted");
        bigDataPersistence.persistAllBigFilesFromMessage(message);
        LOGGER.debug("#submitToController: message data persisted");


        LOGGER.debug("#submitToController: message.getMessageDetails().getService() [{}]", message.getMessageDetails().getService());

        String testPingConnectorMessage = "TEST-ping-connector";
        if (testPingConnectorMessage.equals(message.getMessageDetails().getService().getService())) {
            LOGGER.debug("#submitToController: message is " + testPingConnectorMessage + " so just sending message back to backend!");
            toBackendDeliveryService.deliverMessageToBackend(message);
            
            //also generate evidences?
        } else {
            LOGGER.debug("#submitToController: 'normal' message.....");
        }
        
        //TODO: put message on internal queue for further processing
    }

}
