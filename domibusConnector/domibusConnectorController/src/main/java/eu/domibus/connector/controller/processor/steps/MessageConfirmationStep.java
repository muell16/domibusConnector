package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.processor.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageId;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.persistence.model.enums.EvidenceType;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Comparator;

import static eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType.*;

/**
 * This service is responsible for
 *      -) in the future checking if the provided confirmation is valid
 *      -) storing the confirmation to the according business message into the database
 *      -) sending the confirmation in the other direction as the original message back as
 *      evidence message
 */
@Service
@RequiredArgsConstructor
public class MessageConfirmationStep {

    private static final Logger LOGGER = LogManager.getLogger(MessageConfirmationStep.class);

    private final DomibusConnectorEvidencePersistenceService evidencePersistenceService;
    private final DCMessagePersistenceService messagePersistenceService;


    public void processTransportedConfirmations(DomibusConnectorMessage message) {
        if (!DomainModelHelper.isBusinessMessage(message)) {
            throw new IllegalArgumentException("message must be a business message!");
        }
        for (DomibusConnectorMessageConfirmation c : message.getTransportedMessageConfirmations()) {
            processConfirmationForMessage(message, c);
        }
    }


    public void processConfirmationForMessage(DomibusConnectorMessage message, DomibusConnectorMessageConfirmation confirmation) {
        if (!DomainModelHelper.isBusinessMessage(message)) {
            throw new IllegalArgumentException("message must be a business message!");
        }

        LOGGER.debug("Adding confirmation of type [{}] to business message [{}]",  confirmation.getEvidenceType(), message.getConnectorMessageId());
        evidencePersistenceService.persistEvidenceMessageToBusinessMessage(message, message.getConnectorMessageId(), confirmation);
        message.getRelatedMessageConfirmations().add(confirmation);

        this.confirmRejectMessage(confirmation.getEvidenceType(), message);

    }

    /**
     * Sets the correct message state within the database according to the following rules:
     *      a already rejected message cannot become a confirmed message!
     *      all evidences of lower priority are ignored
     *      (this means a RELAY_REMMD_REJECTION cannot overwrite a already processed DELIVERY evidence)
     *      also see {@link EvidenceType#getPriority()}
     * @param evidenceType - the evidence Type
     * @param originalMessage  - the original Message
     */
    public void confirmRejectMessage(DomibusConnectorEvidenceType evidenceType, DomibusConnectorMessage originalMessage) {
        Integer highestEvidencePriority = originalMessage.getRelatedMessageConfirmations()
                .stream()
                .map(e -> e.getEvidenceType().getPriority())
                .max(Comparator.naturalOrder())
                .orElse(0);

        if (evidenceType.getPriority() < highestEvidencePriority) {
            LOGGER.info("Evidence of type [{}] will not influence the rejected or confirmed state of message [{}]\n because the evidence has lower priority then the already received evidences", evidenceType, originalMessage);
            return;
        }

        if (SUBMISSION_REJECTION == evidenceType || NON_DELIVERY == evidenceType || NON_RETRIEVAL == evidenceType || RELAY_REMMD_REJECTION == evidenceType || RELAY_REMMD_FAILURE == evidenceType) {
            LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "Message [{}] has been rejected by evidence [{}]", originalMessage, evidenceType);
            messagePersistenceService.rejectMessage(originalMessage);
        }
        if (DELIVERY == evidenceType || RETRIEVAL == evidenceType) { //TODO: make a configuration switch to configure which evidence is sufficient to set mesg. into confirmed state!
            if (messagePersistenceService.checkMessageRejected(originalMessage)) {
                LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, "Message [{}] has already been rejected by an negative evidence!\nThe positive evidence of type [{}] will be ignored!", originalMessage, evidenceType);
            } else {
                messagePersistenceService.confirmMessage(originalMessage);
                LOGGER.info(LoggingMarker.Log4jMarker.BUSINESS_LOG, "Message [{}] has been confirmed by evidence [{}]", originalMessage, evidenceType);
            }
        }
    }


}
