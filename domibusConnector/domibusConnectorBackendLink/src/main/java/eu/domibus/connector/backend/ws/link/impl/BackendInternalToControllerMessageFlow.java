package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.service.DomibusConnectorBackendInternalDeliverToController;
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
 * this service handles the message transport from controller to the backend
 *
 */
@Service
public class BackendInternalToControllerMessageFlow implements DomibusConnectorBackendInternalDeliverToController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendInternalToControllerMessageFlow.class);

    private DomibusConnectorMessagePersistenceService messagePersistenceService;

    private DomibusConnectorPersistAllBigDataOfMessageService bigDataPersistence;

    private DomibusConnectorMessageIdGenerator messageIdGenerator;

    private DomibusConnectorBackendDeliveryService toBackendDeliveryService;

    private DomibusConnectorBackendSubmissionService backendToControllerSubmissionService;

    //setter
    @Autowired
    public void setMessagePersistenceService(DomibusConnectorMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    @Autowired
    public void setBigDataPersistence(DomibusConnectorPersistAllBigDataOfMessageService bigDataPersistence) {
        this.bigDataPersistence = bigDataPersistence;
    }

    @Autowired
    public void setMessageIdGenerator(DomibusConnectorMessageIdGenerator messageIdGenerator) {
        this.messageIdGenerator = messageIdGenerator;
    }

    @Autowired
    public void setToBackendDeliveryService(DomibusConnectorBackendDeliveryService backendDeliveryService) {
        this.toBackendDeliveryService = backendDeliveryService;
    }

    @Autowired
    public void setBackendToControllerSubmissionService(DomibusConnectorBackendSubmissionService toControllerSubmissionService) {
        this.backendToControllerSubmissionService = toControllerSubmissionService;
    }


    @Override
    public void submitToController(DomibusConnectorBackendMessage backendMessage) {


        LOGGER.debug("#submitToController: message [{}]", backendMessage);
        DomibusConnectorMessage message = backendMessage.getDomibusConnectorMessage();
        message.getMessageDetails().setConnectorBackendClientName(backendMessage.getBackendClientInfo().getBackendName());

        message.setConnectorMessageId(messageIdGenerator.generateDomibusConnectorMessageId());
        LOGGER.debug("#submitToController: start to process message with message id [{}]", message.getConnectorMessageId());
        MDC.put(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, message.getConnectorMessageId());
        message = messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.NAT_TO_GW);
        LOGGER.debug("#submitToController: message persisted");
        message = bigDataPersistence.persistAllBigFilesFromMessage(message);
        LOGGER.debug("#submitToController: message [{}] data persisted", message);
        message = messagePersistenceService.mergeMessageWithDatabase(message);


        LOGGER.debug("#submitToController: message.getMessageDetails().getService() [{}]", message.getMessageDetails().getService());

        String testPingConnectorMessage = "TEST-ping-connector"; //TODO: make configureable!
        if (testPingConnectorMessage.equals(message.getMessageDetails().getService().getService())) {
            LOGGER.debug("#submitToController: message is " + testPingConnectorMessage + " so just sending message back to backend!");
            toBackendDeliveryService.deliverMessageToBackend(message);

        } else {
            LOGGER.debug("#submitToController: 'normal' message.....");
            backendToControllerSubmissionService.submitToController(message);
        }
    }

    @Override
    public DomibusConnectorMessage processMessageBeforeDeliverToBackend(DomibusConnectorMessage message) {
        return bigDataPersistence.loadAllBigFilesFromMessage(message);
    }

    @Override
    public DomibusConnectorMessage processMessageAfterDeliveredToBackend(DomibusConnectorMessage message) {
        messagePersistenceService.setMessageDeliveredToNationalSystem(message);
        return message;
    }

}
