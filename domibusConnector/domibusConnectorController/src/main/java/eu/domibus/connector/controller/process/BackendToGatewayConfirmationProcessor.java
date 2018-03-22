package eu.domibus.connector.controller.process;

import javax.annotation.Resource;

import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.persistence.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Takes an confirmation message from backend
 * and creates a new confirmation of the same confirmation type
 * and sends it to the gw
 *
 */
@Component("BackendToGatewayConfirmationProcessor")
public class BackendToGatewayConfirmationProcessor implements DomibusConnectorMessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BackendToGatewayConfirmationProcessor.class);

	private DomibusConnectorActionPersistenceService actionPersistenceService;

    private DomibusConnectorMessagePersistenceService messagePersistenceService;

    private DomibusConnectorEvidencePersistenceService evidencePersistenceService;

	private DomibusConnectorEvidencesToolkit evidencesToolkit;

	private DomibusConnectorGatewaySubmissionService gwSubmissionService;

    //setter
    @Autowired
    public void setActionPersistenceService(DomibusConnectorActionPersistenceService actionPersistenceService) {
        this.actionPersistenceService = actionPersistenceService;
    }

    @Autowired
    public void setMessagePersistenceService(DomibusConnectorMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    @Autowired
    public void setEvidencePersistenceService(DomibusConnectorEvidencePersistenceService evidencePersistenceService) {
        this.evidencePersistenceService = evidencePersistenceService;
    }

    @Autowired
    public void setEvidencesToolkit(DomibusConnectorEvidencesToolkit evidencesToolkit) {
        this.evidencesToolkit = evidencesToolkit;
    }

    @Autowired
    public void setGwSubmissionService(DomibusConnectorGatewaySubmissionService gwSubmissionService) {
        this.gwSubmissionService = gwSubmissionService;
    }

    @Override
    @Transactional(propagation=Propagation.NEVER)
    @StoreMessageExceptionIntoDatabase
	public void processMessage(DomibusConnectorMessage message) {
        if (!DomainModelHelper.isEvidenceMessage(message)) {
            throw new IllegalArgumentException("The message is not an evidence message!");
        }

		String messageID = message.getMessageDetails().getRefToMessageId();
        LOGGER.debug("#processMessage: refToMessageId is [{}]", messageID);
        DomibusConnectorMessage originalMessage = messagePersistenceService.findMessageByEbmsId(messageID);
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
        evidencePersistenceService.persistEvidenceForMessageIntoDatabase(originalMessage, confirmation.getEvidence(),
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
            evidencePersistenceService.setEvidenceDeliveredToGateway(originalMessage, evidenceType);
        } catch(PersistenceException persistenceException) {
            LOGGER.error("persistence Exception occured", persistenceException);
        }

        if (!messagePersistenceService.checkMessageConfirmed(originalMessage)) {
            messagePersistenceService.confirmMessage(originalMessage);
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
            return actionPersistenceService.getDeliveryNonDeliveryToRecipientAction();
        case NON_DELIVERY:
            return actionPersistenceService.getDeliveryNonDeliveryToRecipientAction();
        case RETRIEVAL:
            return actionPersistenceService.getRetrievalNonRetrievalToRecipientAction();
        case NON_RETRIEVAL:
            return actionPersistenceService.getRetrievalNonRetrievalToRecipientAction();
        default:
            throw new DomibusConnectorControllerException("Illegal Evidence type " + type + "! No Action found!");
        }
    }

}
