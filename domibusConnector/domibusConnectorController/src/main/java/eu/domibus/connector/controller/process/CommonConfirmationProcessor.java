package eu.domibus.connector.controller.process;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.model.enums.EvidenceType;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Comparator;

import static eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType.*;
import static eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType.RELAY_REMMD_FAILURE;
import static eu.domibus.connector.tools.logging.LoggingMarker.BUSINESS_LOG;

public class CommonConfirmationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConfirmationProcessor.class);

    private final DCMessagePersistenceService messagePersistenceService;

    public CommonConfirmationProcessor(DCMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
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
            LOGGER.warn(BUSINESS_LOG, "Message [{}] has been rejected by evidence [{}]", originalMessage, evidenceType);
            messagePersistenceService.rejectMessage(originalMessage);
        }
        if (DELIVERY == evidenceType || RETRIEVAL == evidenceType) { //TODO: make a configuration switch to configure which evidence is sufficient to set mesg. into confirmed state!
            if (messagePersistenceService.checkMessageRejected(originalMessage)) {
                LOGGER.warn(BUSINESS_LOG, "Message [{}] has already been rejected by an negative evidence!\nThe positive evidence of type [{}] will be ignored!", originalMessage, evidenceType);
            } else {
                messagePersistenceService.confirmMessage(originalMessage);
                LOGGER.info(BUSINESS_LOG, "Message [{}] has been confirmed by evidence [{}]", originalMessage, evidenceType);
            }
        }
    }


}


