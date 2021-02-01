package eu.domibus.connector.controller.process;


import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.process.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.controller.spring.ConnectorTestConfigurationProperties;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageErrorBuilder;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.service.*;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.SetMessageOnLoggingContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.security.DomibusConnectorSecurityToolkit;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

import static eu.domibus.connector.tools.logging.LoggingMarker.BUSINESS_LOG;

@Component(GatewayToBackendMessageProcessor.GW_TO_BACKEND_MESSAGE_PROCESSOR)
public class GatewayToBackendMessageProcessor implements DomibusConnectorMessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayToBackendMessageProcessor.class);

	public static final String GW_TO_BACKEND_MESSAGE_PROCESSOR = "GatewayToBackendMessageProcessor";

	private final ConnectorTestConfigurationProperties connectorTestConfigurationProperties;
	private final DCMessagePersistenceService messagePersistenceService;
	private final CreateConfirmationMessageBuilderFactoryImpl confirmationMessageBuilderFactory;
	private final DomibusConnectorSecurityToolkit securityToolkit;
	private final DomibusConnectorMessageErrorPersistenceService messageErrorPersistenceService;
	private final MessageConfirmationProcessor messageConfirmationProcessor;
	private final SubmitMessageToLinkModuleService submitMessageToLinkModuleService;

	public GatewayToBackendMessageProcessor(ConnectorTestConfigurationProperties connectorTestConfigurationProperties,
											DCMessagePersistenceService messagePersistenceService,
											CreateConfirmationMessageBuilderFactoryImpl confirmationMessageBuilderFactory,
											DomibusConnectorSecurityToolkit securityToolkit,
											DomibusConnectorMessageErrorPersistenceService messageErrorPersistenceService,
											MessageConfirmationProcessor messageConfirmationProcessor,
											SubmitMessageToLinkModuleService submitMessageToLinkModuleService) {
		this.connectorTestConfigurationProperties = connectorTestConfigurationProperties;
		this.messagePersistenceService = messagePersistenceService;
		this.confirmationMessageBuilderFactory = confirmationMessageBuilderFactory;
		this.securityToolkit = securityToolkit;
		this.messageErrorPersistenceService = messageErrorPersistenceService;
		this.messageConfirmationProcessor = messageConfirmationProcessor;
		this.submitMessageToLinkModuleService = submitMessageToLinkModuleService;
	}


    @Override
	@StoreMessageExceptionIntoDatabase
	@MDC(name = LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_PROCESSOR_PROPERTY_NAME, value = GW_TO_BACKEND_MESSAGE_PROCESSOR)
	public void processMessage(final DomibusConnectorMessage incomingMessage) {
		try {
        	SetMessageOnLoggingContext.putConnectorMessageIdOnMDC(incomingMessage); //set message on logging context
			LOGGER.trace("#processMessage: start processing originalMessage [{}] with confirmations [{}]", incomingMessage, incomingMessage.getTransportedMessageConfirmations());

			//process every transported confirmation
			incomingMessage.getTransportedMessageConfirmations().stream()
					.forEach(c -> messageConfirmationProcessor.processConfirmationForMessage(incomingMessage, c));

			createRelayREMMDEvidenceAndSendIt(incomingMessage, true);
		

		    LOGGER.debug("#processMessage: call validateContainer");
			DomibusConnectorMessage validatedMessage = securityToolkit.validateContainer(incomingMessage);
			//update originalMessage in database
			validatedMessage = messagePersistenceService.mergeMessageWithDatabase(validatedMessage);

		
			if(isConnector2ConnectorTest(validatedMessage)){
				// if it is a connector to connector test originalMessage defined by service and action, do NOT deliver originalMessage to the backend, but
				// only send a DELIVERY evidence back.
				LOGGER.info("#processMessage: Message [{}] is a connector to connector test originalMessage. \nIt will NOT be delivered to the backend!", validatedMessage);
				createDeliveryEvidenceAndSendIt(validatedMessage);
				LOGGER.info("#processMessage: Connector to Connector Test originalMessage [{}] is confirmed!", validatedMessage);
			} else {
				try {
					submitMessageToLinkModuleService.submitMessage(validatedMessage);
				} catch (Exception e) {
					createNonDeliveryEvidenceAndSendIt(validatedMessage);
				}
			}

			LOGGER.info(BUSINESS_LOG, "Successfully processed originalMessage {} from GW to backend.", validatedMessage.getConnectorMessageIdAsString());
		} catch (DomibusConnectorSecurityException e) {
			createNonDeliveryEvidenceAndSendIt(incomingMessage);
			throw e;
		}
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
				.createConfirmationMessageBuilderFromBusinessMessage(originalMessage, DomibusConnectorEvidenceType.DELIVERY)
				.switchFromToAttributes()
				.withDirection(MessageTargetSource.GATEWAY)
				.build();

		wrappedDeliveryEvidenceMsg.persistMessage();
		messageConfirmationProcessor.processConfirmationForMessageAndSendBack(originalMessage, wrappedDeliveryEvidenceMsg.getMessageConfirmation());

	}
	
	private void createNonDeliveryEvidenceAndSendIt(DomibusConnectorMessage originalMessage)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {


		CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper wrappedDeliveryEvidenceMsg = confirmationMessageBuilderFactory
				.createConfirmationMessageBuilderFromBusinessMessage(originalMessage, DomibusConnectorEvidenceType.NON_DELIVERY)
				.switchFromToAttributes()
				.withDirection(MessageTargetSource.GATEWAY)
				.build();
		originalMessage.addTransportedMessageConfirmation(wrappedDeliveryEvidenceMsg.getMessageConfirmation());
		messageConfirmationProcessor.processConfirmationForMessageAndSendBack(originalMessage, wrappedDeliveryEvidenceMsg.getMessageConfirmation());
	}

	private void createRelayREMMDEvidenceAndSendIt(DomibusConnectorMessage originalMessage, boolean isAcceptance)
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

			messageConfirmationProcessor.processConfirmationForMessageAndSendBack(originalMessage, wrappedEvidenceMessage.getMessageConfirmation());

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

	}

}
