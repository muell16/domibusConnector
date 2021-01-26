package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.process.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.domain.enums.MessageTargetSource;
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
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Takes a originalMessage from backend and creates evidences for it
 * and also wraps it into an asic container and delivers the
 * originalMessage to the gw
 */
@Component(BackendToGatewayMessageProcessor.BACKEND_TO_GW_MESSAGE_PROCESSOR_BEAN_NAME)
public class BackendToGatewayMessageProcessor implements DomibusConnectorMessageProcessor {

    public static final String BACKEND_TO_GW_MESSAGE_PROCESSOR_BEAN_NAME = "BackendToGatewayMessageProcessor";

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendToGatewayMessageProcessor.class);

    private DCMessagePersistenceService messagePersistenceService;
    private DomibusConnectorSecurityToolkit securityToolkit;
    private MessageConfirmationProcessor messageConfirmationProcessor;
    private SubmitMessageToLinkModuleService submitMessageService;

    @Autowired
    private CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactoryImpl;

    @Autowired
    private CreateSubmissionRejectionAndReturnItService createSubmissionRejectionAndReturnItService;

    @Autowired
    public void setMessageConfirmationProcessor(MessageConfirmationProcessor messageConfirmationProcessor) {
        this.messageConfirmationProcessor = messageConfirmationProcessor;
    }

    @Autowired
    public void setSubmitMessageService(SubmitMessageToLinkModuleService submitMessageService) {
        this.submitMessageService = submitMessageService;
    }

    public void setCreateConfirmationMessageBuilderFactoryImpl(CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactoryImpl) {
        this.createConfirmationMessageBuilderFactoryImpl = createConfirmationMessageBuilderFactoryImpl;
    }

    public void setCreateSubmissionRejectionAndReturnItService(CreateSubmissionRejectionAndReturnItService createSubmissionRejectionAndReturnItService) {
        this.createSubmissionRejectionAndReturnItService = createSubmissionRejectionAndReturnItService;
    }

    @Autowired
    public void setMessagePersistenceService(DCMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    @Autowired
    public void setSecurityToolkit(DomibusConnectorSecurityToolkit securityToolkit) {
        this.securityToolkit = securityToolkit;
    }


    @Override
    @Transactional(propagation = Propagation.NEVER)
    @StoreMessageExceptionIntoDatabase
    @MDC(name = LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_PROCESSOR_PROPERTY_NAME, value = BACKEND_TO_GW_MESSAGE_PROCESSOR_BEAN_NAME)
    public void processMessage(DomibusConnectorMessage message) {

        try {
            securityToolkit.buildContainer(message);

            DomibusConnectorMessage submissionAcceptanceConfirmationMessage = null;

            //process every transported confirmation
            message.getTransportedMessageConfirmations().stream()
                    .forEach(c -> messageConfirmationProcessor.processConfirmationForMessage(message, c));


            CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder submissionAcceptanceConfirmationMessageBuilder = this.createConfirmationMessageBuilderFactoryImpl.createConfirmationMessageBuilderFromBusinessMessage(message, DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE);
            CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper confirmationMessageWrapper = submissionAcceptanceConfirmationMessageBuilder
                    .switchFromToAttributes()
                    .withDirection(MessageTargetSource.BACKEND)
                    .build();
//            submissionAcceptanceConfirmationMessage = confirmationMessageWrapper.getEvidenceMessage();

            //process created confirmation for message and send it backwards
            messageConfirmationProcessor.processConfirmationForMessageAndSendBack(message, confirmationMessageWrapper.getMessageConfirmation());

            submitMessageService.submitMessage(message);
//
//            LOGGER.trace("#processMessage: persist evidence originalMessage [{}] into database", submissionAcceptanceConfirmationMessage);

            LOGGER.info("Successfully sent originalMessage {} to gateway.", message);


        } catch (DomibusConnectorEvidencesToolkitException ete) {
            LOGGER.error("Could not generate evidence [{}] for originalMessage [{}]!", DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, message);
            createSubmissionRejectionAndReturnItService.createSubmissionRejectionAndReturnIt(message, ete.getMessage());
            messagePersistenceService.rejectMessage(message);
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not generate evidence submission acceptance! ")
                    .setSource(this.getClass())
                    .setCause(ete)
                    .buildAndThrow();

        } catch (DomibusConnectorSecurityException se) {
            LOGGER.warn("security toolkit build container failed. The exception will be logged to database", se);
            messagePersistenceService.rejectMessage(message);
            createSubmissionRejectionAndReturnItService.createSubmissionRejectionAndReturnIt(message, se.getMessage());
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText(se.getMessage())
                    .setSource(this.getClass())
                    .setCause(se)
                    .buildAndThrow();

        } catch (SubmitMessageToLinkModuleService.DCSubmitMessageToLinkException e) {
            LOGGER.warn("Cannot submit message to gateway", e);
            createSubmissionRejectionAndReturnItService.createSubmissionRejectionAndReturnIt(message, e.getMessage());
            messagePersistenceService.rejectMessage(message);
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not submit originalMessage to gateway! ")
                    .setSource(this.getClass())
                    .setCause(e)
                    .buildAndThrow();
        }

    }

}
