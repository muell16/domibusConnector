package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;

import eu.domibus.connector.controller.processor.DomibusConnectorMessageProcessor;
import eu.domibus.connector.controller.processor.steps.SubmitMessageToLinkModuleQueueStep;
import eu.domibus.connector.controller.processor.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorMessageId;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.service.*;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.domibus.connector.tools.logging.LoggingMarker.BUSINESS_LOG;

/**
 * Takes an confirmation originalMessage from backend
 * and creates a new confirmation of the same confirmation type
 * and sends it to the gw
 */
@Component(BackendToGatewayConfirmationProcessor.BACKEND_TO_GW_CONFIRMATION_PROCESSOR_BEAN_NAME)
public class BackendToGatewayConfirmationProcessor implements DomibusConnectorMessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendToGatewayConfirmationProcessor.class);

    public static final String BACKEND_TO_GW_CONFIRMATION_PROCESSOR_BEAN_NAME = "BackendToGatewayConfirmationProcessor";

    private CreateConfirmationMessageBuilderFactoryImpl confirmationMessageService;
    private DCMessagePersistenceService messagePersistenceService;
    private SubmitMessageToLinkModuleQueueStep submitMessageToLinkModuleQueueStep;
    private MessageConfirmationProcessor messageConfirmationProcessor;

