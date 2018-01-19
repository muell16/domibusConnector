package eu.domibus.connector.controller.process;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
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
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

@Component("BackendToGatewayMessageProcessor")
public class BackendToGatewayMessageProcessor implements DomibusConnectorMessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BackendToGatewayMessageProcessor.class);

	@Resource
	private DomibusConnectorPersistenceService persistenceService;

	@Resource
	private DomibusConnectorGatewaySubmissionService gwSubmissionService;

	@Resource
	private DomibusConnectorEvidencesToolkit evidencesToolkit;

	@Resource
	private DomibusConnectorSecurityToolkit securityToolkit;
	
	@Resource
	private DomibusConnectorMessageIdGenerator messageIdGenerator;
	
	@Resource
	private DomibusConnectorBackendDeliveryService backendDeliveryService;

	@Override
	public void processMessage(DomibusConnectorMessage message) {


		try {
			securityToolkit.buildContainer(message);
		} catch (DomibusConnectorSecurityException se) {
			createSubmissionRejectionAndReturnIt(message, se.getMessage());
			throw new DomibusConnectorMessageException(message, se.getMessage(), se, this.getClass());
		}

		DomibusConnectorMessageConfirmation confirmation = null;
		try {
			confirmation = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, message, null, null);
			// immediately persist new evidence into database
			persistenceService.persistEvidenceForMessageIntoDatabase(message, confirmation.getEvidence(),
					DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE);

		} catch (DomibusConnectorEvidencesToolkitException ete) {
			createSubmissionRejectionAndReturnIt(message, ete.getMessage());
			throw new DomibusConnectorMessageException(message,
					"Could not generate evidence submission acceptance! ", ete, this.getClass());
		}

		try {
			gwSubmissionService.submitToGateway(message);
		} catch (DomibusConnectorGatewaySubmissionException e) {
			createSubmissionRejectionAndReturnIt(message, e.getMessage());
			throw new DomibusConnectorMessageException(message,
					"Could not submit message to gateway! ", e, this.getClass());
		}

		persistenceService.setMessageDeliveredToGateway(message);
		try {
			persistenceService.setEvidenceDeliveredToGateway(message, confirmation.getEvidenceType());
		} catch (PersistenceException ex) {
			//TODO: handle exception    
			LOGGER.error("Exception occured", ex);
		}

		DomibusConnectorMessage returnMessage = buildEvidenceMessage(confirmation, message);

		backendDeliveryService.deliverMessageToBackend(returnMessage);


		try {
			persistenceService.setEvidenceDeliveredToNationalSystem(message, confirmation.getEvidenceType());
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
			new DomibusConnectorMessageException(message, "Could not even generate submission rejection! ", e,
					this.getClass());
			LOGGER.error("Could not even generate submission rejection! ", e);
			return;
		}

		try {
			// immediately persist new evidence into database
			persistenceService.persistEvidenceForMessageIntoDatabase(message, confirmation.getEvidence(),
					DomibusConnectorEvidenceType.SUBMISSION_REJECTION);
		} catch (Exception e) {
			new DomibusConnectorMessageException(message, "Could not persist evidence of type SUBMISSION_REJECTION! ",
					e, this.getClass());
			LOGGER.error("Could not persist evidence of type SUBMISSION_REJECTION! ", e);
			return;
		}

		DomibusConnectorMessage returnMessage = buildEvidenceMessage(confirmation, message);

		backendDeliveryService.deliverMessageToBackend(returnMessage);     

		try {
			persistenceService.setEvidenceDeliveredToNationalSystem(message, confirmation.getEvidenceType());

			persistenceService.rejectMessage(message);

		} catch (PersistenceException persistenceException) {
			//TODO: exception
			LOGGER.error("Persistence exceptoin occured while trying to send submission rejection. Rejection is not stored in Storage!", persistenceException);
		}

	}

	private DomibusConnectorMessage buildEvidenceMessage(DomibusConnectorMessageConfirmation confirmation, DomibusConnectorMessage originalMessage) {
		DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
		details.setRefToMessageId(originalMessage.getMessageDetails().getBackendMessageId());
		details.setService(originalMessage.getMessageDetails().getService());
		
		DomibusConnectorAction action = persistenceService.getAction("SubmissionAcceptanceRejection");
		details.setAction(action);

		DomibusConnectorMessage returnMessage = new DomibusConnectorMessage(details, confirmation);

		return returnMessage;
	}

}
