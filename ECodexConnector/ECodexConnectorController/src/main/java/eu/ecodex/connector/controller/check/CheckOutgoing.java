package eu.ecodex.connector.controller.check;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
import eu.ecodex.connector.common.enums.ActionEnum;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.evidences.ECodexConnectorEvidencesToolkit;
import eu.ecodex.connector.evidences.exception.ECodexConnectorEvidencesToolkitException;
import eu.ecodex.connector.evidences.type.RejectionReason;
import eu.ecodex.connector.nbc.ECodexConnectorNationalBackendClient;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public class CheckOutgoing {

    static Logger LOGGER = LoggerFactory.getLogger(CheckOutgoing.class);

    private ECodexConnectorNationalBackendClient nationalBackendClient;
    private ECodexConnectorPersistenceService persistenceService;
    private ECodexConnectorProperties connectorProperties;
    private ECodexConnectorEvidencesToolkit evidencesToolkit;

    public void setPersistenceService(ECodexConnectorPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setConnectorProperties(ECodexConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    public void setEvidencesToolkit(ECodexConnectorEvidencesToolkit evidencesToolkit) {
        this.evidencesToolkit = evidencesToolkit;
    }

    public void checkEvidences() throws ECodexConnectorControllerException {
        LOGGER.info("Check outgoing triggered.");
        Date start = new Date();

        List<Message> unconfirmedOutgoing = persistenceService.findOutgoingUnconfirmedMessages();
        if (unconfirmedOutgoing != null && !unconfirmedOutgoing.isEmpty()) {
            for (Message unconfirmed : unconfirmedOutgoing) {
                Date delivered = unconfirmed.getDbMessage().getDeliveredToGateway();
                Date now = new Date();
                long relayRemmdTimout = delivered.getTime() + connectorProperties.getTimeoutRelayREMMD();
                long deliveryTimeout = delivered.getTime() + connectorProperties.getTimeoutDelivery();
                long retrievalTimeout = delivered.getTime() + connectorProperties.getTimeoutRetrieval();

                if (now.getTime() > relayRemmdTimout) {

                    boolean relayRemmdFound = checkEvidencesForRelayRemmd(unconfirmed);
                    if (!relayRemmdFound) {
                        createRelayRemmdRejectionAndSendIt(unconfirmed);
                        continue;
                    }
                }

                if (isStillUnconfirmed(unconfirmed) && now.getTime() > deliveryTimeout) {
                    boolean deliveryFound = checkEvidencesForDelivery(unconfirmed);
                    if (!deliveryFound) {
                        createNonDeliveryAndSendIt(unconfirmed);
                        continue;
                    }
                }

                if (isStillUnconfirmed(unconfirmed) && now.getTime() > retrievalTimeout) {
                    boolean retrievalFound = checkEvidencesForRetrieval(unconfirmed);
                    if (!retrievalFound)
                        createNonRetrievalAndSendIt(unconfirmed);
                    continue;
                }

            }
        }

        LOGGER.info("Check outgoing finished in {} ms.", (System.currentTimeMillis() - start.getTime()));
    }

    private boolean isStillUnconfirmed(Message message) {
        return message.getDbMessage().getConfirmed() == null && message.getDbMessage().getRejected() == null;
    }

    private boolean checkEvidencesForRelayRemmd(Message message) {
        if (checkEvidencesForType(message, ECodexEvidenceType.RELAY_REMMD_ACCEPTANCE)) {
            return true;
        } else if (checkEvidencesForType(message, ECodexEvidenceType.RELAY_REMMD_REJECTION)) {
            persistenceService.rejectMessage(message);
            return true;
        }
        return false;
    }

    private boolean checkEvidencesForDelivery(Message message) {
        if (checkEvidencesForType(message, ECodexEvidenceType.DELIVERY)) {
            return true;
        } else if (checkEvidencesForType(message, ECodexEvidenceType.NON_DELIVERY)) {
            persistenceService.rejectMessage(message);
            return true;
        }
        return false;
    }

    private boolean checkEvidencesForRetrieval(Message message) {
        if (checkEvidencesForType(message, ECodexEvidenceType.RETRIEVAL)) {
            persistenceService.confirmMessage(message);
            return true;
        } else if (checkEvidencesForType(message, ECodexEvidenceType.NON_RETRIEVAL)) {
            persistenceService.rejectMessage(message);
            return true;
        }
        return false;
    }

    private void createRelayRemmdRejectionAndSendIt(Message originalMessage) throws ECodexConnectorControllerException {
        LOGGER.info("The RelayREMMDAcceptance evidence for message {} timed out. Will send rejection!", originalMessage
                .getMessageDetails().getEbmsMessageId());
        MessageConfirmation relayRemMDRejection = null;
        try {
            relayRemMDRejection = evidencesToolkit.createRelayREMMDRejection(RejectionReason.OTHER, originalMessage);
        } catch (ECodexConnectorEvidencesToolkitException e) {
            throw new ECodexConnectorControllerException("Error creating RelayREMMDRejection for message!", e);
        }

        sendEvidenceToNationalSystem(originalMessage, relayRemMDRejection, ActionEnum.RelayREMMDAcceptanceRejection);
    }

    private void createNonDeliveryAndSendIt(Message originalMessage) throws ECodexConnectorControllerException {
        LOGGER.info("The Delivery evidence for message {} timed out. Will send NonDelivery!", originalMessage
                .getMessageDetails().getEbmsMessageId());
        MessageConfirmation nonDelivery = null;
        try {
            nonDelivery = evidencesToolkit.createNonDeliveryEvidence(RejectionReason.OTHER, originalMessage);
        } catch (ECodexConnectorEvidencesToolkitException e) {
            throw new ECodexConnectorControllerException("Error creating NonDelivery for message!", e);
        }

        sendEvidenceToNationalSystem(originalMessage, nonDelivery, ActionEnum.DeliveryNonDeliveryToRecipient);
    }

    private void createNonRetrievalAndSendIt(Message originalMessage) throws ECodexConnectorControllerException {
        LOGGER.info("The Retrieval evidence for message {} timed out. Will send NonRetrieval!", originalMessage
                .getMessageDetails().getEbmsMessageId());
        MessageConfirmation nonRetrieval = null;
        try {
            nonRetrieval = evidencesToolkit.createNonRetrievalEvidence(RejectionReason.OTHER, originalMessage);
        } catch (ECodexConnectorEvidencesToolkitException e) {
            throw new ECodexConnectorControllerException("Error creating NonRetrieval for message!", e);
        }

        sendEvidenceToNationalSystem(originalMessage, nonRetrieval, ActionEnum.RetrievalNonRetrievalToRecipient);
    }

    private void sendEvidenceToNationalSystem(Message originalMessage, MessageConfirmation confirmation,
            ActionEnum evidenceAction) throws ECodexConnectorControllerException {

        originalMessage.addConfirmation(confirmation);
        persistenceService.persistEvidenceForMessageIntoDatabase(originalMessage, confirmation.getEvidence(),
                confirmation.getEvidenceType());

        MessageDetails details = new MessageDetails();
        details.setRefToMessageId(originalMessage.getMessageDetails().getNationalMessageId());
        details.setConversationId(originalMessage.getMessageDetails().getConversationId());
        details.setService(originalMessage.getMessageDetails().getService());
        details.setAction(evidenceAction);

        Message evidenceMessage = new Message(details, confirmation);

        try {
            nationalBackendClient.deliverLastEvidenceForMessage(evidenceMessage);
        } catch (ECodexConnectorNationalBackendClientException e) {
            throw new ECodexConnectorControllerException("Exception sending "
                    + confirmation.getEvidenceType().toString() + " evidence back to national system of message "
                    + originalMessage.getMessageDetails().getNationalMessageId(), e);
        } catch (ImplementationMissingException e) {
            throw new ECodexConnectorControllerException(e);
        }

        persistenceService.setEvidenceDeliveredToNationalSystem(originalMessage, confirmation.getEvidenceType());
        persistenceService.rejectMessage(originalMessage);
    }

    private boolean checkEvidencesForType(Message message, ECodexEvidenceType type) {
        if (message.getConfirmations() != null) {
            for (MessageConfirmation confirmation : message.getConfirmations()) {
                if (confirmation.getEvidenceType().equals(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setNationalBackendClient(ECodexConnectorNationalBackendClient nationalBackendClient) {
        this.nationalBackendClient = nationalBackendClient;
    }
}
