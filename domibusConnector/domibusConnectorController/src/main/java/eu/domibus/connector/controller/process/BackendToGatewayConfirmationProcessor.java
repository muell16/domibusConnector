package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.process.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.model.enums.EvidenceType;
import eu.domibus.connector.persistence.service.*;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Message;

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
    private DomibusConnectorMessagePersistenceService messagePersistenceService;
    private DomibusConnectorGatewaySubmissionService gwSubmissionService;
    private DomibusConnectorBackendDeliveryService backendDeliveryService;

    //setter
    @Autowired
    public void setConfirmationMessageService(CreateConfirmationMessageBuilderFactoryImpl confirmationMessageService) {
        this.confirmationMessageService = confirmationMessageService;
    }

    @Autowired
    public void setMessagePersistenceService(DomibusConnectorMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    @Autowired
    public void setGwSubmissionService(DomibusConnectorGatewaySubmissionService gwSubmissionService) {
        this.gwSubmissionService = gwSubmissionService;
    }

    @Autowired
    public void setBackendDeliveryService(DomibusConnectorBackendDeliveryService backendDeliveryService) {
        this.backendDeliveryService = backendDeliveryService;
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    @StoreMessageExceptionIntoDatabase
    @MDC(name = LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_PROCESSOR_PROPERTY_NAME, value = BACKEND_TO_GW_CONFIRMATION_PROCESSOR_BEAN_NAME)
    public void processMessage(DomibusConnectorMessage message) {
        if (!DomainModelHelper.isEvidenceMessage(message)) {
            throw new IllegalArgumentException("The originalMessage is not an evidence originalMessage!");
        }

        if (!DomainModelHelper.isEvidenceMessageTrigger(message)) {
            LOGGER.warn("The evidence of the message is already generated. The current connector will generate a new evidence anyway. Future are going to use the already provided evidence!");
        }

        DomibusConnectorEvidenceType evidenceType = DomainModelHelper.getEvidenceTypeOfEvidenceMessage(message);
        if ((evidenceType == DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE || evidenceType == DomibusConnectorEvidenceType.SUBMISSION_REJECTION)) {
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setText("The backend is not allowed to trigger a SUBMISSION_ACCEPTANCE or SUBMISSION_REJECTION evidence!")
                    .setSource(this.getClass())
                    .buildAndThrow();
        }

        String refToOriginalMessage = message.getMessageDetails().getRefToMessageId();
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
                    .setMessage(message)
                    .setText(String.format("Message [%s] is already confirmed or rejected no more evidences will be handled for that message!", originalMessage.getConnectorMessageId())                          )
                    .setSource(this.getClass())
                    .buildAndThrow();
        }

        CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder confirmationMessageBuilder
                = confirmationMessageService.createConfirmationMessageBuilder(originalMessage, evidenceType);


        sendAsEvidenceMessageToGw(evidenceType, originalMessage, confirmationMessageBuilder);

        CommonConfirmationProcessor commonConfirmationProcessor = new CommonConfirmationProcessor(messagePersistenceService);
        commonConfirmationProcessor.confirmRejectMessage(evidenceType, originalMessage);

        sendAsEvidenceMessageBackToBackend(confirmationMessageBuilder);

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
                .getMessageConfirmations()
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
        DomibusConnectorMessage evidenceMessage = confirmationMessageBuilder
                .switchFromToParty()
                .withDirection(MessageTargetSource.BACKEND)
                .build()
                .getEvidenceMessage();
        backendDeliveryService.deliverMessageToBackend(evidenceMessage);
    }

    private void sendAsEvidenceMessageToGw(DomibusConnectorEvidenceType evidenceType, DomibusConnectorMessage originalMessage, CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder confirmationMessageBuilder) {
        CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper wrappedConfirmation = confirmationMessageBuilder
                .switchFromToParty()
                .withDirection(MessageTargetSource.GATEWAY)
                .build();

        wrappedConfirmation.persistEvidenceToMessage();
        DomibusConnectorMessageConfirmation confirmation = wrappedConfirmation.getMessageConfirmation();
        originalMessage.addConfirmation(confirmation);

        DomibusConnectorMessage evidenceMessage = wrappedConfirmation.getEvidenceMessage();

        submitToGateway(evidenceMessage, originalMessage);
        
        wrappedConfirmation.setEvidenceDeliveredToGateway();

        LOGGER.info(BUSINESS_LOG, "Successfully sent evidence of type [{}] for originalMessage [{}] to gateway.", confirmation.getEvidenceType(), originalMessage);
    }

    private void submitToGateway(DomibusConnectorMessage evidenceMessage, DomibusConnectorMessage originalMessage) {
        try {
            gwSubmissionService.submitToGateway(evidenceMessage);
        } catch (DomibusConnectorGatewaySubmissionException gwse) {
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(originalMessage)
                    .setText("Could not send Evidence Message to Gateway!")
                    .setSource(this.getClass())
                    .setCause(gwse)
                    .buildAndThrow();
        }
    }

//    private void setDeliveredToGateway(DomibusConnectorMessage evidenceMessage) {
//        try {
//            messagePersistenceService.setDeliveredToGateway(evidenceMessage);
//        } catch (PersistenceException persistenceException) {
//            LOGGER.error("persistence Exception occured", persistenceException);
//        }
//    }


}
