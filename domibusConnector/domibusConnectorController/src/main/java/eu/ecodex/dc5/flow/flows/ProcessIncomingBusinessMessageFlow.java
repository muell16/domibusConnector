package eu.ecodex.dc5.flow.flows;


import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import eu.ecodex.dc5.flow.events.MessageReadyForTransportEvent;
import eu.ecodex.dc5.flow.steps.*;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5Confirmation;
import eu.ecodex.dc5.message.validation.IncomingBusinessMesssageRules;
import eu.ecodex.dc5.message.validation.IncomingMessageRules;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static eu.ecodex.dc5.message.ConfirmationCreatorService.toDC5Confirmation;
import static eu.ecodex.dc5.message.ConfirmationCreatorService.toMessageParams;

@Component(ProcessIncomingBusinessMessageFlow.GW_TO_BACKEND_MESSAGE_PROCESSOR)
@RequiredArgsConstructor
public class ProcessIncomingBusinessMessageFlow {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessIncomingBusinessMessageFlow.class);

	public static final String GW_TO_BACKEND_MESSAGE_PROCESSOR = "ProcessIncomingBusinessMessageFlow";

	private final DomibusConnectorEvidencesToolkit evidencesToolkit;
	private final MessageConfirmationStep messageConfirmationStep;
	private final ResolveECodexContainerStep resolveECodexContainerStep;
	private final LookupBackendNameStep lookupBackendNameStep;
	private final SubmitConfirmationAsEvidenceMessageStep submitAsEvidenceMessageToLink;
	private final VerifyPModesStep verifyPModesStep;
	private final ApplicationEventPublisher eventPublisher;

	private final Validator validator;


	@MDC(name = LoggingMDCPropertyNames.MDC_DC_MESSAGE_PROCESSOR_PROPERTY_NAME, value = GW_TO_BACKEND_MESSAGE_PROCESSOR)
	public void processMessage(final DC5Message incomingMessage) {

		validateIncomingBusinessMessage(incomingMessage);


		try (org.slf4j.MDC.MDCCloseable var = org.slf4j.MDC.putCloseable(LoggingMDCPropertyNames.MDC_EBMS_MESSAGE_ID_PROPERTY_NAME, incomingMessage.getEbmsData().getEbmsMessageId().getEbmsMesssageId())) {

			//verify pModes
			verifyPModesStep.verifyIncoming(incomingMessage);

			//lookup correct backend name
			lookupBackendNameStep.executeStep(incomingMessage);

			//process all with this business message transported confirmations
			messageConfirmationStep.processTransportedConfirmations(incomingMessage);

			//Create relayREMMD //TODO: decide if this should maybe generated after msg successfully transported to backend link?
			DC5Confirmation relayREMMDEvidence = createRelayREMMDEvidence(incomingMessage, true);
			//process created relayREMMD (save it to db and attach it to transported message)
			messageConfirmationStep.processConfirmationForMessage(incomingMessage, relayREMMDEvidence);

			//send confirmation msg back
			submitAsEvidenceMessageToLink.submitOppositeDirection(null, incomingMessage, relayREMMDEvidence);
			//TODO: !!!


			//resolve ecodex-Container
			resolveECodexContainerStep.executeStep(incomingMessage);

//			submitMessageToLinkStep.submitMessage(incomingMessage);
			// TODO: !!

//			LOGGER.info(LoggingMarker.BUSINESS_LOG, "Put processed incoming Business Message with EBMS ID [{}] from GW to Backend Link [{}] on to Link Queue",
//					incomingMessage.getEbmsData().getEbmsMessageId(),
//					incomingMessage.getBackendLinkName()
//			);
//			return incomingMessage;
			MessageReadyForTransportEvent messageReadyForTransportEvent = MessageReadyForTransportEvent.of(incomingMessage.getId(),
					incomingMessage.getBackendLinkName(),
					MessageTargetSource.BACKEND);

			eventPublisher.publishEvent(messageReadyForTransportEvent); //publish message

		} catch (DomibusConnectorSecurityException e) {
			LOGGER.warn("Security Exception occured! Responding with RelayRemmdRejection ConfirmationMessage", e);
			DC5Confirmation negativeEvidence = createNonDeliveryEvidence(incomingMessage);
			messageConfirmationStep.processConfirmationForMessage(incomingMessage, negativeEvidence);
			//respond with negative evidence...
			submitAsEvidenceMessageToLink.submitOppositeDirection(null, incomingMessage, negativeEvidence);
			LOGGER.warn(LoggingMarker.BUSINESS_LOG, "Rejected processed incoming Business Message with EBMS ID [{}] from GW to Backend Link [{}] due security exception",
					incomingMessage.getEbmsData().getEbmsMessageId(),
					incomingMessage.getBackendLinkName()
			);
		}
	}

	private void validateIncomingBusinessMessage(DC5Message incomingMessage) {
		Set<ConstraintViolation<DC5Message>> validate = validator.validate(incomingMessage, IncomingMessageRules.class, IncomingBusinessMesssageRules.class);

		if (!validate.isEmpty()) {
			throw new IllegalArgumentException("Message is not valid due: " + validate
					.stream()
					.map(ConstraintViolation::getMessage)
					.collect(Collectors.joining("\n")));
		}
	}


	private DC5Confirmation createNonDeliveryEvidence(DC5Message originalMessage)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {

		DomibusConnectorEvidencesToolkit.MessageParameters msgData = toMessageParams(originalMessage);

		try {
			DomibusConnectorEvidencesToolkit.Evidence evidence = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.NON_DELIVERY,
					msgData,
					DomibusConnectorRejectionReason.OTHER, "");
			return toDC5Confirmation(evidence);


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



	private DC5Confirmation createRelayREMMDEvidence(DC5Message originalMessage, boolean isAcceptance)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {

		DomibusConnectorEvidencesToolkit.MessageParameters msgData = toMessageParams(originalMessage);
		try {
			DomibusConnectorEvidencesToolkit.Evidence evidence;
			if(isAcceptance) {
			    LOGGER.trace("relay is acceptance, generating RELAY_REMMD_ACCEPTANCE");
				evidence = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE,
						msgData,
						DomibusConnectorRejectionReason.OTHER, "");
			} else {
                LOGGER.trace("relay is denied, generating RELAY_REMMD_REJECTION");
				evidence = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.RELAY_REMMD_REJECTION,
						msgData,
						DomibusConnectorRejectionReason.OTHER, "");
			}
			return toDC5Confirmation(evidence);

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