//    private DomibusConnectorGatewaySubmissionService gwSubmissionService;
//    private DomibusConnectorBackendDeliveryService backendDeliveryService;

    //setter
    @Autowired
    public void setMessageConfirmationProcessor(MessageConfirmationProcessor messageConfirmationProcessor) {
        this.messageConfirmationProcessor = messageConfirmationProcessor;
    }

    @Autowired
    public void setConfirmationMessageService(CreateConfirmationMessageBuilderFactoryImpl confirmationMessageService) {
        this.confirmationMessageService = confirmationMessageService;
    }

    @Autowired
    public void setMessagePersistenceService(DCMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    @Autowired
    public void setSubmitMessageToLinkModuleService(SubmitMessageToLinkModuleQueueStep submitMessageToLinkModuleQueueStep) {
        this.submitMessageToLinkModuleQueueStep = submitMessageToLinkModuleQueueStep;
    }

    //    @Autowired
//    public void setGwSubmissionService(DomibusConnectorGatewaySubmissionService gwSubmissionService) {
//        this.gwSubmissionService = gwSubmissionService;
//    }
//
//    @Autowired
//    public void setBackendDeliveryService(DomibusConnectorBackendDeliveryService backendDeliveryService) {
//        this.backendDeliveryService = backendDeliveryService;
//    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    @StoreMessageExceptionIntoDatabase
    @MDC(name = LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_PROCESSOR_PROPERTY_NAME, value = BACKEND_TO_GW_CONFIRMATION_PROCESSOR_BEAN_NAME)
    public void processMessage(DomibusConnectorMessage triggerMessage) {
        if (!DomainModelHelper.isEvidenceMessage(triggerMessage)) {
            throw new IllegalArgumentException("The originalMessage is not an evidence originalMessage!");
        }

        if (!DomainModelHelper.isEvidenceMessageTrigger(triggerMessage)) {
            LOGGER.warn("The evidence of the message is already generated. The current connector will generate a new evidence anyway. Future are going to use the already provided evidence!");
        }

        DomibusConnectorEvidenceType evidenceType = DomainModelHelper.getEvidenceTypeOfEvidenceMessage(triggerMessage);
        if ((evidenceType == DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE || evidenceType == DomibusConnectorEvidenceType.SUBMISSION_REJECTION)) {
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setText("The backend is not allowed to trigger a SUBMISSION_ACCEPTANCE or SUBMISSION_REJECTION evidence!")
                    .setSource(this.getClass())
                    .buildAndThrow();
        }

        String refToOriginalMessage = triggerMessage.getMessageDetails().getRefToMessageId();
        LOGGER.debug("#processMessage: refToMessageId is [{}]", refToOriginalMessage);


        DomibusConnectorMessageDirection origMsgDirection = DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND;
        //try EBMS id first, then backend message id
        DomibusConnectorMessage originalMessage = messagePersistenceService
                .findMessageByEbmsIdAndDirection(refToOriginalMessage, origMsgDirection)
                .orElse(messagePersistenceService
                        .findMessageByNationalIdAndDirection(refToOriginalMessage, origMsgDirection)
                        .orElse(null)
                );
        LOGGER.debug("#processMessage: processing evidence [{}] of original message [{}]", evidenceType, originalMessage);
        if (originalMessage == null) {
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setText(String.format("No message for refToMessageId [%s] with direction [%s] found!",
                            refToOriginalMessage,
                            origMsgDirection))
                    .setSource(this.getClass())
                    .buildAndThrow();
        }

        checkEvidenceAlreadyCreated(evidenceType, originalMessage);
        if (messagePersistenceService.checkMessageConfirmedOrRejected(originalMessage)
                && evidenceType != DomibusConnectorEvidenceType.RETRIEVAL && evidenceType != DomibusConnectorEvidenceType.NON_RETRIEVAL) {
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(triggerMessage)
                    .setText(String.format("Message [%s] is already confirmed or rejected no more evidences will be handled for that message!", originalMessage.getConnectorMessageIdAsString())                          )
                    .setSource(this.getClass())
                    .buildAndThrow();
        }

        CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder confirmationMessageBuilder
                = confirmationMessageService.createConfirmationMessageBuilderFromBusinessMessage(originalMessage, evidenceType);

        CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper backtravelEvidenceMessage =
                confirmationMessageBuilder
                        .build();

        CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper triggeredEvidenceMessage
                = confirmationMessageBuilder
                    .switchFromToAttributes()
                    .buildFromEvidenceTriggerMessage(triggerMessage);

        List<DomibusConnectorMessageId> collect = Stream.of(triggerMessage, triggeredEvidenceMessage.getEvidenceMessage(), backtravelEvidenceMessage.getEvidenceMessage())
                .map(DomibusConnectorMessage::getConnectorMessageId)
                .collect(Collectors.toList());

//        messageConfirmationProcessor.processConfirmationMessageForMessage(originalMessage, collect, triggeredEvidenceMessage.getMessageConfirmation());

        triggeredEvidenceMessage.persistMessage();
        submitMessageToLinkModuleQueueStep.submitMessage(triggeredEvidenceMessage.getEvidenceMessage());

        backtravelEvidenceMessage.persistMessage();
        submitMessageToLinkModuleQueueStep.submitMessage(backtravelEvidenceMessage.getEvidenceMessage());

        //sendAsEvidenceMessageToGw(originalMessage, triggerMessage, confirmationMessageBuilder);
//        CommonConfirmationProcessor commonConfirmationProcessor = new CommonConfirmationProcessor(messagePersistenceService);
        //commonConfirmationProcessor.appendConfirmationToMessage(,confirmationMessageBuilder.build().getMessageConfirmation());
//        commonConfirmationProcessor.confirmRejectMessage(evidenceType, originalMessage);
//        sendAsEvidenceMessageBackToBackend(confirmationMessageBuilder);

    }

    /**
     * Is checking if an evidence has already reached the maximum creation limit the message
     *  see {@link DomibusConnectorEvidenceType#getMaxOccurence()} for details how often
     *  an evidence can be assigned to an message
     *
     * @param evidenceType - the type of the evidence
     * @param originalMessage - the original message, the evidence is related to
     */
    private void checkEvidenceAlreadyCreated(DomibusConnectorEvidenceType evidenceType, DomibusConnectorMessage originalMessage) {
        LOGGER.debug("Checking if evidence of type [{}] has not already been created", evidenceType);
        if (evidenceType.getMaxOccurence() > 0 && originalMessage
                .getRelatedMessageConfirmations()
                .stream()
                .filter(c -> evidenceType.equals(c.getEvidenceType()))
                .count() > evidenceType.getMaxOccurence()
        ) {
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(originalMessage)
                    .setText(String.format("Cannot create evidence [%s] more than [%d] times!", evidenceType, evidenceType.getMaxOccurence()))
                    .setSource(this.getClass())
                    .buildAndThrow();
        }
    }

    private void sendAsEvidenceMessageBackToBackend(CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder confirmationMessageBuilder) {
        CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper evidenceMessageWrapper = confirmationMessageBuilder
                .switchFromToAttributes()
                .withDirection(MessageTargetSource.BACKEND)
                .build();

        evidenceMessageWrapper.persistMessage();
        //deliver to backend
        submitMessageToLinkModuleQueueStep.submitMessage(evidenceMessageWrapper.getEvidenceMessage());
    }

    private void sendAsEvidenceMessageToGw(DomibusConnectorMessage originalMessage, DomibusConnectorMessage triggerMessage, CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder confirmationMessageBuilder) {
        CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper wrappedConfirmation = confirmationMessageBuilder
                .switchFromToAttributes()
                .withDirection(MessageTargetSource.GATEWAY)
                .buildFromEvidenceTriggerMessage(triggerMessage);

        submitToGateway(wrappedConfirmation);
        LOGGER.info(BUSINESS_LOG, "Successfully submitted evidence of type [{}] for originalMessage [{}] to gateway transport.", wrappedConfirmation.getEvidenceType(), originalMessage);
    }

    private void submitToGateway(CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper evidenceMessage) {
//        evidenceMessage.persistMessage();
//        try {
            //gwSubmissionService.submitToGateway(evidenceMessage.getEvidenceMessage());

//        } catch (DomibusConnectorGatewaySubmissionException gwse) {
//            DomibusConnectorMessageExceptionBuilder.createBuilder()
////                    .setMessage(originalMessage)
//                    .setText("Could not send Evidence Message to Gateway!")
//                    .setSource(this.getClass())
//                    .setCause(gwse)
//                    .buildAndThrow();
//        }
    }


}
