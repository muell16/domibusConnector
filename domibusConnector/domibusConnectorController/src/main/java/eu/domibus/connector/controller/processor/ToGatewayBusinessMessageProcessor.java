package eu.domibus.connector.controller.processor;

import eu.domibus.connector.controller.processor.steps.MessageConfirmationStep;
import eu.domibus.connector.controller.processor.steps.BuildECodexContainerStep;
import eu.domibus.connector.controller.processor.steps.CreateNewBusinessMessageInDBStep;
import eu.domibus.connector.controller.processor.steps.SubmitMessageToLinkModuleQueueStep;
import eu.domibus.connector.controller.processor.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;

/**
 * Takes a originalMessage from backend and creates evidences for it
 * and also wraps it into an asic container and delivers the
 * originalMessage to the gw
 */
@Component //(ToGatewayBusinessMessageProcessor.BACKEND_TO_GW_MESSAGE_PROCESSOR_BEAN_NAME)
@RequiredArgsConstructor
public class ToGatewayBusinessMessageProcessor implements DomibusConnectorMessageProcessor {

    public static final String BACKEND_TO_GW_MESSAGE_PROCESSOR_BEAN_NAME = "ToGatewayBusinessMessageProcessor";

    private static final Logger LOGGER = LoggerFactory.getLogger(ToGatewayBusinessMessageProcessor.class);

    private final CreateNewBusinessMessageInDBStep createNewBusinessMessageInDBStep;
    private final BuildECodexContainerStep buildECodexContainerStep;
    private final SubmitMessageToLinkModuleQueueStep submitMessageToLinkStep;

    private final MessageConfirmationStep messageConfirmationStep;
    private final CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactoryImpl;

    @MDC(name = LoggingMDCPropertyNames.MDC_DC_MESSAGE_PROCESSOR_PROPERTY_NAME, value = BACKEND_TO_GW_MESSAGE_PROCESSOR_BEAN_NAME)
    public void processMessage(DomibusConnectorMessage message) {
        try (org.slf4j.MDC.MDCCloseable var = org.slf4j.MDC.putCloseable(LoggingMDCPropertyNames.MDC_BACKEND_MESSAGE_ID_PROPERTY_NAME, message.getMessageDetails().getBackendMessageId())){

            //buildEcodexContainerStep
            buildECodexContainerStep.executeStep(message);

            //persistence step
            createNewBusinessMessageInDBStep.executeStep(message);

            //create confirmation
            CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper confirmationMessageWrapper = createSubmissionAcceptanceEvidence(message);
            //process created confirmation for message
            messageConfirmationStep.processConfirmationForMessage(message, confirmationMessageWrapper.getMessageConfirmation());
            //append confirmation to message
            message.getTransportedMessageConfirmations().add(confirmationMessageWrapper.getMessageConfirmation());

            //submit message to GW
            submitMessageToLinkStep.submitMessage(message);
            //submit evidence message to BACKEND
            submitMessageToLinkStep.submitMessageOpposite(message, confirmationMessageWrapper.getEvidenceMessage());

            LOGGER.info(LoggingMarker.BUSINESS_LOG, "Successfully processed message with backendId [{}] to Link", message.getMessageDetails().getBackendMessageId());

        } catch (DomibusConnectorEvidencesToolkitException ete) {
            LOGGER.error("Could not generate evidence [{}] for originalMessage [{}]!", DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, message);

            CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper errorMessage = createSubmissionRejection(message);
            submitMessageToLinkStep.submitMessageOpposite(message, errorMessage.getEvidenceMessage());

            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not generate evidence submission acceptance! ")
                    .setSource(this.getClass())
                    .setCause(ete)
                    .buildAndThrow();
        }
    }

    private CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper createSubmissionRejection(DomibusConnectorMessage message) {
        CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder submissionAcceptanceConfirmationMessageBuilder = this.createConfirmationMessageBuilderFactoryImpl.createConfirmationMessageBuilderFromBusinessMessage(message, DomibusConnectorEvidenceType.SUBMISSION_REJECTION);
        CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper errorMessage = submissionAcceptanceConfirmationMessageBuilder
                .switchFromToAttributes()
                .withDirection(MessageTargetSource.BACKEND)
                .setRejectionReason(DomibusConnectorRejectionReason.OTHER)
                .build();
        return errorMessage;
    }

    private CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper createSubmissionAcceptanceEvidence(DomibusConnectorMessage message) {
        CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder submissionAcceptanceConfirmationMessageBuilder = this.createConfirmationMessageBuilderFactoryImpl.createConfirmationMessageBuilderFromBusinessMessage(message, DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE);
        CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper confirmationMessageWrapper = submissionAcceptanceConfirmationMessageBuilder
                .switchFromToAttributes()
                .withDirection(MessageTargetSource.BACKEND)
                .build();
        return confirmationMessageWrapper;
    }

}
