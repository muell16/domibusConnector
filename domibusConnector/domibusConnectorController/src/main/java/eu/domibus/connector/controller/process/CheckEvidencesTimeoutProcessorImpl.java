package eu.domibus.connector.controller.process;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import eu.domibus.connector.controller.spring.EvidencesTimeoutConfigurationProperties;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.DomibusConnectorServiceBuilder;
import eu.domibus.connector.persistence.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;

@Component
public class CheckEvidencesTimeoutProcessorImpl implements CheckEvidencesTimeoutProcessor {

    static Logger LOGGER = LoggerFactory.getLogger(CheckEvidencesTimeoutProcessorImpl.class);

    @Autowired
    EvidencesTimeoutConfigurationProperties evidencesTimeoutConfigurationProperties;

    @Autowired
    private DomibusConnectorMessagePersistenceService persistenceService;

    @Autowired
    private DomibusConnectorActionPersistenceService actionPersistenceService;

    @Autowired
    private DomibusConnectorEvidencePersistenceService evidencePersistenceService;

    @Autowired
    private DomibusConnectorEvidencesToolkit evidencesToolkit;

    @Autowired
    private DomibusConnectorBackendDeliveryService backendDeliveryService;

    @Override
    @Scheduled(fixedDelayString = "#{evidencesTimeoutConfigurationProperties.checkTimeout.milliseconds}")
    public void checkEvidencesTimeout() throws DomibusConnectorControllerException {
        LOGGER.debug("Job for checking evidence timeouts triggered.");
        Date start = new Date();

        // only check for timeout of RELAY_REMMD_ACCEPTANCE/REJECTION evidences if the timeout is set in the connector.properties
        if (evidencesTimeoutConfigurationProperties.getRelayREMMDTimeout().getMilliseconds() > 0)
            checkNotRejectedNorConfirmedWithoutRelayREMMD();

        // only check for timeout of DELIVERY/NON_DELIVERY evidences if the timeout is set in the connector.properties
        if (evidencesTimeoutConfigurationProperties.getDeliveryTimeout().getMilliseconds() > 0)
            checkNotRejectedWithoutDelivery();

        LOGGER.debug("Job for checking evidence timeouts finished in {} ms.",
                (System.currentTimeMillis() - start.getTime()));

    }

    private void checkNotRejectedNorConfirmedWithoutRelayREMMD() throws DomibusConnectorControllerException {
        //Request database to get all messages not rejected and not confirmed yet and without a RELAY_REMMD_ACCEPTANCE/REJECTION evidence
        List<DomibusConnectorMessage> messages = persistenceService.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
        Date now = new Date();
        if (messages != null && !messages.isEmpty()) {
            for (DomibusConnectorMessage message : messages) {
                //Evaluate time in ms the reception of a RELAY_REMMD_ACCEPTANCE/REJECTION for the originalMessage times out
                //Date delivered = originalMessage.getDbMessage().getDeliveredToGateway(); //TODO:
                Date delivered = new Date();
                long relayRemmdTimout = delivered.getTime() + evidencesTimeoutConfigurationProperties.getRelayREMMDTimeout().getMilliseconds();

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
                //Evaluate time in ms the reception of a DELIVERY/NON_DELIVERY for the originalMessage times out
                //Date delivered = originalMessage.getDbMessage().getDeliveredToGateway(); //TODO
                Date delivered = new Date();
                long deliveryTimeoutT = delivered.getTime() + evidencesTimeoutConfigurationProperties.getDeliveryTimeout().getMilliseconds();

                //if it is later then the evaluated timeout given
                if (now.getTime() > deliveryTimeoutT) {
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
