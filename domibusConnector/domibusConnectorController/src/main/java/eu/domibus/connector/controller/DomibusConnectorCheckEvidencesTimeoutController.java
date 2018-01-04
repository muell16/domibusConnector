package eu.domibus.connector.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.common.CommonConnectorProperties;
//import eu.domibus.connector.persistence.model.DomibusConnectorAction;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.common.exception.ImplementationMissingException;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.domain.Action;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageConfirmation;
import eu.domibus.connector.domain.MessageDetails;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.nbc.exception.DomibusConnectorNationalBackendClientException;
import eu.domibus.connector.persistence.service.PersistenceException;
import java.util.logging.Level;
import eu.domibus.connector.nbc.DomibusConnectorRemoteNationalBackendService;

public class DomibusConnectorCheckEvidencesTimeoutController implements DomibusConnectorController {

	static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorCheckEvidencesTimeoutController.class);

	private DomibusConnectorRemoteNationalBackendService nationalBackendClient;
	private DomibusConnectorPersistenceService persistenceService;
	private CommonConnectorProperties connectorProperties;
	private DomibusConnectorEvidencesToolkit evidencesToolkit;

	public void setPersistenceService(DomibusConnectorPersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setConnectorProperties(CommonConnectorProperties connectorProperties) {
		this.connectorProperties = connectorProperties;
	}

	public void setEvidencesToolkit(DomibusConnectorEvidencesToolkit evidencesToolkit) {
		this.evidencesToolkit = evidencesToolkit;
	}

	@Override
	public void execute() throws DomibusConnectorControllerException {
		if (connectorProperties.isCheckEvidences()) {
			LOGGER.debug("Job for checking evidence timeouts triggered.");
					Date start = new Date();

					// only check for timeout of RELAY_REMMD_ACCEPTANCE/REJECTION evidences if the timeout is set in the connector.properties
					if(connectorProperties.getTimeoutRelayREMMD() > 0)
						checkNotRejectedNorConfirmedWithoutRelayREMMD();

					// only check for timeout of DELIVERY/NON_DELIVERY evidences if the timeout is set in the connector.properties
					if(connectorProperties.getTimeoutDelivery() > 0)
						checkNotRejectedWithoutDelivery();

					LOGGER.debug("Job for checking evidence timeouts finished in {} ms.",
							(System.currentTimeMillis() - start.getTime()));
		} else {
			LOGGER.debug("Property connector.use.evidences.timeout set to false.");
		}
	}
	
	private void checkNotRejectedNorConfirmedWithoutRelayREMMD() throws DomibusConnectorControllerException {
		//Request database to get all messages not rejected and not confirmed yet and without a RELAY_REMMD_ACCEPTANCE/REJECTION evidence
		List<DomibusConnectorMessage> messages = persistenceService.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
		Date now = new Date();
		if (messages != null && !messages.isEmpty()) {
			for (DomibusConnectorMessage message : messages) {
				//Evaluate time in ms the reception of a RELAY_REMMD_ACCEPTANCE/REJECTION for the message times out
				//Date delivered = message.getDbMessage().getDeliveredToGateway(); //TODO:
                Date delivered = new Date();
				long relayRemmdTimout = delivered.getTime() + connectorProperties.getTimeoutRelayREMMD();
				
				//if it is later then the evaluated timeout given
				if (now.getTime() > relayRemmdTimout) {
						try {
							createRelayRemmdFailureAndSendIt(message);
						} catch (DomibusConnectorMessageException e) {
							throw new DomibusConnectorControllerException(e);
						}
						continue;
				}
			}
		}
	}

	private void checkNotRejectedWithoutDelivery() throws DomibusConnectorControllerException {
		//Request database to get all messages not rejected yet and without a DELIVERY/NON_DELIVERY evidence
		List<DomibusConnectorMessage> messages = persistenceService.findOutgoingMessagesNotRejectedAndWithoutDelivery();
		Date now = new Date();
		if (messages != null && !messages.isEmpty()) {
			for (DomibusConnectorMessage message : messages) {
				//Evaluate time in ms the reception of a DELIVERY/NON_DELIVERY for the message times out
				//Date delivered = message.getDbMessage().getDeliveredToGateway(); //TODO
                Date delivered = new Date();
				long deliveryTimeout = delivered.getTime() + connectorProperties.getTimeoutDelivery();
				
				//if it is later then the evaluated timeout given
				if (now.getTime() > deliveryTimeout) {
					try {
						createNonDeliveryAndSendIt(message);
					} catch (DomibusConnectorMessageException e) {
						throw new DomibusConnectorControllerException(e);
					}
					continue;
				}
			}
		}
	}

	private void createRelayRemmdFailureAndSendIt(DomibusConnectorMessage originalMessage) throws DomibusConnectorControllerException,
	DomibusConnectorMessageException {

		LOGGER.info("The RelayREMMDAcceptance/Rejection evidence for message {} timed out. Sending RELAY_REMMD_FAILURE to backend!", originalMessage
				.getMessageDetails().getEbmsMessageId());

		DomibusConnectorMessageConfirmation relayRemMDFailure = null;

		try {
			relayRemMDFailure = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.RELAY_REMMD_FAILURE, originalMessage, DomibusConnectorRejectionReason.OTHER, null);
		} catch (DomibusConnectorEvidencesToolkitException e) {
			throw new DomibusConnectorMessageException(originalMessage,
					"Error creating RelayREMMDFailure for message!", e, this.getClass());
		}

		DomibusConnectorAction action = persistenceService.getRelayREMMDFailure();

		sendEvidenceToNationalSystem(originalMessage, relayRemMDFailure, action);
	}

	private void createNonDeliveryAndSendIt(DomibusConnectorMessage originalMessage) throws DomibusConnectorControllerException,
	DomibusConnectorMessageException {
		
		LOGGER.info("The Delivery/NonDelivery evidence for message {} timed out. Sending NonDelivery to backend!", originalMessage
				.getMessageDetails().getEbmsMessageId());
		DomibusConnectorMessageConfirmation nonDelivery = null;
		try {
			nonDelivery = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.NON_DELIVERY, originalMessage, DomibusConnectorRejectionReason.OTHER, null);
		} catch (DomibusConnectorEvidencesToolkitException e) {
			throw new DomibusConnectorMessageException(originalMessage, "Error creating NonDelivery for message!", e,
					this.getClass());
		}

		DomibusConnectorAction action = persistenceService.getDeliveryNonDeliveryToRecipientAction();

		sendEvidenceToNationalSystem(originalMessage, nonDelivery, action);
	}


	private void sendEvidenceToNationalSystem(DomibusConnectorMessage originalMessage, DomibusConnectorMessageConfirmation confirmation,
			DomibusConnectorAction evidenceAction) throws DomibusConnectorControllerException,
	DomibusConnectorMessageException {

		try {
            
            originalMessage.addConfirmation(confirmation);
            persistenceService.persistEvidenceForMessageIntoDatabase(originalMessage, confirmation.getEvidence(),
                    confirmation.getEvidenceType());
            
            DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
            details.setRefToMessageId(originalMessage.getMessageDetails().getBackendMessageId());
            details.setConversationId(originalMessage.getMessageDetails().getConversationId());
            details.setService(originalMessage.getMessageDetails().getService());
            details.setAction(evidenceAction);
            
            DomibusConnectorMessage evidenceMessage = new DomibusConnectorMessage(details, confirmation);
            
            try {
                nationalBackendClient.deliverLastEvidenceForMessage(evidenceMessage);
            } catch (DomibusConnectorNationalBackendClientException e) {
                throw new DomibusConnectorMessageException(originalMessage, "Exception sending "
                        + confirmation.getEvidenceType().toString() + " evidence back to national system for message "
                        + originalMessage.getMessageDetails().getBackendMessageId(), e, this.getClass());
            } catch (ImplementationMissingException e) {
                throw new DomibusConnectorControllerException(e);
            }
            
            persistenceService.setEvidenceDeliveredToNationalSystem(originalMessage, confirmation.getEvidenceType());
            persistenceService.rejectMessage(originalMessage);
        } catch (PersistenceException ex) {
            LOGGER.error("PersistenceException occured", ex);
            throw new RuntimeException(ex);
            //TODO: handle exception
		}
	}

	public void setNationalBackendClient(DomibusConnectorRemoteNationalBackendService nationalBackendClient) {
		this.nationalBackendClient = nationalBackendClient;
	}
}
