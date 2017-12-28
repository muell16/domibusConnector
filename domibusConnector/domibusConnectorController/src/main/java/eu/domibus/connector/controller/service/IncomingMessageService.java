package eu.domibus.connector.controller.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.common.exception.DomibusConnectorMessageException;
import eu.domibus.connector.common.exception.ImplementationMissingException;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClientException;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.domain.Action;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageConfirmation;
import eu.domibus.connector.domain.MessageDetails;
import eu.domibus.connector.domain.enums.EvidenceType;
import eu.domibus.connector.domain.enums.MessageDirection;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.evidences.type.RejectionReason;
import eu.domibus.connector.mapping.exception.DomibusConnectorContentMapperException;
import eu.domibus.connector.nbc.exception.DomibusConnectorNationalBackendClientException;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

public class IncomingMessageService extends AbstractMessageService implements MessageService {

	static Logger LOGGER = LoggerFactory.getLogger(IncomingMessageService.class);

	@Override
	public void handleMessage(Message message) throws DomibusConnectorControllerException,
	DomibusConnectorMessageException {

//		try {
			persistenceService.persistMessageIntoDatabase(message, MessageDirection.GW_TO_NAT);
//		} catch (PersistenceException e1) {
//			createRelayREMMDEvidenceAndSendIt(message, false);
//			LOGGER.error("Message could not be persisted!", e1);
//			return;
//		}

		if (connectorProperties.isUseEvidencesToolkit()) {
			createRelayREMMDEvidenceAndSendIt(message, true);
		}

		if (connectorProperties.isUseSecurityToolkit()) {
			try {
				securityToolkit.validateContainer(message);
			} catch (DomibusConnectorSecurityException e) {
				createNonDeliveryEvidenceAndSendIt(message);
				throw e;
			}
		}

		if (connectorProperties.isUseContentMapper()) {
			try {
				contentMapper.mapInternationalToNational(message);
			} catch (DomibusConnectorContentMapperException e) {
				createNonDeliveryEvidenceAndSendIt(message);
				throw new DomibusConnectorMessageException(message,
						"Error mapping content of message into national format!", e, this.getClass());
			} catch (ImplementationMissingException e) {
				createNonDeliveryEvidenceAndSendIt(message);
				throw new DomibusConnectorMessageException(message, e.getMessage(), e, this.getClass());
			}
			persistenceService.mergeMessageWithDatabase(message);
		}

		if(isConnector2ConnectorTest(message)){
			// if it is a connector to connector test message defined by service and action, do NOT deliver message to the backend, but 
			// only send a DELIVERY evidence back.
			LOGGER.info("Message with id {} is a connector to connector test message. \nIt will not be delivered to the backend!");
			createDeliveryEvidenceAndSendIt(message);
			LOGGER.info("Connector to Connector Test message is confirmed!");
		}else{
			try {
				nationalBackendClient.deliverMessage(message);
			} catch (DomibusConnectorNationalBackendClientException e) {
				createNonDeliveryEvidenceAndSendIt(message);
				throw new DomibusConnectorMessageException(message, "Error delivering message to national backend client!",
						e, this.getClass());
			} catch (ImplementationMissingException e) {
				createNonDeliveryEvidenceAndSendIt(message);
				throw new DomibusConnectorMessageException(message, e.getMessage(), e, this.getClass());
			}
		}

		persistenceService.setMessageDeliveredToNationalSystem(message);

		LOGGER.info("Successfully processed message with id {} from GW to NAT.", message.getDbMessageId());

	}

	private boolean isConnector2ConnectorTest(Message message) {
		return (!StringUtils.isEmpty(connectorProperties.getConnectorTestService()) && message.getMessageDetails().getService().getService().equals(connectorProperties.getConnectorTestService())) 
				&& (!StringUtils.isEmpty(connectorProperties.getConnectorTestAction()) && message.getMessageDetails().getAction().getAction().equals(connectorProperties.getConnectorTestAction()));
	}

