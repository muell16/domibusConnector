package eu.domibus.connector.controller.processor;

import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.controller.processor.steps.*;
import eu.ecodex.dc5.flow.steps.MessageConfirmationStep;
import eu.ecodex.dc5.flow.steps.SubmitConfirmationAsEvidenceMessageStep;
import eu.ecodex.dc5.flow.steps.SubmitMessageToLinkStep;
import eu.ecodex.dc5.message.ConfirmationCreatorService;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.ecodex.dc5.message.model.DC5Confirmation;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;

/**
 * Takes a originalMessage from backend and creates evidences for it
 * and also wraps it into an asic container and delivers the
 * originalMessage to the gw
 */
@Component
public class ToGatewayBusinessMessageProcessor implements DomibusConnectorMessageProcessor {

    public static final String BACKEND_TO_GW_MESSAGE_PROCESSOR_BEAN_NAME = "ToGatewayBusinessMessageProcessor";

    private static final Logger LOGGER = LoggerFactory.getLogger(ToGatewayBusinessMessageProcessor.class);

    private final CreateNewBusinessMessageInDBStep createNewBusinessMessageInDBStep;
    private final BuildECodexContainerStep buildECodexContainerStep;
    private final SubmitMessageToLinkStep submitMessageToLinkStep;
    private final MessageConfirmationStep messageConfirmationStep;
    private final ConfirmationCreatorService confirmationCreatorService;
    private final SubmitConfirmationAsEvidenceMessageStep submitAsEvidenceMessageToLink;
    private final LookupGatewayNameStep lookupGatewayNameStep;
    private final GenerateEbmsIdStep generateEbmsIdStep;
    private final VerifyPModesStep verifyPModesStep;
    private final ConfigurationPropertyManagerService configurationPropertyManagerService;

    public ToGatewayBusinessMessageProcessor(CreateNewBusinessMessageInDBStep createNewBusinessMessageInDBStep,
                                             BuildECodexContainerStep buildECodexContainerStep,
                                             SubmitMessageToLinkStep submitMessageToLinkStep,
                                             MessageConfirmationStep messageConfirmationStep,
                                             ConfirmationCreatorService confirmationCreatorService,
                                             SubmitConfirmationAsEvidenceMessageStep submitAsEvidenceMessageToLink,
                                             LookupGatewayNameStep lookupGatewayNameStep,
                                             GenerateEbmsIdStep generateEbmsIdStep,
                                             VerifyPModesStep verifyPModesStep,
                                             ConfigurationPropertyManagerService configurationPropertyManagerService) {
        this.submitAsEvidenceMessageToLink = submitAsEvidenceMessageToLink;
        this.createNewBusinessMessageInDBStep = createNewBusinessMessageInDBStep;
        this.buildECodexContainerStep = buildECodexContainerStep;
        this.submitMessageToLinkStep = submitMessageToLinkStep;
        this.messageConfirmationStep = messageConfirmationStep;
        this.confirmationCreatorService = confirmationCreatorService;
        this.lookupGatewayNameStep = lookupGatewayNameStep;
        this.generateEbmsIdStep = generateEbmsIdStep;
        this.verifyPModesStep = verifyPModesStep;
        this.configurationPropertyManagerService = configurationPropertyManagerService;
    }

    @MDC(name = LoggingMDCPropertyNames.MDC_DC_MESSAGE_PROCESSOR_PROPERTY_NAME, value = BACKEND_TO_GW_MESSAGE_PROCESSOR_BEAN_NAME)
    public void processMessage(DC5Message message) {
        try (org.slf4j.MDC.MDCCloseable var = org.slf4j.MDC.putCloseable(LoggingMDCPropertyNames.MDC_BACKEND_MESSAGE_ID_PROPERTY_NAME, message.getBackendData().getBackendMessageId().toString())){

            //verify p-Modes step

//            ConnectorMessageProcessingProperties.PModeVerificationMode outgoingPModeVerificationMode = connectorMessageProcessingProperties.getOutgoingPModeVerificationMode();
            verifyPModesStep.verifyOutgoing(message);

            //buildEcodexContainerStep
            buildECodexContainerStep.executeStep(message);

            //set gateway name
            lookupGatewayNameStep.executeStep(message);

            //set ebms id, the EBMS id is set here, because it might be possible, that a RELAY_REEMD_EVIDENCE
            //comes back from the remote connector before this connector has already received the by the
            //sending GW created EBMSID
            generateEbmsIdStep.executeStep(message);

            //persistence step
            createNewBusinessMessageInDBStep.executeStep(message);

            //create confirmation
            DC5Confirmation submissionAcceptanceConfirmation = confirmationCreatorService.createConfirmation(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, message, null, null);
            //process created confirmation for message
            messageConfirmationStep.processConfirmationForMessage(message, submissionAcceptanceConfirmation);
            //append confirmation to message
            message.getTransportedMessageConfirmations().add(submissionAcceptanceConfirmation);

            //submit message to GW
            submitMessageToLinkStep.submitMessage(message);
            //submit evidence message to BACKEND
            //TODO: do this after submitMessage was successfull! offload into extra queue
            submitAsEvidenceMessageToLink.submitOppositeDirection(null, message, submissionAcceptanceConfirmation);

            LOGGER.info(LoggingMarker.BUSINESS_LOG, "Put message with backendId [{}] to Gateway Link [{}] on toLink Queue.",
                    message.getBackendData().getBackendMessageId(),
                    message.getGatewayLinkName());

        } catch (DomibusConnectorEvidencesToolkitException ete) {
            LOGGER.error("Could not generate evidence [{}] for originalMessage [{}]!", DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, message);

            DC5Confirmation submissionRejction = confirmationCreatorService.createConfirmation(DomibusConnectorEvidenceType.SUBMISSION_REJECTION, message, DomibusConnectorRejectionReason.OTHER, "");
            submitAsEvidenceMessageToLink.submitOppositeDirection(null, message, submissionRejction);

            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not generate evidence submission acceptance! ")
                    .setSource(this.getClass())
                    .setCause(ete)
                    .buildAndThrow();
        }
    }

}
