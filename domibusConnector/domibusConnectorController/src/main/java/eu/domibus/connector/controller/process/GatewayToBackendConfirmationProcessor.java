package eu.domibus.connector.controller.process;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;

@Component("GatewayToBackendConfirmationProcessor")
public class GatewayToBackendConfirmationProcessor implements DomibusConnectorMessageProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(GatewayToBackendConfirmationProcessor.class);
	
	@Resource
	private DomibusConnectorPersistenceService persistenceService;
	
	@Resource
	private DomibusConnectorBackendDeliveryService backendDeliveryService;

	@Override
	public void processMessage(DomibusConnectorMessage message) {
		String refToMessageID = message.getMessageDetails().getRefToMessageId();

        DomibusConnectorMessage originalMessage = persistenceService.findMessageByEbmsId(refToMessageID);    
        DomibusConnectorMessageConfirmation confirmation = message.getMessageConfirmations().get(0);

        if (isMessageAlreadyRejected(originalMessage)) {
            persistenceService.rejectMessage(originalMessage);
            throw new DomibusConnectorMessageException(originalMessage, "Received evidence of type "
                    + confirmation.getEvidenceType().toString() + " for an already rejected Message with ebms ID "
                    + refToMessageID, this.getClass());
        }

        originalMessage.addConfirmation(confirmation);

        persistenceService.persistEvidenceForMessageIntoDatabase(originalMessage, confirmation.getEvidence(),
                confirmation.getEvidenceType());

        backendDeliveryService.deliverMessageToBackend(message);

        // TODO this needs to be done by the backend link!!!
//        try {
//            persistenceService.setEvidenceDeliveredToNationalSystem(originalMessage, confirmation.getEvidenceType());
//        } catch (PersistenceException persistenceException) {
//        	logger.error("Persistence Exception occured", persistenceException);
//        }

        boolean confirmedOrRejected = persistenceService.checkMessageConfirmedOrRejected(originalMessage);
        if (!confirmedOrRejected) {
        	if (confirmation.getEvidenceType().equals(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE)
                    || confirmation.getEvidenceType().equals(DomibusConnectorEvidenceType.DELIVERY)) {
        		persistenceService.confirmMessage(originalMessage);
        	}
        }

        logger.info("Successfully processed evidence of type {} to message {}", confirmation.getEvidenceType(),
                originalMessage.getConnectorMessageId());

	}
	
	private boolean isMessageAlreadyRejected(DomibusConnectorMessage message) {
      if (persistenceService.checkMessageRejected(message)) {
          return true;
      }
      if (message.getMessageConfirmations() != null) {
          for (DomibusConnectorMessageConfirmation confirmation : message.getMessageConfirmations()) {
              if (confirmation.getEvidenceType().equals(DomibusConnectorEvidenceType.RELAY_REMMD_REJECTION)
                      || confirmation.getEvidenceType().equals(DomibusConnectorEvidenceType.NON_DELIVERY)
                      || confirmation.getEvidenceType().equals(DomibusConnectorEvidenceType.NON_RETRIEVAL)) {
              	persistenceService.rejectMessage(message);
                  return true;
              }
          }
      }
      return false;
  }

}
