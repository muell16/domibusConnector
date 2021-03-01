package eu.domibus.connector.controller.processor;


import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;

import eu.domibus.connector.controller.processor.steps.MessageConfirmationStep;
import eu.domibus.connector.controller.processor.steps.CreateNewBusinessMessageInDBStep;
import eu.domibus.connector.controller.processor.steps.LookupBackendNameStep;
import eu.domibus.connector.controller.processor.steps.ResolveECodexContainerStep;
import eu.domibus.connector.controller.processor.steps.SubmitMessageToLinkModuleQueueStep;
import eu.domibus.connector.controller.processor.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.controller.spring.ConnectorTestConfigurationProperties;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageErrorBuilder;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.service.*;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

import javax.transaction.Transactional;

@Component(ToBackendBusinessMessageProcessor.GW_TO_BACKEND_MESSAGE_PROCESSOR)
@RequiredArgsConstructor
public class ToBackendBusinessMessageProcessor implements DomibusConnectorMessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ToBackendBusinessMessageProcessor.class);

	public static final String GW_TO_BACKEND_MESSAGE_PROCESSOR = "GatewayToBackendMessageProcessor";

	private final ConnectorTestConfigurationProperties connectorTestConfigurationProperties;
	private final DCMessagePersistenceService messagePersistenceService;
	private final CreateConfirmationMessageBuilderFactoryImpl confirmationMessageBuilderFactory;
	private final DomibusConnectorSecurityToolkit securityToolkit;
	private final DomibusConnectorMessageErrorPersistenceService messageErrorPersistenceService;
	private final MessageConfirmationStep messageConfirmationStep;


	private final ResolveECodexContainerStep resolveECodexContainerStep;
	private final CreateNewBusinessMessageInDBStep createNewBusinessMessageInDBStep;
	private final SubmitMessageToLinkModuleQueueStep submitMessageToLinkModuleQueueStep;
	private final LookupBackendNameStep lookupBackendNameStep;

    @Override
//	@StoreMessageExceptionIntoDatabase
//	@Transactional(Transactional.TxType.REQUIRES_NEW) //start new nested transaction...
	@MDC(name = LoggingMDCPropertyNames.MDC_DC_MESSAGE_PROCESSOR_PROPERTY_NAME, value = GW_TO_BACKEND_MESSAGE_PROCESSOR)
	public void processMessage(final DomibusConnectorMessage incomingMessage) {
		try (org.slf4j.MDC.MDCCloseable var = org.slf4j.MDC.putCloseable(LoggingMDCPropertyNames.MDC_EBMS_MESSAGE_ID_PROPERTY_NAME, incomingMessage.getMessageDetails().getEbmsMessageId())) {

			//persistMessage
			createNewBusinessMessageInDBStep.executeStep(incomingMessage);

			//process all with this business message transported confirmations
			messageConfirmationStep.processTransportedConfirmations(incomingMessage);

			//Create relayREMMD
			CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper relayREMMDEvidence = createRelayREMMDEvidence(incomingMessage, true);
			//process created relayREMMD (save it to db and attach it to transported message)
			messageConfirmationStep.processConfirmationForMessage(incomingMessage, relayREMMDEvidence.getMessageConfirmation());

			//send confirmation msg back
			submitMessageToLinkModuleQueueStep.submitMessageOpposite(incomingMessage, relayREMMDEvidence.getEvidenceMessage());

			//resolve ecodex-Container
			resolveECodexContainerStep.executeStep(incomingMessage);

			//lookup correct backend name
			lookupBackendNameStep.executeStep(incomingMessage);

			submitMessageToLinkModuleQueueStep.submitMessage(incomingMessage);


			LOGGER.info(LoggingMarker.BUSINESS_LOG, "Successfully processed incomingMessage from GW to backend.");

		} catch (DomibusConnectorSecurityException e) {
			LOGGER.warn("Security Exception occured! Responding with RelayRemmdRejection ConfirmationMessage", e);
			CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper negativeEvidence = createNonDeliveryEvidence(incomingMessage);
//			CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper negativeEvidence = createRelayREMMDEvidence(incomingMessage, false);
			messageConfirmationStep.processConfirmationForMessage(incomingMessage, negativeEvidence.getMessageConfirmation());

			submitMessageToLinkModuleQueueStep.submitMessageOpposite(incomingMessage, negativeEvidence.getEvidenceMessage());
		}
	}

