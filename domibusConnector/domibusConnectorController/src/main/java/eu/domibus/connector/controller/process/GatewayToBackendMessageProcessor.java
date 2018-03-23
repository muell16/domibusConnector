package eu.domibus.connector.controller.process;

import javax.annotation.Resource;

import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.persistence.service.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
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

@Component("GatewayToBackendMessageProcessor")
public class GatewayToBackendMessageProcessor implements DomibusConnectorMessageProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayToBackendMessageProcessor.class);
	
	@Value("${domibus.connector.to.connector.test.service:null}")
	private String connectorTestService;
	
	@Value("${domibus.connector.to.connector.test.action:null}")
	private String connectorTestAction;

	private DomibusConnectorMessagePersistenceService messagePersistenceService;
	private DomibusConnectorEvidencePersistenceService evidencePersistenceService;
	private DomibusConnectorGatewaySubmissionService gwSubmissionService;
	private DomibusConnectorMessageIdGenerator messageIdGenerator;
	private DomibusConnectorEvidencesToolkit evidencesToolkit;
	private DomibusConnectorSecurityToolkit securityToolkit;
	private DomibusConnectorBackendDeliveryService backendDeliveryService;
	private DomibusConnectorActionPersistenceService actionPersistenceService;

	public void setConnectorTestService(String connectorTestService) {
		this.connectorTestService = connectorTestService;
	}

	public void setConnectorTestAction(String connectorTestAction) {
		this.connectorTestAction = connectorTestAction;
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
	public void setGwSubmissionService(DomibusConnectorGatewaySubmissionService gwSubmissionService) {
		this.gwSubmissionService = gwSubmissionService;
	}

	@Autowired
	public void setMessageIdGenerator(DomibusConnectorMessageIdGenerator messageIdGenerator) {
		this.messageIdGenerator = messageIdGenerator;
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

	@Autowired
	public void setActionPersistenceService(DomibusConnectorActionPersistenceService actionPersistenceService) {
		this.actionPersistenceService = actionPersistenceService;
	}

	@Override
	@StoreMessageExceptionIntoDatabase
	public void processMessage(DomibusConnectorMessage message) {
		LOGGER.trace("#processMessage: start processing message [{}] with confirmations [{}]", message, message.getMessageConfirmations());
		
		createRelayREMMDEvidenceAndSendIt(message, true);
		
		
		try {
		    LOGGER.debug("#processMessage: call validateContainer");
			securityToolkit.validateContainer(message);
		} catch (DomibusConnectorSecurityException e) {
			createNonDeliveryEvidenceAndSendIt(message);
			throw e;
		}
		
		if(isConnector2ConnectorTest(message)){
			// if it is a connector to connector test message defined by service and action, do NOT deliver message to the backend, but 
			// only send a DELIVERY evidence back.
			LOGGER.info("#processMessage: Message [{}] is a connector to connector test message. \nIt will NOT be delivered to the backend!", message);
			createDeliveryEvidenceAndSendIt(message);
			LOGGER.info("#processMessage: Connector to Connector Test message [{}] is confirmed!", message);
		}else{
			backendDeliveryService.deliverMessageToBackend(message);
		}

		 // TODO this needs to be done by the backend link!!!
//		persistenceService.setMessageDeliveredToNationalSystem(message);

		LOGGER.info("Successfully processed message {} from GW to backend.", message.getConnectorMessageId());
	}
	
	private boolean isConnector2ConnectorTest(DomibusConnectorMessage message) {
		return (!StringUtils.isEmpty(connectorTestService) && message.getMessageDetails().getService().getService().equals(connectorTestService)) 
				&& (!StringUtils.isEmpty(connectorTestAction) && message.getMessageDetails().getAction().getAction().equals(connectorTestAction));
	}
	
	private void createDeliveryEvidenceAndSendIt(DomibusConnectorMessage originalMessage)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {

		DomibusConnectorMessageConfirmation delivery = null;
		try {
			delivery = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.DELIVERY, originalMessage, null, null);
			originalMessage.addConfirmation(delivery);
		} catch (DomibusConnectorEvidencesToolkitException e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(originalMessage)
                    .setText("Error creating Delivery evidence for message!")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
		}

		DomibusConnectorAction action = actionPersistenceService.getDeliveryNonDeliveryToRecipientAction();

		sendEvidenceToBackToGateway(originalMessage, action, delivery);

		messagePersistenceService.confirmMessage(originalMessage);
	}
	
	private void createNonDeliveryEvidenceAndSendIt(DomibusConnectorMessage originalMessage)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {

		DomibusConnectorMessageConfirmation nonDelivery = null;
		try {
			nonDelivery = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.NON_DELIVERY, originalMessage,DomibusConnectorRejectionReason.OTHER, null);
		} catch (DomibusConnectorEvidencesToolkitException e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(originalMessage)
                    .setText("Error creating NonDelivery evidence for message!")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
		}

		DomibusConnectorAction action = actionPersistenceService.getDeliveryNonDeliveryToRecipientAction();

		sendEvidenceToBackToGateway(originalMessage, action, nonDelivery);

		messagePersistenceService.rejectMessage(originalMessage);
	}
	
	private void createRelayREMMDEvidenceAndSendIt(DomibusConnectorMessage originalMessage, boolean isAcceptance)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {
		DomibusConnectorMessageConfirmation messageConfirmation = null;
		try {
			messageConfirmation = isAcceptance ? evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE, originalMessage, null, null)
					: evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.RELAY_REMMD_REJECTION, originalMessage, DomibusConnectorRejectionReason.OTHER, null);
		} catch (DomibusConnectorEvidencesToolkitException e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(originalMessage)
                    .setText("Error creating RelayREMMD evidence for message!")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
		}

		DomibusConnectorAction action = actionPersistenceService.getRelayREMMDAcceptanceRejectionAction();

		sendEvidenceToBackToGateway(originalMessage, action, messageConfirmation);

		if (!isAcceptance) {
			messagePersistenceService.rejectMessage(originalMessage);
		}
	}
	
	private void sendEvidenceToBackToGateway(DomibusConnectorMessage originalMessage, DomibusConnectorAction action,
			DomibusConnectorMessageConfirmation messageConfirmation) throws DomibusConnectorControllerException,
	DomibusConnectorMessageException {

		originalMessage.addConfirmation(messageConfirmation);
		evidencePersistenceService.persistEvidenceForMessageIntoDatabase(originalMessage, messageConfirmation);

		DomibusConnectorMessageDetails originalDetails = originalMessage.getMessageDetails();
		DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
		BeanUtils.copyProperties(originalDetails, details);

		details.setFromParty(originalDetails.getToParty());
		details.setToParty(originalDetails.getFromParty());
		details.setRefToMessageId(originalMessage.getMessageDetails().getEbmsMessageId());
		details.setAction(action);


		DomibusConnectorMessage evidenceMessage = new DomibusConnectorMessage(details, messageConfirmation);
		
        try {
            this.gwSubmissionService.submitToGateway(evidenceMessage);
        } catch (Exception e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(originalMessage)
                    .setText("Exception sending confirmation message '" + originalMessage.getConnectorMessageId() + "' back to gateway ")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
        }


        try {
            evidencePersistenceService.setEvidenceDeliveredToGateway(originalMessage, messageConfirmation);
        } catch (PersistenceException ex) {
        	String error = String.format("Confirmation [%s] could not set to 'delivered' to GW", messageConfirmation);
            LOGGER.error(error, ex);
            //TODO: further exception handling!
        }
	}

}
