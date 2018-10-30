package eu.domibus.connector.controller.processor.confirmation;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import eu.domibus.connector.controller.spring.EvidencesTimeoutConfigurationProperties;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.lib.spring.DomibusConnectorDuration;
import eu.domibus.connector.persistence.service.*;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import org.springframework.stereotype.Service;

import static eu.domibus.connector.tools.logging.LoggingMarker.BUSINESS_LOG;

@Service
public class CheckEvidencesTimeoutProcessorImpl implements CheckEvidencesTimeoutProcessor {

    static Logger LOGGER = LoggerFactory.getLogger(CheckEvidencesTimeoutProcessorImpl.class);

    private EvidencesTimeoutConfigurationProperties evidencesTimeoutConfigurationProperties;
    private DomibusConnectorMessagePersistenceService persistenceService;
    private DomibusConnectorActionPersistenceService actionPersistenceService;
    private DomibusConnectorEvidencePersistenceService evidencePersistenceService;
    private DomibusConnectorEvidencesToolkit evidencesToolkit;
    private DomibusConnectorBackendDeliveryService backendDeliveryService;

    //setter
    @Autowired
    public void setEvidencesTimeoutConfigurationProperties(EvidencesTimeoutConfigurationProperties evidencesTimeoutConfigurationProperties) {
        this.evidencesTimeoutConfigurationProperties = evidencesTimeoutConfigurationProperties;
    }

