package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.process.util.CreateConfirmationMessageBuilderFactoryImpl;
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
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType.*;
import static eu.domibus.connector.tools.logging.LoggingMarker.BUSINESS_LOG;

/**
 * This service is responsible for
 *      -) in the future checking if the provided confirmation is valid
 *      -) storing the confirmation to the according business message into the database
 *      -) sending the confirmation in the other direction as the original message back as
 *      evidence message
 */
@Service
public class MessageConfirmationProcessor {

    private static final Logger LOGGER = LogManager.getLogger(MessageConfirmationProcessor.class);

    @Autowired
    DomibusConnectorEvidencePersistenceService evidencePersistenceService;

    @Autowired
    SubmitMessageToLinkModuleService submitMessageToLinkModuleService;

    @Autowired
    DomibusConnectorMessageIdGenerator messageIdGenerator;

    @Autowired
    CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactory;

    @Autowired
    DCMessagePersistenceService messagePersistenceService;

    public void processConfirmationMessageForMessage(DomibusConnectorMessage originalMessage, List<DomibusConnectorMessageId> transportId, DomibusConnectorMessageConfirmation confirmation) {
        if (!DomainModelHelper.isBusinessMessage(originalMessage)) {
            throw new IllegalArgumentException("message must be a business message!");
        }

        //TODO: write all transport ids for the evidence into the persistence layer
        evidencePersistenceService.persistEvidenceMessageToBusinessMessage(originalMessage, transportId.stream().findAny().orElse(null), confirmation);

        this.confirmRejectMessage(confirmation.getEvidenceType(), originalMessage);
    }

    public void processConfirmationForMessage(DomibusConnectorMessage message, DomibusConnectorMessageConfirmation confirmation) {
        if (!DomainModelHelper.isBusinessMessage(message)) {
            throw new IllegalArgumentException("message must be a business message!");
        }

        evidencePersistenceService.persistEvidenceMessageToBusinessMessage(message, message.getConnectorMessageId(), confirmation);

        this.confirmRejectMessage(confirmation.getEvidenceType(), message);

    }


    public void processConfirmationForMessageAndSendBack(DomibusConnectorMessage message, DomibusConnectorMessageConfirmation confirmation) {
        if (!DomainModelHelper.isBusinessMessage(message)) {
            throw new IllegalArgumentException("message must be a business message!");
        }

        evidencePersistenceService.persistEvidenceMessageToBusinessMessage(message, message.getConnectorMessageId(), confirmation);

        message.addTransportedMessageConfirmation(confirmation);
        this.sendConfirmationBack(message, confirmation);

        this.confirmRejectMessage(confirmation.getEvidenceType(), message);
    }

    @StoreMessageExceptionIntoDatabase //catches DomibusConnectorMessageException and stores it into db, ex will not be thrown again
    void sendConfirmationBack(DomibusConnectorMessage originalMessage, DomibusConnectorMessageConfirmation confirmation) {
        DomibusConnectorMessageId domibusConnectorMessageId = messageIdGenerator.generateDomibusConnectorMessageId();
        try (CloseableThreadContext.Instance ctx = CloseableThreadContext.put(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, domibusConnectorMessageId.getConnectorMessageId());
                CloseableThreadContext.Instance ctx2 = CloseableThreadContext.put(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_PROCESSOR_PROPERTY_NAME, "send_confirmation_back");
        ) {
            CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder confirmationMessageBuilder =
                    createConfirmationMessageBuilderFactory.createConfirmationMessageBuilderFromBusinessMessageAndConfirmation(originalMessage, confirmation);
            CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper evidenceMessageWrapper =
                    confirmationMessageBuilder
                            //send evidence as evidence message into other direction back...
//                            .revertMessageDirection()
                            .switchMessageDirection()
                            .switchFromToAttributes()
                            .build();

            DomibusConnectorMessage evidenceMessage = evidenceMessageWrapper.getEvidenceMessage();
            messagePersistenceService.persistMessageIntoDatabase(evidenceMessage);

            submitMessageToLinkModuleService.submitMessage(evidenceMessage);
            LOGGER.info("Successfully submitted evidence-message [{}] to link module", evidenceMessage);
        } catch (Exception e) {
            LOGGER.error("Exception occured while sending evidence back", e);
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(originalMessage)
                    .setCause(e)
                    .buildAndThrow();
        }
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
