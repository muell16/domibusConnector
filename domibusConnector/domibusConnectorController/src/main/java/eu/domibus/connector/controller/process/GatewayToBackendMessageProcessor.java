package eu.domibus.connector.controller.process;


import javax.annotation.Nonnull;


import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
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
	private DomibusConnectorMessageIdGenerator messageIdGenerator;
	private DomibusConnectorEvidencesToolkit evidencesToolkit;
	private DomibusConnectorSecurityToolkit securityToolkit;
	private DomibusConnectorBackendDeliveryService backendDeliveryService;
	private DomibusConnectorActionPersistenceService actionPersistenceService;
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
	public void setMessageIdGenerator(DomibusConnectorMessageIdGenerator messageIdGenerator) {
		this.messageIdGenerator = messageIdGenerator;
	}

	@Autowired
	public void setEvidencesToolkit(DomibusConnectorEvidencesToolkit evidencesToolkit) {
		this.evidencesToolkit = evidencesToolkit;
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
	public void setActionPersistenceService(DomibusConnectorActionPersistenceService actionPersistenceService) {
		this.actionPersistenceService = actionPersistenceService;
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
		}else{
			backendDeliveryService.deliverMessageToBackend(message);
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

		DomibusConnectorMessageConfirmation delivery = null;
		try {
			delivery = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.DELIVERY, originalMessage, null, null);
			originalMessage.addConfirmation(delivery);
		} catch (DomibusConnectorEvidencesToolkitException e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(originalMessage)
                    .setText("Error creating Delivery evidence for originalMessage!")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
		}

		DomibusConnectorAction action = actionPersistenceService.getDeliveryNonDeliveryToRecipientAction();

		sendEvidenceBackToGateway(originalMessage, action, delivery);

		messagePersistenceService.confirmMessage(originalMessage);
	}
	
	private void createNonDeliveryEvidenceAndSendIt(DomibusConnectorMessage originalMessage)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {

		DomibusConnectorMessageConfirmation nonDelivery = null;
		try {
			nonDelivery = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.NON_DELIVERY, originalMessage,DomibusConnectorRejectionReason.OTHER, null);
		} catch (DomibusConnectorEvidencesToolkitException e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(originalMessage)
                    .setText("Error creating NonDelivery evidence for originalMessage!")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
		}

		DomibusConnectorAction action = actionPersistenceService.getDeliveryNonDeliveryToRecipientAction();

		sendEvidenceBackToGateway(originalMessage, action, nonDelivery);

		messagePersistenceService.rejectMessage(originalMessage);
	}
	
	private void createRelayREMMDEvidenceAndSendIt(DomibusConnectorMessage originalMessage, boolean isAcceptance)
			throws DomibusConnectorControllerException, DomibusConnectorMessageException {
		DomibusConnectorMessageConfirmation messageConfirmation = null;
        DomibusConnectorAction action = actionPersistenceService.getRelayREMMDAcceptanceRejectionAction();

		try {
			if(isAcceptance) {
			    LOGGER.trace("relay is acceptance, generating RELAY_REMMD_ACCEPTANCE");
                messageConfirmation = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE, originalMessage, null, null);
            } else {
                LOGGER.trace("relay is denied, generating RELAY_REMMD_REJECTION");
                messageConfirmation = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.RELAY_REMMD_REJECTION, originalMessage, DomibusConnectorRejectionReason.OTHER, null);
            }
            LOGGER.trace("generated confirmation is [{}]", messageConfirmation);
            sendEvidenceBackToGateway(originalMessage, action, messageConfirmation);

		} catch (DomibusConnectorEvidencesToolkitException e) {
			DomibusConnectorMessageException evidenceBuildFailed = DomibusConnectorMessageExceptionBuilder.createBuilder()
					.setMessage(originalMessage)
					.setText("Error creating RelayREMMD evidence for originalMessage!")
					.setSource(this.getClass())
					.setCause(e)
					.build();
            //TODO: improve that!
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
	
	private void sendEvidenceBackToGateway(DomibusConnectorMessage originalMessage, DomibusConnectorAction action,
										   @Nonnull DomibusConnectorMessageConfirmation messageConfirmation) throws DomibusConnectorControllerException,
	DomibusConnectorMessageException {
        if (messageConfirmation == null) {
            throw new IllegalArgumentException("messageConfirmation is not allowed to be null!");
        }

		originalMessage.addConfirmation(messageConfirmation);
//		evidencePersistenceService.persistEvidenceForMessageIntoDatabase(originalMessage, messageConfirmation);

		DomibusConnectorMessageDetails originalDetails = originalMessage.getMessageDetails();
		DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
		BeanUtils.copyProperties(originalDetails, details);

		details.setFromParty(originalDetails.getToParty());
		details.setToParty(originalDetails.getFromParty());
		details.setRefToMessageId(originalMessage.getMessageDetails().getEbmsMessageId());
		details.setAction(action);
		details.setCausedBy(originalMessage.getConnectorMessageId());

		DomibusConnectorMessage evidenceMessage = new DomibusConnectorMessage(details, messageConfirmation);

		evidenceMessage.setConnectorMessageId(messageIdGenerator.generateDomibusConnectorMessageId());
		messagePersistenceService.persistMessageIntoDatabase(evidenceMessage, DomibusConnectorMessageDirection.CONNECTOR_TO_GATEWAY);

		
        try {
        	LOGGER.debug("Submitting messageConfirmation [{}] back to GW", messageConfirmation);
            gwSubmissionService.submitToGateway(evidenceMessage);
            LOGGER.info(BUSINESS_EVIDENCE_LOG, "[{}] confirmation for message [{}] successfully sent to gw", messageConfirmation.getEvidenceType(), originalMessage.getConnectorMessageId());
            LOGGER.trace("Confirmation [{}] with sent successfully to gw, the content is:\n\n{}\n\n", messageConfirmation, new String(messageConfirmation.getEvidence(), "UTF-8"));

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
            LOGGER.error("Exception occured", exception);
            DomibusConnectorMessageError messageError =
                    DomibusConnectorMessageErrorBuilder.createBuilder()
                            .setSource(this.getClass().getName())
                            .setDetails(e.getStackTrace().toString())
                            .setText(error)
                            .build();
            messageErrorPersistenceService.persistMessageError(originalMessage.getConnectorMessageId(), messageError);
        }


//        try {
//            evidencePersistenceService.setEvidenceDeliveredToGateway(originalMessage, messageConfirmation);
//        } catch (PersistenceException ex) {
//        	String error = String.format("Confirmation [%s] could not set to 'delivered' to GW", messageConfirmation);
//            LOGGER.error(error, ex);
//            //TODO: further exception handling!
//        }
	}

}