//	private CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper createDeliveryEvidence(DomibusConnectorMessage originalMessage)
//			throws DomibusConnectorControllerException, DomibusConnectorMessageException {
//
//		CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper wrappedDeliveryEvidenceMsg = confirmationMessageBuilderFactory
//				.createConfirmationMessageBuilderFromBusinessMessage(originalMessage, DomibusConnectorEvidenceType.DELIVERY)
//				.switchFromToAttributes()
//				.withDirection(MessageTargetSource.GATEWAY)
//				.build();
//
//		messageConfirmationStep.processConfirmationForMessage(originalMessage, wrappedDeliveryEvidenceMsg.getMessageConfirmation());
//
//		return wrappedDeliveryEvidenceMsg;
//
//	}
	
	private CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper createNonDeliveryEvidence(DomibusConnectorMessage originalMessage)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {
		try {
			CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper wrappedDeliveryEvidenceMsg = confirmationMessageBuilderFactory
					.createConfirmationMessageBuilderFromBusinessMessage(originalMessage, DomibusConnectorEvidenceType.NON_DELIVERY)
					.switchFromToAttributes()
					.withDirection(MessageTargetSource.GATEWAY)
					.build();
			return wrappedDeliveryEvidenceMsg;
		} catch (DomibusConnectorEvidencesToolkitException e) {
			DomibusConnectorMessageException evidenceBuildFailed = DomibusConnectorMessageExceptionBuilder.createBuilder()
					.setMessage(originalMessage)
					.setText("Error creating NonDelivery evidence for originalMessage!")
					.setSource(this.getClass())
					.setCause(e)
					.build();
			LOGGER.error("Failed to create Evidence", evidenceBuildFailed);
			DomibusConnectorMessageError messageError =
					DomibusConnectorMessageErrorBuilder.createBuilder()
							.setSource(this.getClass().getName())
							.setDetails(e)
							.setText("Error creating NonDelivery evidence for originalMessage!")
							.build();
			messageErrorPersistenceService.persistMessageError(originalMessage.getConnectorMessageId(), messageError);
			throw evidenceBuildFailed;
		}
	}

	private CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper createRelayREMMDEvidence(DomibusConnectorMessage originalMessage, boolean isAcceptance)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {
		CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder wrappedMessageConfirmationBuilder = null;


		try {
			if(isAcceptance) {
			    LOGGER.trace("relay is acceptance, generating RELAY_REMMD_ACCEPTANCE");
                wrappedMessageConfirmationBuilder = confirmationMessageBuilderFactory.createConfirmationMessageBuilderFromBusinessMessage(originalMessage, DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE);
            } else {
                LOGGER.trace("relay is denied, generating RELAY_REMMD_REJECTION");
                wrappedMessageConfirmationBuilder = confirmationMessageBuilderFactory.createConfirmationMessageBuilderFromBusinessMessage(originalMessage, DomibusConnectorEvidenceType.RELAY_REMMD_REJECTION);
            }

			CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper wrappedEvidenceMessage = wrappedMessageConfirmationBuilder
					.switchFromToAttributes()
					.withDirection(MessageTargetSource.GATEWAY)
					.build();
			LOGGER.trace("generated confirmation is [{}]", wrappedEvidenceMessage.getMessageConfirmation());
			return wrappedEvidenceMessage;

		} catch (DomibusConnectorEvidencesToolkitException e) {
			DomibusConnectorMessageException evidenceBuildFailed = DomibusConnectorMessageExceptionBuilder.createBuilder()
					.setMessage(originalMessage)
					.setText("Error creating RelayREMMD evidence for originalMessage!")
					.setSource(this.getClass())
					.setCause(e)
					.build();
            LOGGER.error("Failed to create Evidence", evidenceBuildFailed);
            DomibusConnectorMessageError messageError =
                    DomibusConnectorMessageErrorBuilder.createBuilder()
                            .setSource(this.getClass().getName())
                            .setDetails(e)
                            .setText("Error creating RelayREMMD evidence for originalMessage!")
                        .build();
            messageErrorPersistenceService.persistMessageError(originalMessage.getConnectorMessageId(), messageError);
            throw evidenceBuildFailed;
		}

	}

}
