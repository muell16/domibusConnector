package eu.domibus.connector.controller.process;

import javax.annotation.Resource;

import eu.domibus.connector.persistence.service.*;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

/**
 * Takes a message from backend and creates evidences for it
 * and also wraps it into an asic container and delivers the
 * message to the gw
 */
@Component("BackendToGatewayMessageProcessor")
public class BackendToGatewayMessageProcessor implements DomibusConnectorMessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BackendToGatewayMessageProcessor.class);

	private DomibusConnectorMessagePersistenceService messagePersistenceService;

	private DomibusConnectorEvidencePersistenceService evidencePersistenceService;

	private DomibusConnectorActionPersistenceService actionPersistenceService;

	private DomibusConnectorGatewaySubmissionService gwSubmissionService;

	private DomibusConnectorEvidencesToolkit evidencesToolkit;

	private DomibusConnectorSecurityToolkit securityToolkit;

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
    public void setActionPersistenceService(DomibusConnectorActionPersistenceService actionPersistenceService) {
        this.actionPersistenceService = actionPersistenceService;
    }

    @Autowired
    public void setGwSubmissionService(DomibusConnectorGatewaySubmissionService gwSubmissionService) {
        this.gwSubmissionService = gwSubmissionService;
    }

    @Autowired
    public void setEvidencesToolkit(DomibusConnectorEvidencesToolkit evidencesToolkit) {
        this.evidencesToolkit = evidencesToolkit;
    }

    @Autowired
    public void setSecurityToolkit(DomibusConnectorSecurityToolkit securityToolkit) {
        this.securityToolkit = securityToolkit;
    }

    @Autowired
    public void setBackendDeliveryService(DomibusConnectorBackendDeliveryService backendDeliveryService) {
        this.backendDeliveryService = backendDeliveryService;
    }

    @Override
	public void processMessage(DomibusConnectorMessage message) {
		
		try {
			securityToolkit.buildContainer(message);
		} catch (DomibusConnectorSecurityException se) {
			createSubmissionRejectionAndReturnIt(message, se.getMessage());			
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText(se.getMessage())
                    .setSource(this.getClass())
                    .setCause(se)
                    .buildAndThrow();
		}

		DomibusConnectorMessageConfirmation confirmation = null;
		try {
			confirmation = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, message, null, null);
			LOGGER.debug("#processMessage: created confirmation [{}] for message [{}]", confirmation, message);
			// immediately persist new evidence into database
            evidencePersistenceService.persistEvidenceForMessageIntoDatabase(message, confirmation);

		} catch (DomibusConnectorEvidencesToolkitException ete) {
		    LOGGER.error("Could not generate evidence [{}] for message [{}]!", DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, message);
			createSubmissionRejectionAndReturnIt(message, ete.getMessage());
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not generate evidence submission acceptance! ")
                    .setSource(this.getClass())
                    .setCause(ete)
                    .buildAndThrow();
		}

		try {
			gwSubmissionService.submitToGateway(message);
		} catch (DomibusConnectorGatewaySubmissionException e) {
			createSubmissionRejectionAndReturnIt(message, e.getMessage());
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not submit message to gateway! ")
                    .setSource(this.getClass())
                    .setCause(e)
                    .buildAndThrow();
		}

		messagePersistenceService.setMessageDeliveredToGateway(message);
		try {
            evidencePersistenceService.setEvidenceDeliveredToGateway(message, confirmation);
		} catch (PersistenceException ex) {
			//TODO: handle exception    
			LOGGER.error("Exception occured", ex);
		}

		DomibusConnectorMessage returnMessage = buildEvidenceMessage(confirmation, message);

		backendDeliveryService.deliverMessageToBackend(returnMessage);


		try {
            evidencePersistenceService.setEvidenceDeliveredToNationalSystem(message, confirmation);
		} catch (PersistenceException ex) {
			//TODO: handle exception    
			LOGGER.error("Exception occured", ex);
		}

		LOGGER.info("Successfully sent message {} to gateway.", message);

	}

	private void createSubmissionRejectionAndReturnIt(DomibusConnectorMessage message, String errorMessage){

		DomibusConnectorMessageConfirmation confirmation = null;
		try {
			confirmation = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.SUBMISSION_REJECTION, message, DomibusConnectorRejectionReason.OTHER,
					errorMessage);
		} catch (DomibusConnectorEvidencesToolkitException e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not even generate submission rejection! ")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
		}

		try {
			// immediately persist new evidence into database
            evidencePersistenceService.persistEvidenceForMessageIntoDatabase(message, confirmation);
		} catch (Exception e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not persist evidence of type SUBMISSION_REJECTION! ")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();            
		}

		DomibusConnectorMessage returnMessage = buildEvidenceMessage(confirmation, message);

		backendDeliveryService.deliverMessageToBackend(returnMessage);     

		try {
            evidencePersistenceService.setEvidenceDeliveredToNationalSystem(message, confirmation);

			messagePersistenceService.rejectMessage(message);

		} catch (PersistenceException persistenceException) {
			//TODO: exception
			LOGGER.error("Persistence exceptoin occured while trying to send submission rejection. Rejection is not stored in Storage!", persistenceException);
		}

	}

    /**
     * prepares an evidence message for sending back to the backend
     *  for this purpose the action is set to SubmissionAcceptanceRejection
     *  which is the action for submission -acceptance and -rejection evidences
     *
     * @param confirmation the confirmation to send back
     * @param originalMessage the message the confirmation belongs to
     * @return the created evidence message
     */
	private DomibusConnectorMessage buildEvidenceMessage(DomibusConnectorMessageConfirmation confirmation, DomibusConnectorMessage originalMessage) {
		DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
		details.setRefToMessageId(originalMessage.getMessageDetails().getBackendMessageId());
		details.setService(originalMessage.getMessageDetails().getService());

		DomibusConnectorAction action = actionPersistenceService.getAction("SubmissionAcceptanceRejection");
		details.setAction(action);

		DomibusConnectorMessage returnMessage = new DomibusConnectorMessage(details, confirmation);

		return returnMessage;
	}

}
