package eu.domibus.connector.controller.processor.confirmation;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.processor.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.controller.spring.EvidencesTimeoutConfigurationProperties;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.tools.logging.LoggingMarker;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckEvidencesTimeoutProcessorImpl implements CheckEvidencesTimeoutProcessor {

//    static Logger LOGGER = LoggerFactory.getLogger(CheckEvidencesTimeoutProcessorImpl.class);
    private static Logger LOGGER = LogManager.getLogger(CheckEvidencesTimeoutProcessorImpl.class);

    private final EvidencesTimeoutConfigurationProperties evidencesTimeoutConfigurationProperties;
    private final DCMessagePersistenceService persistenceService;
//    private final DomibusConnectorBackendDeliveryService backendDeliveryService;
    private final CreateConfirmationMessageBuilderFactoryImpl confirmationMessageBuilderFactory;



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


    void checkNotRejectedWithoutDelivery() throws DomibusConnectorControllerException {
        //Request database to get all messages not rejected yet and without a DELIVERY/NON_DELIVERY evidence
        List<DomibusConnectorMessage> messages = persistenceService.findOutgoingMessagesNotRejectedAndWithoutDelivery();
        LOGGER.trace("Checking [{}] messages for confirmation timeout notRejectedWithoutDelivery", messages.size());
        messages.forEach(this::checkNotRejectedWithoutDelivery);
    }

    void checkNotRejectedWithoutDelivery(DomibusConnectorMessage message) {
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
    void createRelayRemmdFailureAndSendIt(DomibusConnectorMessage originalMessage) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {

        LOGGER.error(LoggingMarker.Log4jMarker.BUSINESS_LOG, "The RelayREMMDAcceptance/Rejection evidence for originalMessage {} timed out. Sending RELAY_REMMD_FAILURE to backend!", originalMessage
                .getMessageDetails().getEbmsMessageId());


        CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder confirmationMessageBuilder =
                confirmationMessageBuilderFactory.createConfirmationMessageBuilderFromBusinessMessage(originalMessage, DomibusConnectorEvidenceType.RELAY_REMMD_FAILURE);
        createPersistAndSendConfirmation(originalMessage, confirmationMessageBuilder);

    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void createNonDeliveryAndSendIt(DomibusConnectorMessage originalMessage) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {

        LOGGER.error(LoggingMarker.Log4jMarker.BUSINESS_LOG, "The Delivery/NonDelivery evidence for originalMessage {} timed out. Sending NonDelivery to backend!", originalMessage
            .getMessageDetails().getEbmsMessageId());


        CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder confirmationMessageBuilder = confirmationMessageBuilderFactory.createConfirmationMessageBuilderFromBusinessMessage(originalMessage, DomibusConnectorEvidenceType.NON_DELIVERY);
        createPersistAndSendConfirmation(originalMessage, confirmationMessageBuilder);

    }

    private void createPersistAndSendConfirmation(DomibusConnectorMessage originalMessage, CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder confirmationMessageBuilder) {
        confirmationMessageBuilder.setRejectionReason(DomibusConnectorRejectionReason.OTHER);
        confirmationMessageBuilder.withDirection(MessageTargetSource.BACKEND);

        CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper wrappedConfirmationMessage = confirmationMessageBuilder.build();

//        wrappedConfirmationMessage.persistEvidenceMessageAndPersistEvidenceToBusinessMessage();
        DomibusConnectorMessage evidenceMessage = wrappedConfirmationMessage.getEvidenceMessage();
        persistenceService.rejectMessage(originalMessage);
//        backendDeliveryService.deliverMessageToBackend(evidenceMessage);
    }


}
