package eu.domibus.connector.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.common.CommonConnectorProperties;
import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.common.enums.EvidenceType;
import eu.domibus.connector.common.exception.DomibusConnectorMessageException;
import eu.domibus.connector.common.exception.ImplementationMissingException;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageConfirmation;
import eu.domibus.connector.common.message.MessageDetails;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.evidences.type.RejectionReason;
import eu.domibus.connector.nbc.DomibusConnectorNationalBackendClient;
import eu.domibus.connector.nbc.exception.DomibusConnectorNationalBackendClientException;

public class DomibusConnectorCheckEvidencesTimeoutController implements DomibusConnectorController {

    static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorCheckEvidencesTimeoutController.class);

    private DomibusConnectorNationalBackendClient nationalBackendClient;
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

            List<Message> unconfirmedOutgoing = persistenceService.findOutgoingUnconfirmedMessages();
            if (unconfirmedOutgoing != null && !unconfirmedOutgoing.isEmpty()) {
                for (Message unconfirmed : unconfirmedOutgoing) {
                    Date delivered = unconfirmed.getDbMessage().getDeliveredToGateway();
                    Date now = new Date();
                    long relayRemmdTimout = delivered.getTime() + connectorProperties.getTimeoutRelayREMMD();
                    long deliveryTimeout = delivered.getTime() + connectorProperties.getTimeoutDelivery();
                    long retrievalTimeout = delivered.getTime() + connectorProperties.getTimeoutRetrieval();

                    if (connectorProperties.getTimeoutRelayREMMD() > 0 && now.getTime() > relayRemmdTimout) {

                        boolean relayRemmdFound = checkEvidencesForRelayRemmd(unconfirmed);
                        if (!relayRemmdFound) {
                            try {
                                createRelayRemmdFailureAndSendIt(unconfirmed);
                            } catch (DomibusConnectorMessageException e) {
                                throw new DomibusConnectorControllerException(e);
                            }
                            continue;
                        }
                    }

                    if (connectorProperties.getTimeoutDelivery() > 0 && isStillUnconfirmed(unconfirmed)
                            && now.getTime() > deliveryTimeout) {
                        boolean deliveryFound = checkEvidencesForDelivery(unconfirmed);
                        if (!deliveryFound) {
                            try {
                                createNonDeliveryAndSendIt(unconfirmed);
                            } catch (DomibusConnectorMessageException e) {
                                throw new DomibusConnectorControllerException(e);
                            }
                            continue;
                        }
                    }

                    if (connectorProperties.getTimeoutRetrieval() > 0 && isStillUnconfirmed(unconfirmed)
                            && now.getTime() > retrievalTimeout) {
                        boolean retrievalFound = checkEvidencesForRetrieval(unconfirmed);
                        if (!retrievalFound)
                            try {
                                createNonRetrievalAndSendIt(unconfirmed);
                            } catch (DomibusConnectorMessageException e) {
                                throw new DomibusConnectorControllerException(e);
                            }
                        continue;
                    }

                }
            }

            LOGGER.debug("Job for checking evidence timeouts finished in {} ms.",
                    (System.currentTimeMillis() - start.getTime()));
        } else {
            LOGGER.debug("Property connector.use.evidences.timeout set to false.");
        }
    }

    private boolean isStillUnconfirmed(Message message) {
        return message.getDbMessage().getConfirmed() == null && message.getDbMessage().getRejected() == null;
    }

    private boolean checkEvidencesForRelayRemmd(Message message) {
        if (checkEvidencesForType(message, EvidenceType.RELAY_REMMD_ACCEPTANCE)) {
            return true;
        } else if (checkEvidencesForType(message, EvidenceType.RELAY_REMMD_REJECTION)) {
            persistenceService.rejectMessage(message);
            return true;
        }
        return false;
    }

    private boolean checkEvidencesForDelivery(Message message) {
        if (checkEvidencesForType(message, EvidenceType.DELIVERY)) {
            return true;
        } else if (checkEvidencesForType(message, EvidenceType.NON_DELIVERY)) {
            persistenceService.rejectMessage(message);
            return true;
        }
        return false;
    }

    private boolean checkEvidencesForRetrieval(Message message) {
        if (checkEvidencesForType(message, EvidenceType.RETRIEVAL)) {
            persistenceService.confirmMessage(message);
            return true;
        } else if (checkEvidencesForType(message, EvidenceType.NON_RETRIEVAL)) {
            persistenceService.rejectMessage(message);
            return true;
        }
        return false;
    }

    private void createRelayRemmdFailureAndSendIt(Message originalMessage) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {

        LOGGER.info("The RelayREMMDAcceptance evidence for message {} timed out. Will send failure!", originalMessage
                .getMessageDetails().getEbmsMessageId());

        MessageConfirmation relayRemMDFailure = null;

        try {
            relayRemMDFailure = evidencesToolkit.createRelayREMMDFailure(RejectionReason.OTHER, originalMessage);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            throw new DomibusConnectorMessageException(originalMessage,
                    "Error creating RelayREMMDFailure for message!", e, this.getClass());
        }

        DomibusConnectorAction action = persistenceService.getRelayREMMDFailure();

        sendEvidenceToNationalSystem(originalMessage, relayRemMDFailure, action);
    }

    private void createNonDeliveryAndSendIt(Message originalMessage) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {
        LOGGER.info("The Delivery evidence for message {} timed out. Will send NonDelivery!", originalMessage
                .getMessageDetails().getEbmsMessageId());
        MessageConfirmation nonDelivery = null;
        try {
            nonDelivery = evidencesToolkit.createNonDeliveryEvidence(RejectionReason.OTHER, originalMessage);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            throw new DomibusConnectorMessageException(originalMessage, "Error creating NonDelivery for message!", e,
                    this.getClass());
        }

        DomibusConnectorAction action = persistenceService.getDeliveryNonDeliveryToRecipientAction();

        sendEvidenceToNationalSystem(originalMessage, nonDelivery, action);
    }

    private void createNonRetrievalAndSendIt(Message originalMessage) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {
        LOGGER.info("The Retrieval evidence for message {} timed out. Will send NonRetrieval!", originalMessage
                .getMessageDetails().getEbmsMessageId());
        MessageConfirmation nonRetrieval = null;
        try {
            nonRetrieval = evidencesToolkit.createNonRetrievalEvidence(RejectionReason.OTHER, originalMessage);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            throw new DomibusConnectorMessageException(originalMessage, "Error creating NonRetrieval for message!", e,
                    this.getClass());
        }

        DomibusConnectorAction action = persistenceService.getRetrievalNonRetrievalToRecipientAction();

        sendEvidenceToNationalSystem(originalMessage, nonRetrieval, action);
    }

    private void sendEvidenceToNationalSystem(Message originalMessage, MessageConfirmation confirmation,
            DomibusConnectorAction evidenceAction) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {

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
        } catch (DomibusConnectorNationalBackendClientException e) {
            throw new DomibusConnectorMessageException(originalMessage, "Exception sending "
                    + confirmation.getEvidenceType().toString() + " evidence back to national system for message "
                    + originalMessage.getMessageDetails().getNationalMessageId(), e, this.getClass());
        } catch (ImplementationMissingException e) {
            throw new DomibusConnectorControllerException(e);
        }

        persistenceService.setEvidenceDeliveredToNationalSystem(originalMessage, confirmation.getEvidenceType());
        persistenceService.rejectMessage(originalMessage);
    }

    private boolean checkEvidencesForType(Message message, EvidenceType type) {
        if (message.getConfirmations() != null) {
            for (MessageConfirmation confirmation : message.getConfirmations()) {
                if (confirmation.getEvidenceType().equals(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setNationalBackendClient(DomibusConnectorNationalBackendClient nationalBackendClient) {
        this.nationalBackendClient = nationalBackendClient;
    }
}
