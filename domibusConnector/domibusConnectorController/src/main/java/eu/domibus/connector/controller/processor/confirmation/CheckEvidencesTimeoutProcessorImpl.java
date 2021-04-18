package eu.domibus.connector.controller.processor.confirmation;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.processor.steps.MessageConfirmationStep;
import eu.domibus.connector.controller.processor.steps.SubmitConfirmationAsEvidenceMessageStep;
import eu.domibus.connector.controller.processor.util.ConfirmationCreatorService;
import eu.domibus.connector.controller.spring.EvidencesTimeoutConfigurationProperties;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class CheckEvidencesTimeoutProcessorImpl implements CheckEvidencesTimeoutProcessor {

    private static Logger LOGGER = LogManager.getLogger(CheckEvidencesTimeoutProcessorImpl.class);

    private final EvidencesTimeoutConfigurationProperties evidencesTimeoutConfigurationProperties;
    private final DCMessagePersistenceService persistenceService;
    private final ConfirmationCreatorService confirmationCreatorService;
    private final SubmitConfirmationAsEvidenceMessageStep submitConfirmationAsEvidenceMessageStep;
    private final MessageConfirmationStep messageConfirmationStep;

    public CheckEvidencesTimeoutProcessorImpl(EvidencesTimeoutConfigurationProperties evidencesTimeoutConfigurationProperties,
                                              DCMessagePersistenceService persistenceService,
                                              ConfirmationCreatorService confirmationCreatorService,
                                              MessageConfirmationStep messageConfirmationStep,
                                              SubmitConfirmationAsEvidenceMessageStep submitConfirmationAsEvidenceMessageStep) {
        this.evidencesTimeoutConfigurationProperties = evidencesTimeoutConfigurationProperties;
        this.persistenceService = persistenceService;
        this.confirmationCreatorService = confirmationCreatorService;
        this.submitConfirmationAsEvidenceMessageStep = submitConfirmationAsEvidenceMessageStep;
        this.messageConfirmationStep = messageConfirmationStep;
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
        LOGGER.trace("Checking [{}] messages for not rejected nor confirmed withoutRelayREMMD", messages.size());
        messages.forEach(this::checkNotRejectedNorConfirmedWithoutRelayREMMD);
    }

    void checkNotRejectedNorConfirmedWithoutRelayREMMD(DomibusConnectorMessage message) {
        String messageId = message.getConnectorMessageId().toString();
        try (org.slf4j.MDC.MDCCloseable mdcCloseable = org.slf4j.MDC.putCloseable(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, messageId)) {
            Duration relayREMMDTimeout = evidencesTimeoutConfigurationProperties.getRelayREMMDTimeout().getDuration();
            Duration relayREMMDWarnTimeout = evidencesTimeoutConfigurationProperties.getRelayREMMDWarnTimeout().getDuration();

            //if it is later then the evaluated timeout given
            if (Duration.between(getDeliveryTime(message), Instant.now()).compareTo(relayREMMDTimeout) > 0) {
                try {
                    createRelayRemmdFailureAndSendIt(message);
                    LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "Message [{}] reached relayREMMD timeout. A RelayREMMDFailure evidence has been generated and sent.", message.getConnectorMessageIdAsString());
                } catch (DomibusConnectorMessageException e) {
                    //throw new DomibusConnectorControllerException(e);
                    LOGGER.error("Exception occured while checking relayREMMDTimeout", e);
                }
                return;
            }
            if (Duration.between(getDeliveryTime(message), Instant.now()).compareTo(relayREMMDWarnTimeout) > 0) {
                LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "Message [{}] reached warning limit for relayREMMD confirmation timeout. No RelayREMMD evidence for this message has been received yet!",
                        message.getConnectorMessageId());
            }
        }
    }

    void checkNotRejectedWithoutDelivery() throws DomibusConnectorControllerException {
        //Request database to get all messages not rejected yet and without a DELIVERY/NON_DELIVERY evidence
        List<DomibusConnectorMessage> messages = persistenceService.findOutgoingMessagesNotRejectedAndWithoutDelivery();
        LOGGER.trace("Checking [{}] messages for confirmation timeout notRejectedWithoutDelivery", messages.size());
        messages.forEach(this::checkNotRejectedWithoutDelivery);
    }

    void checkNotRejectedWithoutDelivery(DomibusConnectorMessage message) {
        String messageId = message.getConnectorMessageId().toString();
        try (org.slf4j.MDC.MDCCloseable mdcCloseable = org.slf4j.MDC.putCloseable(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, messageId)) {
            LOGGER.trace("checkNotRejectedWithoutDelivery# checking message: [{}]");
            Duration deliveryTimeout = evidencesTimeoutConfigurationProperties.getDeliveryTimeout().getDuration();
            Duration deliveryWarnTimeout = evidencesTimeoutConfigurationProperties.getDeliveryWarnTimeout().getDuration();

            //if it is later then the evaluated timeout given
            if (Duration.between(getDeliveryTime(message), Instant.now()).compareTo(deliveryTimeout) > 0) {
                try {
                    createNonDeliveryAndSendIt(message);
                    LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "Message [{}] reached Delivery confirmation timeout. A NonDelivery evidence has been generated and sent.", message.getConnectorMessageIdAsString());
                } catch (DomibusConnectorMessageException e) {
                    throw new DomibusConnectorControllerException(e);
                }
                return;
            }
            if (Duration.between(getDeliveryTime(message), Instant.now()).compareTo(deliveryWarnTimeout) > 0) {
                LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "Message [{}] reached warning limit for delivery confirmation timeout. No Delivery evidence for this message has been received yet!",
                        message.getConnectorMessageId());
            }
        }
    }

    private Instant getDeliveryTime(DomibusConnectorMessage message) {
        DomibusConnectorMessageDetails details = message.getMessageDetails();
        Date deliveryDate;
        switch (details.getDirection()) {
            case GATEWAY_TO_BACKEND:
                deliveryDate = details.getDeliveredToBackend();
                break;
            case BACKEND_TO_GATEWAY:
                deliveryDate = details.getDeliveredToGateway();
                break;
            default:
                throw new IllegalStateException("Unknown message direction, cannot process any timeouts!");

        }
        return deliveryDate.toInstant();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_MESSAGE_PROCESSOR_PROPERTY_NAME, value = "CreateRelayRemmdFailureAndSendIt")
    public void createRelayRemmdFailureAndSendIt(DomibusConnectorMessage originalMessage) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {

        LOGGER.error(LoggingMarker.Log4jMarker.BUSINESS_LOG, "The RelayREMMDAcceptance/Rejection evidence timeout for originalMessage {} timed out. Sending RELAY_REMMD_FAILURE to backend!", originalMessage
                .getMessageDetails().getEbmsMessageId());

        DomibusConnectorMessageConfirmation nonDelivery = confirmationCreatorService.createRelayRemmdFailure(originalMessage, DomibusConnectorRejectionReason.RELAY_REMMD_TIMEOUT);
        processConfirmationForMessage(originalMessage, nonDelivery);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_MESSAGE_PROCESSOR_PROPERTY_NAME, value = "CreateNonDeliveryAndSendIt")
    public void createNonDeliveryAndSendIt(DomibusConnectorMessage originalMessage) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {

        LOGGER.error(LoggingMarker.Log4jMarker.BUSINESS_LOG, "The Delivery/NonDelivery evidence timeout for originalMessage {} timed out. Sending NonDelivery to backend!", originalMessage
            .getMessageDetails().getEbmsMessageId());

        DomibusConnectorMessageConfirmation nonDelivery = confirmationCreatorService.createNonDelivery(originalMessage, DomibusConnectorRejectionReason.DELIVERY_EVIDENCE_TIMEOUT);
        processConfirmationForMessage(originalMessage, nonDelivery);
    }

    private void processConfirmationForMessage(DomibusConnectorMessage originalMessage, DomibusConnectorMessageConfirmation confirmation) {
        //process confirmation for business message (store confirmation, reject business message)
        messageConfirmationStep.processConfirmationForMessage(originalMessage, confirmation);
        //send confirmation to backend
        submitConfirmationAsEvidenceMessageStep.submitOppositeDirection(null, originalMessage, confirmation);
    }

}
