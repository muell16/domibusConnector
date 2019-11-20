package eu.domibus.connector.controller.process;


import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;

@Component(GatewayToBackendConfirmationProcessor.GW_TO_BACKEND_CONFIRMATION_PROCESSOR)
public class GatewayToBackendConfirmationProcessor implements DomibusConnectorMessageProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayToBackendConfirmationProcessor.class);

	public static final String GW_TO_BACKEND_CONFIRMATION_PROCESSOR = "GatewayToBackendConfirmationProcessor";

    private DomibusConnectorMessagePersistenceService messagePersistenceService;

    private DomibusConnectorEvidencePersistenceService evidencePersistenceService;

	private DomibusConnectorBackendDeliveryService backendDeliveryService;

	@Autowired
    public void setMessagePersistenceService(DomibusConnectorMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    @Autowired
    public void setEvidencePersistenceService(DomibusConnectorEvidencePersistenceService evidencePersistenceService) {
        this.evidencePersistenceService = evidencePersistenceService;
    }

    @Autowired
    public void setBackendDeliveryService(DomibusConnectorBackendDeliveryService backendDeliveryService) {
        this.backendDeliveryService = backendDeliveryService;
    }

    @Override
    @StoreMessageExceptionIntoDatabase
    @MDC(name = LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_PROCESSOR_PROPERTY_NAME, value = GW_TO_BACKEND_CONFIRMATION_PROCESSOR)
	public void processMessage(DomibusConnectorMessage confirmationMessage) {
		String refToMessageID = confirmationMessage.getMessageDetails().getRefToMessageId();

        DomibusConnectorMessage originalMessage = messagePersistenceService
                .findMessageByEbmsIdAndDirection(refToMessageID, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY)
                .get();

        DomibusConnectorMessageConfirmation confirmation = confirmationMessage.getMessageConfirmations().get(0);

        if (isMessageAlreadyRejected(originalMessage)) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(originalMessage)
                    .setText("Received evidence of type " + confirmation.getEvidenceType() +
                            " for an already in database marked as rejected Message with ebms ID " + refToMessageID)
                    .setSource(this.getClass())
                    .build();	
            
        }
        if (containsRejectionConfirmation(originalMessage)) {
            LOGGER.info(LoggingMarker.BUSINESS_LOG, "Confirmation message received of type [{}] - putting message into rejected state", confirmation.getEvidenceType());
            messagePersistenceService.rejectMessage(originalMessage);
        }

        originalMessage.addConfirmation(confirmation);

        evidencePersistenceService.persistEvidenceForMessageIntoDatabase(originalMessage,
                confirmation,
                new DomibusConnectorMessage.DomibusConnectorMessageId(confirmationMessage.getConnectorMessageId()));


        if (originalMessage.getMessageDetails().getBackendMessageId() != null) {
            confirmationMessage.getMessageDetails().setBackendMessageId(originalMessage.getMessageDetails().getBackendMessageId());
        }
        backendDeliveryService.deliverMessageToBackend(confirmationMessage);


        boolean confirmedOrRejected = messagePersistenceService.checkMessageConfirmedOrRejected(originalMessage);
        if (!confirmedOrRejected) {
        	if (confirmation.getEvidenceType().equals(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE)
                    || confirmation.getEvidenceType().equals(DomibusConnectorEvidenceType.DELIVERY)) {
                messagePersistenceService.confirmMessage(originalMessage);
        	}
        }

        LOGGER.info("Successfully processed evidence of type {} to originalMessage {}", confirmation.getEvidenceType(),
                originalMessage.getConnectorMessageId());

	}
	
	private boolean isMessageAlreadyRejected(DomibusConnectorMessage message) {
      if (messagePersistenceService.checkMessageRejected(message)) {
          return true;
      }
      return false;
    }

    /**
     *
     *
     * @return returns false if the message contains an evidence/confirmation which
     * requires to put the message in rejected state. These evidences are:
     * <ul>
     *     <li>RELAY_REMMD_REJECTION</li>
     *     <li>NON_DELIVERY</li>
     *     <li>NON_RETRIEVAL</li>
     * </ul>
     */
    private boolean containsRejectionConfirmation(DomibusConnectorMessage message) {
        if (message.getMessageConfirmations() != null) {
            for (DomibusConnectorMessageConfirmation confirmation : message.getMessageConfirmations()) {
                if (confirmation.getEvidenceType().equals(DomibusConnectorEvidenceType.RELAY_REMMD_REJECTION)
                        || confirmation.getEvidenceType().equals(DomibusConnectorEvidenceType.NON_DELIVERY)
                        || confirmation.getEvidenceType().equals(DomibusConnectorEvidenceType.NON_RETRIEVAL)) {
                    messagePersistenceService.rejectMessage(message);
                    return true;
                }
            }
        }
        return false;
    }

}
