package eu.domibus.connector.controller.process;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;

@Component("BackendToGatewayConfirmationProcessor")
public class BackendToGatewayConfirmationProcessor implements DomibusConnectorMessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BackendToGatewayConfirmationProcessor.class);
	
	@Resource
	private DomibusConnectorPersistenceService persistenceService;
	
	@Resource
	private DomibusConnectorEvidencesToolkit evidencesToolkit;
	
	@Resource
	private DomibusConnectorGatewaySubmissionService gwSubmissionService;
	
	@Override
	public void processMessage(DomibusConnectorMessage message) {
		String messageID = message.getMessageDetails().getRefToMessageId();

        DomibusConnectorMessage originalMessage = persistenceService.findMessageByEbmsId(messageID);
        DomibusConnectorEvidenceType evidenceType = message.getMessageConfirmations().get(0).getEvidenceType();

        DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
        DomibusConnectorAction action = createEvidenceAction(evidenceType);
        details.setAction(action);
        details.setService(message.getMessageDetails().getService());
        details.setRefToMessageId(originalMessage.getMessageDetails().getEbmsMessageId());
        details.setConversationId(originalMessage.getMessageDetails().getConversationId());
        details.setFromParty(message.getMessageDetails().getFromParty());
        details.setToParty(message.getMessageDetails().getToParty());

        DomibusConnectorMessageConfirmation confirmation = null;
        try {
            confirmation = generateEvidence(evidenceType, originalMessage);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            DomibusConnectorMessageExceptionBuilder.createBuilder()                    
                    .setMessage(originalMessage)
                    .setText("Could not handle Evidence to Message " + messageID)
                    .setCause(e)
                    .setSource(this.getClass())
                    .buildAndThrow();
        }

        originalMessage.addConfirmation(confirmation);
        persistenceService.persistEvidenceForMessageIntoDatabase(originalMessage, confirmation.getEvidence(),
                evidenceType);

        DomibusConnectorMessage evidenceMessage = new DomibusConnectorMessage(details, confirmation);

        try {
			gwSubmissionService.submitToGateway(evidenceMessage);
		} catch (DomibusConnectorGatewaySubmissionException gwse) {
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(originalMessage)
                    .setText("Could not send Evidence Message to Gateway!")
                    .setSource(this.getClass())
                    .setCause(gwse)
                    .buildAndThrow();			
		}


        try {
            persistenceService.setEvidenceDeliveredToGateway(originalMessage, evidenceType);
        } catch(PersistenceException persistenceException) {
            LOGGER.error("persistence Exception occured", persistenceException);
        }

        if (!persistenceService.checkMessageConfirmed(originalMessage)) {        
            persistenceService.confirmMessage(originalMessage);
        }

        LOGGER.info("Successfully sent evidence of type {} for message {} to gateway.", confirmation.getEvidenceType(), originalMessage);
	}
	
	private DomibusConnectorMessageConfirmation generateEvidence(DomibusConnectorEvidenceType type, DomibusConnectorMessage originalMessage)
            throws DomibusConnectorEvidencesToolkitException, DomibusConnectorMessageException {
            return evidencesToolkit.createEvidence(type, originalMessage, DomibusConnectorRejectionReason.OTHER, null);
        
    }

    private DomibusConnectorAction createEvidenceAction(DomibusConnectorEvidenceType type) throws DomibusConnectorControllerException {
        switch (type) {
        case DELIVERY:
            return persistenceService.getDeliveryNonDeliveryToRecipientAction();
        case NON_DELIVERY:
            return persistenceService.getDeliveryNonDeliveryToRecipientAction();
        case RETRIEVAL:
            return persistenceService.getRetrievalNonRetrievalToRecipientAction();
        case NON_RETRIEVAL:
            return persistenceService.getRetrievalNonRetrievalToRecipientAction();
        default:
            throw new DomibusConnectorControllerException("Illegal Evidence type " + type + "! No Action found!");
        }
    }

}