	private void createNonDeliveryEvidenceAndSendIt(Message originalMessage)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {

		MessageConfirmation nonDelivery = null;
		try {
			nonDelivery = evidencesToolkit.createEvidence(EvidenceType.NON_DELIVERY, originalMessage,RejectionReason.OTHER, null);
		} catch (DomibusConnectorEvidencesToolkitException e) {
			throw new DomibusConnectorMessageException(originalMessage,
					"Error creating NonDelivery evidence for message!", e, this.getClass());
		}

		Action action = persistenceService.getDeliveryNonDeliveryToRecipientAction();

		sendEvidenceToBackToGateway(originalMessage, action, nonDelivery);

		persistenceService.rejectMessage(originalMessage);
	}
	
	private void createDeliveryEvidenceAndSendIt(Message originalMessage)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {

		MessageConfirmation delivery = null;
		try {
			delivery = evidencesToolkit.createEvidence(EvidenceType.DELIVERY,originalMessage, null, null);
		} catch (DomibusConnectorEvidencesToolkitException e) {
			throw new DomibusConnectorMessageException(originalMessage,
					"Error creating Delivery evidence for message!", e, this.getClass());
		}

		Action action = persistenceService.getDeliveryNonDeliveryToRecipientAction();

		sendEvidenceToBackToGateway(originalMessage, action, delivery);

		persistenceService.confirmMessage(originalMessage);
	}

	private void createRelayREMMDEvidenceAndSendIt(Message originalMessage, boolean isAcceptance)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {
		MessageConfirmation messageConfirmation = null;
		try {
			messageConfirmation = isAcceptance ? evidencesToolkit.createEvidence(EvidenceType.RELAY_REMMD_ACCEPTANCE, originalMessage, null, null)
					: evidencesToolkit.createEvidence(EvidenceType.RELAY_REMMD_REJECTION, originalMessage, RejectionReason.OTHER, null);
		} catch (DomibusConnectorEvidencesToolkitException e) {
			throw new DomibusConnectorMessageException(originalMessage,
					"Error creating RelayREMMD evidence for message!", e, this.getClass());
		}

		Action action = persistenceService.getRelayREMMDAcceptanceRejectionAction();

		sendEvidenceToBackToGateway(originalMessage, action, messageConfirmation);

		if (!isAcceptance) {
			persistenceService.rejectMessage(originalMessage);
		}
	}

	private void sendEvidenceToBackToGateway(Message originalMessage, Action action,
			MessageConfirmation messageConfirmation) throws DomibusConnectorControllerException,
	DomibusConnectorMessageException {

		originalMessage.addConfirmation(messageConfirmation);
		persistenceService.persistEvidenceForMessageIntoDatabase(originalMessage, messageConfirmation.getEvidence(),
				messageConfirmation.getEvidenceType());

		MessageDetails details = new MessageDetails();
		details.setRefToMessageId(originalMessage.getMessageDetails().getEbmsMessageId());
		details.setConversationId(originalMessage.getMessageDetails().getConversationId());
		details.setService(originalMessage.getMessageDetails().getService());
		details.setAction(action);
		details.setFromParty(originalMessage.getMessageDetails().getToParty());
		details.setToParty(originalMessage.getMessageDetails().getFromParty());

		Message evidenceMessage = new Message(details, messageConfirmation);

		try {
			gatewayWebserviceClient.sendMessage(evidenceMessage);
		} catch (DomibusConnectorGatewayWebserviceClientException e) {
			throw new DomibusConnectorMessageException(originalMessage,
					"Exception sending evidence back to sender gateway of message "
							+ originalMessage.getMessageDetails().getEbmsMessageId(), e, this.getClass());
		}

		persistenceService.setEvidenceDeliveredToGateway(originalMessage, messageConfirmation.getEvidenceType());
	}
}
