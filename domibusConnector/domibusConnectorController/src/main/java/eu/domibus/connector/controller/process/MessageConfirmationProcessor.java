package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.process.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageId;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void processConfirmationMessageForMessage(DomibusConnectorMessage originalMessage, DomibusConnectorMessageConfirmation confirmation) {
        if (!DomainModelHelper.isBusinessMessage(originalMessage)) {
            throw new IllegalArgumentException("message must be a business message!");
        }

        evidencePersistenceService.persistEvidenceMessageToBusinessMessage(originalMessage, originalMessage.getConnectorMessageId(), confirmation);

        CommonConfirmationProcessor commonConfirmationProcessor = new CommonConfirmationProcessor(messagePersistenceService);
        commonConfirmationProcessor.confirmRejectMessage(confirmation.getEvidenceType(), originalMessage);
    }

    public void processConfirmationForMessage(DomibusConnectorMessage message, DomibusConnectorMessageConfirmation confirmation) {
        if (!DomainModelHelper.isBusinessMessage(message)) {
            throw new IllegalArgumentException("message must be a business message!");
        }

        evidencePersistenceService.persistEvidenceMessageToBusinessMessage(message, message.getConnectorMessageId(), confirmation);

//        message.addTransportedMessageConfirmation(confirmation);
//        sendConfirmationBack(message, confirmation);

        CommonConfirmationProcessor commonConfirmationProcessor = new CommonConfirmationProcessor(messagePersistenceService);
        commonConfirmationProcessor.confirmRejectMessage(confirmation.getEvidenceType(), message);

    }


    public void processConfirmationForMessageAndSendBack(DomibusConnectorMessage message, DomibusConnectorMessageConfirmation confirmation) {
        if (!DomainModelHelper.isBusinessMessage(message)) {
            throw new IllegalArgumentException("message must be a business message!");
        }

        evidencePersistenceService.persistEvidenceMessageToBusinessMessage(message, message.getConnectorMessageId(), confirmation);

        message.addTransportedMessageConfirmation(confirmation);
        this.sendConfirmationBack(message, confirmation);

        CommonConfirmationProcessor commonConfirmationProcessor = new CommonConfirmationProcessor(messagePersistenceService);
        commonConfirmationProcessor.confirmRejectMessage(confirmation.getEvidenceType(), message);

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



}
