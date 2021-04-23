package eu.domibus.connector.controller.processor;


import eu.domibus.connector.controller.processor.steps.*;
import eu.domibus.connector.controller.processor.util.ConfirmationCreatorService;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageErrorBuilder;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.service.*;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

@Component(ToBackendBusinessMessageProcessor.GW_TO_BACKEND_MESSAGE_PROCESSOR)
public class ToBackendBusinessMessageProcessor implements DomibusConnectorMessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ToBackendBusinessMessageProcessor.class);

	public static final String GW_TO_BACKEND_MESSAGE_PROCESSOR = "GatewayToBackendMessageProcessor";

	private final DomibusConnectorEvidencesToolkit evidencesToolkit;
	private final MessageConfirmationStep messageConfirmationStep;
	private final ResolveECodexContainerStep resolveECodexContainerStep;
	private final CreateNewBusinessMessageInDBStep createNewBusinessMessageInDBStep;
	private final SubmitMessageToLinkModuleQueueStep submitMessageToLinkModuleQueueStep;
	private final LookupBackendNameStep lookupBackendNameStep;
	private final SubmitConfirmationAsEvidenceMessageStep submitAsEvidenceMessageToLink;

	public ToBackendBusinessMessageProcessor(
			DomibusConnectorEvidencesToolkit evidencesToolkit,
			MessageConfirmationStep messageConfirmationStep,
			ResolveECodexContainerStep resolveECodexContainerStep,
			CreateNewBusinessMessageInDBStep createNewBusinessMessageInDBStep,
			SubmitMessageToLinkModuleQueueStep submitMessageToLinkModuleQueueStep,
			SubmitConfirmationAsEvidenceMessageStep submitAsEvidenceMessageToLink,
			LookupBackendNameStep lookupBackendNameStep) {
		this.evidencesToolkit = evidencesToolkit;
		this.submitAsEvidenceMessageToLink = submitAsEvidenceMessageToLink;
		this.messageConfirmationStep = messageConfirmationStep;
		this.resolveECodexContainerStep = resolveECodexContainerStep;
		this.createNewBusinessMessageInDBStep = createNewBusinessMessageInDBStep;
		this.submitMessageToLinkModuleQueueStep = submitMessageToLinkModuleQueueStep;
		this.lookupBackendNameStep = lookupBackendNameStep;
	}

	@Override
	@MDC(name = LoggingMDCPropertyNames.MDC_DC_MESSAGE_PROCESSOR_PROPERTY_NAME, value = GW_TO_BACKEND_MESSAGE_PROCESSOR)
	public void processMessage(final DomibusConnectorMessage incomingMessage) {
		try (org.slf4j.MDC.MDCCloseable var = org.slf4j.MDC.putCloseable(LoggingMDCPropertyNames.MDC_EBMS_MESSAGE_ID_PROPERTY_NAME, incomingMessage.getMessageDetails().getEbmsMessageId())) {

			//lookup correct backend name
			lookupBackendNameStep.executeStep(incomingMessage);

			//persistMessage
			createNewBusinessMessageInDBStep.executeStep(incomingMessage);

			//process all with this business message transported confirmations
			messageConfirmationStep.processTransportedConfirmations(incomingMessage);

			//Create relayREMMD
			DomibusConnectorMessageConfirmation relayREMMDEvidence = createRelayREMMDEvidence(incomingMessage, true);
			//process created relayREMMD (save it to db and attach it to transported message)
			messageConfirmationStep.processConfirmationForMessage(incomingMessage, relayREMMDEvidence);

			//send confirmation msg back
			submitAsEvidenceMessageToLink.submitOppositeDirection(null, incomingMessage, relayREMMDEvidence);

			//resolve ecodex-Container
			resolveECodexContainerStep.executeStep(incomingMessage);

			submitMessageToLinkModuleQueueStep.submitMessage(incomingMessage);


			LOGGER.info(LoggingMarker.BUSINESS_LOG, "Successfully processed incomingMessage from GW to backend.");

		} catch (DomibusConnectorSecurityException e) {
			LOGGER.warn("Security Exception occured! Responding with RelayRemmdRejection ConfirmationMessage", e);
			DomibusConnectorMessageConfirmation negativeEvidence = createNonDeliveryEvidence(incomingMessage);
			messageConfirmationStep.processConfirmationForMessage(incomingMessage, negativeEvidence);
			//respond with negative evidence...
			submitAsEvidenceMessageToLink.submitOppositeDirection(null, incomingMessage, negativeEvidence);
		}
	}

	
	private DomibusConnectorMessageConfirmation createNonDeliveryEvidence(DomibusConnectorMessage originalMessage)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {
		try {
			return evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.NON_DELIVERY,
					originalMessage,
					DomibusConnectorRejectionReason.OTHER, "");

		} catch (DomibusConnectorEvidencesToolkitException e) {
			DomibusConnectorMessageException evidenceBuildFailed = DomibusConnectorMessageExceptionBuilder.createBuilder()
					.setMessage(originalMessage)
					.setText("Error creating NonDelivery evidence for originalMessage!")
					.setSource(this.getClass())
					.setCause(e)
					.build();
			LOGGER.error("Failed to create Evidence", evidenceBuildFailed);
			throw evidenceBuildFailed;
		}
	}

	private DomibusConnectorMessageConfirmation createRelayREMMDEvidence(DomibusConnectorMessage originalMessage, boolean isAcceptance)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {

		try {
			if(isAcceptance) {
			    LOGGER.trace("relay is acceptance, generating RELAY_REMMD_ACCEPTANCE");
				return evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE,
						originalMessage,
						DomibusConnectorRejectionReason.OTHER, "");
            } else {
                LOGGER.trace("relay is denied, generating RELAY_REMMD_REJECTION");
                return evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.RELAY_REMMD_REJECTION,
						originalMessage,
						DomibusConnectorRejectionReason.OTHER, "");
            }

		} catch (DomibusConnectorEvidencesToolkitException e) {
			DomibusConnectorMessageException evidenceBuildFailed = DomibusConnectorMessageExceptionBuilder.createBuilder()
					.setMessage(originalMessage)
					.setText("Error creating RelayREMMD evidence for originalMessage!")
					.setSource(this.getClass())
					.setCause(e)
					.build();
            LOGGER.error("Failed to create Evidence", evidenceBuildFailed);
            throw evidenceBuildFailed;
		}

	}

}