    @Autowired
    public void setPersistenceService(DomibusConnectorMessagePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Autowired
    public void setActionPersistenceService(DomibusConnectorActionPersistenceService actionPersistenceService) {
        this.actionPersistenceService = actionPersistenceService;
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
    public void setBackendDeliveryService(DomibusConnectorBackendDeliveryService backendDeliveryService) {
        this.backendDeliveryService = backendDeliveryService;
    }

    @Override
    @Scheduled(fixedDelayString = "#{evidencesTimeoutConfigurationProperties.checkTimeout.milliseconds}")
    public void checkEvidencesTimeout() throws DomibusConnectorControllerException {
        LOGGER.info("Job for checking evidence timeouts triggered.");
        Date start = new Date();

        //TODO: combine the timeouts into ONE database access, to reduce database hits...

        // only check for timeout of RELAY_REMMD_ACCEPTANCE/REJECTION evidences if the timeout is set in the connector.properties
        if (evidencesTimeoutConfigurationProperties.getRelayREMMDTimeout().getMilliseconds() > 0 ||
                evidencesTimeoutConfigurationProperties.getRelayREMMDWarnTimeout().getMilliseconds() > 0 )
            checkNotRejectedNorConfirmedWithoutRelayREMMD();

        // only check for timeout of DELIVERY/NON_DELIVERY evidences if the timeout is set in the connector.properties
        if (evidencesTimeoutConfigurationProperties.getDeliveryTimeout().getMilliseconds() > 0 ||
                evidencesTimeoutConfigurationProperties.getDeliveryWarnTimeout().getMilliseconds() > 0)
            checkNotRejectedWithoutDelivery();

        LOGGER.debug("Job for checking evidence timeouts finished in {} ms.",
                (System.currentTimeMillis() - start.getTime()));

    }


    void checkNotRejectedNorConfirmedWithoutRelayREMMD() throws DomibusConnectorControllerException {
        //Request database to get all messages not rejected and not confirmed yet and without a RELAY_REMMD_ACCEPTANCE/REJECTION evidence
        List<DomibusConnectorMessage> messages = persistenceService.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
        LOGGER.trace("Checking [{}] messages for confirmed withoutRelayREMMD", messages.size());
        messages.forEach(this::checkNotRejectedNorConfirmedWithoutRelayREMMD);
    }

    void checkNotRejectedNorConfirmedWithoutRelayREMMD(DomibusConnectorMessage message) {
        Duration relayREMMDTimeout = evidencesTimeoutConfigurationProperties.getRelayREMMDTimeout().getDuration();
        Duration relayREMMDWarnTimeout = evidencesTimeoutConfigurationProperties.getRelayREMMDWarnTimeout().getDuration();

        //if it is later then the evaluated timeout given
        if (Duration.between(getDeliveryTime(message), Instant.now()).compareTo(relayREMMDTimeout) > 0) {
            try {
                createRelayRemmdFailureAndSendIt(message);
                LOGGER.warn(BUSINESS_LOG, "Message [{}] reached relayREMMD timeout. A RelayREMMDFailure evidence has been generated and sent.", message.getConnectorMessageId());
            } catch (DomibusConnectorMessageException e) {
                //throw new DomibusConnectorControllerException(e);
                LOGGER.error("Exception occured while checking relayREMMDTimeout", e);
            }
            return;
        }
        if (Duration.between(getDeliveryTime(message), Instant.now()).compareTo(relayREMMDWarnTimeout) > 0) {
            LOGGER.warn(BUSINESS_LOG, "Message [{}] reached warning limit for relayREMMD confirmation timeout. No RelayREMMD evidence for this message has been received yet!",
                    message.getConnectorMessageId());
        }
    }


    void checkNotRejectedWithoutDelivery() throws DomibusConnectorControllerException {
        //Request database to get all messages not rejected yet and without a DELIVERY/NON_DELIVERY evidence
        List<DomibusConnectorMessage> messages = persistenceService.findOutgoingMessagesNotRejectedAndWithoutDelivery();
        LOGGER.trace("Checking [{}] messages for confirmation timeout notRejectedWithoutDelivery", messages.size());
        messages.forEach(this::checkNotRejectedWithoutDelivery);
    }

    void checkNotRejectedWithoutDelivery(DomibusConnectorMessage message) {
        Duration deliveryTimeout = evidencesTimeoutConfigurationProperties.getDeliveryTimeout().getDuration();
        Duration deliveryWarnTimeout = evidencesTimeoutConfigurationProperties.getDeliveryWarnTimeout().getDuration();


//        Date delivered = new Date();
//        long deliveryTimeoutT = delivered.getTime() + evidencesTimeoutConfigurationProperties.getDeliveryTimeout().getMilliseconds();
//        Date now = new Date();
        //if it is later then the evaluated timeout given
        if (Duration.between(getDeliveryTime(message), Instant.now()).compareTo(deliveryTimeout) > 0) {
            try {
                createNonDeliveryAndSendIt(message);
                LOGGER.warn(BUSINESS_LOG, "Message [{}] reached Delivery confirmation timeout. A NonDelivery evidence has been generated and sent.", message.getConnectorMessageId());
            } catch (DomibusConnectorMessageException e) {
                throw new DomibusConnectorControllerException(e);
            }
            return;
        }
        if (Duration.between(getDeliveryTime(message), Instant.now()).compareTo(deliveryWarnTimeout) > 0) {
            LOGGER.warn(BUSINESS_LOG, "Message [{}] reached warning limit for delivery confirmation timeout. No Delivery evidence for this message has been received yet!",
                    message.getConnectorMessageId());
        }
    }

    private Instant getDeliveryTime(DomibusConnectorMessage message) {
        DomibusConnectorMessageDetails details = message.getMessageDetails();
        Date deliveryDate;
        switch (details.getDirection()) {
            case GATEWAY_TO_BACKEND:
            case CONNECTOR_TO_BACKEND:
                deliveryDate = details.getDeliveredToBackend();
                break;
            case BACKEND_TO_GATEWAY:
            case CONNECTOR_TO_GATEWAY:
                deliveryDate = details.getDeliveredToGateway();
                break;
            default:
                throw new IllegalStateException("Unknown message direction, cannot process any timeouts!");

        }
        return deliveryDate.toInstant();
    }

    void createRelayRemmdFailureAndSendIt(DomibusConnectorMessage originalMessage) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {

        LOGGER.info("The RelayREMMDAcceptance/Rejection evidence for originalMessage {} timed out. Sending RELAY_REMMD_FAILURE to backend!", originalMessage
                .getMessageDetails().getEbmsMessageId());

        DomibusConnectorMessageConfirmation relayRemMDFailure = null;

        try {
            relayRemMDFailure = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.RELAY_REMMD_FAILURE, originalMessage, DomibusConnectorRejectionReason.OTHER, null);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(originalMessage)
                    .setText("Error creating RelayREMMDFailure for originalMessage!")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
        }

        DomibusConnectorAction action = actionPersistenceService.getRelayREMMDFailure();

        sendEvidenceToNationalSystem(originalMessage, relayRemMDFailure, action);
    }

    private void createNonDeliveryAndSendIt(DomibusConnectorMessage originalMessage) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {

        LOGGER.info("The Delivery/NonDelivery evidence for originalMessage {} timed out. Sending NonDelivery to backend!", originalMessage
                .getMessageDetails().getEbmsMessageId());
        DomibusConnectorMessageConfirmation nonDelivery = null;
        try {
            nonDelivery = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.NON_DELIVERY, originalMessage, DomibusConnectorRejectionReason.OTHER, null);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(originalMessage)
                    .setText("Error creating NonDelivery for originalMessage!")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
        }

        DomibusConnectorAction action = actionPersistenceService.getDeliveryNonDeliveryToRecipientAction();

        sendEvidenceToNationalSystem(originalMessage, nonDelivery, action);
    }


    private void sendEvidenceToNationalSystem(DomibusConnectorMessage originalMessage, DomibusConnectorMessageConfirmation confirmation,
                                              DomibusConnectorAction evidenceAction) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {

        try {

            originalMessage.addConfirmation(confirmation);
            evidencePersistenceService.persistEvidenceForMessageIntoDatabase(originalMessage, confirmation.getEvidence(),
                    confirmation.getEvidenceType());

            DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
            details.setRefToMessageId(originalMessage.getMessageDetails().getBackendMessageId());
            details.setConversationId(originalMessage.getMessageDetails().getConversationId());
            details.setService(originalMessage.getMessageDetails().getService());
            details.setAction(evidenceAction);

            DomibusConnectorMessage evidenceMessage = new DomibusConnectorMessage(details, confirmation);

            backendDeliveryService.deliverMessageToBackend(evidenceMessage);

            evidencePersistenceService.setEvidenceDeliveredToNationalSystem(originalMessage, confirmation.getEvidenceType());
            persistenceService.rejectMessage(originalMessage);
        } catch (PersistenceException ex) {
            LOGGER.error("PersistenceException occured", ex);
            throw new RuntimeException(ex);
            //TODO: handle exception
        }
    }

}
