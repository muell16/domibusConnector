package eu.domibus.connector.controller.process;


import javax.annotation.Nonnull;


import eu.domibus.connector.common.service.ConfigurationPropertyLoaderService;
import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.process.util.ConfirmationMessageBuilderFactory;
import eu.domibus.connector.controller.process.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.controller.spring.ConnectorTestConfigurationProperties;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageErrorBuilder;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.service.*;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.SetMessageOnLoggingContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

import static eu.domibus.connector.tools.logging.LoggingMarker.BUSINESS_EVIDENCE_LOG;
import static eu.domibus.connector.tools.logging.LoggingMarker.BUSINESS_LOG;

@Component(GatewayToBackendMessageProcessor.GW_TO_BACKEND_MESSAGE_PROCESSOR)
public class GatewayToBackendMessageProcessor implements DomibusConnectorMessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayToBackendMessageProcessor.class);

	public static final String GW_TO_BACKEND_MESSAGE_PROCESSOR = "GatewayToBackendMessageProcessor";



	public ConnectorTestConfigurationProperties connectorTestConfigurationProperties;
	private DomibusConnectorMessagePersistenceService messagePersistenceService;
	private DomibusConnectorGatewaySubmissionService gwSubmissionService;
	private ConfirmationMessageBuilderFactory confirmationMessageBuilderFactory;
	private DomibusConnectorSecurityToolkit securityToolkit;
	private DomibusConnectorBackendDeliveryService backendDeliveryService;
	private DomibusConnectorMessageErrorPersistenceService messageErrorPersistenceService;

	@Autowired
	public void setConnectorTestConfigurationProperties(ConnectorTestConfigurationProperties connectorTestConfigurationProperties) {
		this.connectorTestConfigurationProperties = connectorTestConfigurationProperties;
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
	public void setConfirmationMessageBuilderFactory(ConfirmationMessageBuilderFactory confirmationMessageBuilderFactory) {
		this.confirmationMessageBuilderFactory = confirmationMessageBuilderFactory;
	}


	@Autowired
	public void setSecurityToolkit(DomibusConnectorSecurityToolkit securityToolkit) {
		this.securityToolkit = securityToolkit;
	}

	@Autowired
	public void setBackendDeliveryService(DomibusConnectorBackendDeliveryService backendDeliveryService) {
		this.backendDeliveryService = backendDeliveryService;
	}

	@Autowired
    public void setMessageErrorPersistenceService(DomibusConnectorMessageErrorPersistenceService messageErrorPersistenceService) {
        this.messageErrorPersistenceService = messageErrorPersistenceService;
    }

    @Override
	@StoreMessageExceptionIntoDatabase
	@MDC(name = LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_PROCESSOR_PROPERTY_NAME, value = GW_TO_BACKEND_MESSAGE_PROCESSOR)
	public void processMessage(DomibusConnectorMessage message) {
        SetMessageOnLoggingContext.putConnectorMessageIdOnMDC(message); //set message on logging context
		LOGGER.trace("#processMessage: start processing originalMessage [{}] with confirmations [{}]", message, message.getMessageConfirmations());
		
		createRelayREMMDEvidenceAndSendIt(message, true);
		
		
		try {
		    LOGGER.debug("#processMessage: call validateContainer");
			message = securityToolkit.validateContainer(message);
			//update originalMessage in database
			message = messagePersistenceService.mergeMessageWithDatabase(message);
		} catch (DomibusConnectorSecurityException e) {
			createNonDeliveryEvidenceAndSendIt(message);
			throw e;
		}
		
		if(isConnector2ConnectorTest(message)){
			// if it is a connector to connector test originalMessage defined by service and action, do NOT deliver originalMessage to the backend, but
			// only send a DELIVERY evidence back.
			LOGGER.info("#processMessage: Message [{}] is a connector to connector test originalMessage. \nIt will NOT be delivered to the backend!", message);
			createDeliveryEvidenceAndSendIt(message);
			LOGGER.info("#processMessage: Connector to Connector Test originalMessage [{}] is confirmed!", message);
		} else {
			try {
				backendDeliveryService.deliverMessageToBackend(message);
			} catch (Exception e) {
				createNonDeliveryEvidenceAndSendIt(message);
			}
		}

		LOGGER.info(BUSINESS_LOG, "Successfully processed originalMessage {} from GW to backend.", message.getConnectorMessageId());
	}
	
	private boolean isConnector2ConnectorTest(DomibusConnectorMessage message) {
		String connectorTestService = connectorTestConfigurationProperties.getService();
		String connectorTestAction = connectorTestConfigurationProperties.getAction();

		return (!StringUtils.isEmpty(connectorTestService) && message.getMessageDetails().getService().getService().equals(connectorTestService)) 
				&& (!StringUtils.isEmpty(connectorTestAction) && message.getMessageDetails().getAction().getAction().equals(connectorTestAction));
	}
	
	private void createDeliveryEvidenceAndSendIt(DomibusConnectorMessage originalMessage)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {

		CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper wrappedDeliveryEvidenceMsg = confirmationMessageBuilderFactory
				.createConfirmationMessageBuilder(originalMessage, DomibusConnectorEvidenceType.DELIVERY)
				.switchFromToParty()
				.build();

		wrappedDeliveryEvidenceMsg.persistEvidenceToMessage();

		DomibusConnectorMessage evidenceMessage = wrappedDeliveryEvidenceMsg.getEvidenceMessage();
		try {
			LOGGER.debug("Submitting messageConfirmation [{}] back to GW", evidenceMessage);
			gwSubmissionService.submitToGateway(evidenceMessage);
			LOGGER.info(BUSINESS_EVIDENCE_LOG, "[{}] confirmation for message [{}] successfully sent to gw", wrappedDeliveryEvidenceMsg.getEvidenceType(), originalMessage.getConnectorMessageId());
//			LOGGER.trace("Confirmation [{}] with sent successfully to gw, the content is:\n\n{}\n\n", messageConfirmation, new String(messageConfirmation.getEvidence(), "UTF-8"));

		} catch (Exception e) {
			//TODO: improve that!
			String error = String.format("Exception sending evidenceMessage [%s] of originalMessage with connectorMessageId [%s] back to gateway ",
					evidenceMessage,
					originalMessage.getConnectorMessageId());
			DomibusConnectorMessageException exception = DomibusConnectorMessageExceptionBuilder.createBuilder()
					.setMessage(originalMessage)
					.setText(error)
					.setSource(this.getClass())
					.setCause(e)
					.build();
			LOGGER.error("Exception occured while sending evidence msg back to GW", exception);
			DomibusConnectorMessageError messageError =
					DomibusConnectorMessageErrorBuilder.createBuilder()
							.setSource(this.getClass().getName())
							.setDetails(e.getStackTrace().toString())
							.setText(error)
							.build();
			messageErrorPersistenceService.persistMessageError(originalMessage.getConnectorMessageId(), messageError);
		}
		messagePersistenceService.confirmMessage(originalMessage);

	}
	
	private void createNonDeliveryEvidenceAndSendIt(DomibusConnectorMessage originalMessage)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {


		CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper wrappedDeliveryEvidenceMsg = confirmationMessageBuilderFactory
				.createConfirmationMessageBuilder(originalMessage, DomibusConnectorEvidenceType.NON_DELIVERY)
				.switchFromToParty()
				.build();

		wrappedDeliveryEvidenceMsg.persistEvidenceToMessage();

		sendEvidenceBackToGateway(wrappedDeliveryEvidenceMsg);


		messagePersistenceService.rejectMessage(originalMessage);
	}

	private void sendEvidenceBackToGateway(CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper wrappedDeliveryEvidenceMsg) {


		wrappedDeliveryEvidenceMsg.persistEvidenceToMessage();

		DomibusConnectorMessage evidenceMessage = wrappedDeliveryEvidenceMsg.getEvidenceMessage();


		try {
			LOGGER.debug("Submitting messageConfirmation [{}] back to GW", wrappedDeliveryEvidenceMsg.getMessageConfirmation());
			gwSubmissionService.submitToGateway(evidenceMessage);
			LOGGER.info(BUSINESS_EVIDENCE_LOG, "[{}] confirmation for message [{}] successfully sent to gw", wrappedDeliveryEvidenceMsg.getEvidenceType(), wrappedDeliveryEvidenceMsg.getCausedByConnectorMessageId());
//			LOGGER.trace("Confirmation [{}] with sent successfully to gw, the content is:\n\n{}\n\n", messageConfirmation, new String(messageConfirmation.getEvidence(), "UTF-8"));

		} catch (Exception e) {
			//TODO: improve that!
			String error = String.format("Exception sending evidenceMessage [%s] of originalMessage with connectorMessageId [%s] back to gateway ",
					evidenceMessage,
					wrappedDeliveryEvidenceMsg.getCausedByConnectorMessageId());
			DomibusConnectorMessageException exception = DomibusConnectorMessageExceptionBuilder.createBuilder()
					.setMessage(wrappedDeliveryEvidenceMsg.getOriginalMessage())
					.setText(error)
					.setSource(this.getClass())
					.setCause(e)
					.build();
			LOGGER.error("Exception occured", exception);
			DomibusConnectorMessageError messageError =
					DomibusConnectorMessageErrorBuilder.createBuilder()
							.setSource(this.getClass().getName())
							.setDetails(e.getStackTrace().toString())
							.setText(error)
							.build();
			messageErrorPersistenceService.persistMessageError(wrappedDeliveryEvidenceMsg.getCausedByConnectorMessageId(), messageError);
		}
	}

	private void createRelayREMMDEvidenceAndSendIt(DomibusConnectorMessage originalMessage, boolean isAcceptance)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {
		CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder wrappedMessageConfirmationBuilder = null;


		try {
			if(isAcceptance) {
			    LOGGER.trace("relay is acceptance, generating RELAY_REMMD_ACCEPTANCE");
                wrappedMessageConfirmationBuilder = confirmationMessageBuilderFactory.createConfirmationMessageBuilder(originalMessage, DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE);
            } else {
                LOGGER.trace("relay is denied, generating RELAY_REMMD_REJECTION");
                wrappedMessageConfirmationBuilder = confirmationMessageBuilderFactory.createConfirmationMessageBuilder(originalMessage, DomibusConnectorEvidenceType.RELAY_REMMD_REJECTION);
            }

			CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper wrappedEvidenceMessage = wrappedMessageConfirmationBuilder
					.switchFromToParty()
					.build();
			LOGGER.trace("generated confirmation is [{}]", wrappedEvidenceMessage.getMessageConfirmation());
            sendEvidenceBackToGateway(wrappedEvidenceMessage);

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
                            .setDetails(e.getStackTrace().toString())
                            .setText("Error creating RelayREMMD evidence for originalMessage!")
                        .build();
            messageErrorPersistenceService.persistMessageError(originalMessage.getConnectorMessageId(), messageError);
		}


		if (!isAcceptance) {
			LOGGER.warn(BUSINESS_LOG, "Message is not accepted!");
			messagePersistenceService.rejectMessage(originalMessage);
		}
	}

}
