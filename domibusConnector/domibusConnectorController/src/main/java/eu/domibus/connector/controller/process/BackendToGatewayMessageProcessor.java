package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.process.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.service.*;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
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

	private DomibusConnectorMessagePersistenceService messagePersistenceService;
	private DomibusConnectorGatewaySubmissionService gwSubmissionService;
	private DomibusConnectorSecurityToolkit securityToolkit;
	private DomibusConnectorBackendDeliveryService backendDeliveryService;
	private DomibusConnectorPersistAllBigDataOfMessageService bigDataPersistenceService;

	@Autowired
	private CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactoryImpl;

	@Autowired
	private CreateSubmissionRejectionAndReturnItService createSubmissionRejectionAndReturnItService;

	public void setCreateConfirmationMessageBuilderFactoryImpl(CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactoryImpl) {
		this.createConfirmationMessageBuilderFactoryImpl = createConfirmationMessageBuilderFactoryImpl;
	}

	public void setCreateSubmissionRejectionAndReturnItService(CreateSubmissionRejectionAndReturnItService createSubmissionRejectionAndReturnItService) {
		this.createSubmissionRejectionAndReturnItService = createSubmissionRejectionAndReturnItService;
	}

	@Autowired
    public void setMessagePersistenceService(DomibusConnectorMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

	@Autowired
	public void setBigDataPersistenceService(DomibusConnectorPersistAllBigDataOfMessageService bigDataPersistenceService) {
		this.bigDataPersistenceService = bigDataPersistenceService;
	}

    @Autowired
    public void setGwSubmissionService(DomibusConnectorGatewaySubmissionService gwSubmissionService) {
        this.gwSubmissionService = gwSubmissionService;
    }

    @Autowired
    public void setSecurityToolkit(DomibusConnectorSecurityToolkit securityToolkit) {
        this.securityToolkit = securityToolkit;
    }

    @Autowired
    public void setBackendDeliveryService(DomibusConnectorBackendDeliveryService backendDeliveryService) {
        this.backendDeliveryService = backendDeliveryService;
    }

    @Override
    @Transactional(propagation=Propagation.NEVER)
    @StoreMessageExceptionIntoDatabase
    @MDC(name = LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_PROCESSOR_PROPERTY_NAME, value = BACKEND_TO_GW_MESSAGE_PROCESSOR_BEAN_NAME)
	public void processMessage(DomibusConnectorMessage message) {

        try {
			securityToolkit.buildContainer(message);
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
		}

		DomibusConnectorMessage submissionAcceptanceConfirmationMessage = null;
		try {


			CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder confirmationMessageBuilder = this.createConfirmationMessageBuilderFactoryImpl.createConfirmationMessageBuilder(message, DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE);
			CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper confirmationMessage = confirmationMessageBuilder
					.useNationalIdAsRefToMessageId()
					.build();
			confirmationMessage.persistEvidenceToMessage();

			submissionAcceptanceConfirmationMessage = confirmationMessage.getEvidenceMessage();
		} catch (DomibusConnectorEvidencesToolkitException ete) {
			//TODO: better logging....
		    LOGGER.error("Could not generate evidence [{}] for originalMessage [{}]!", DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, message);
			createSubmissionRejectionAndReturnItService.createSubmissionRejectionAndReturnIt(message, ete.getMessage());
			messagePersistenceService.rejectMessage(message);
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not generate evidence submission acceptance! ")
                    .setSource(this.getClass())
                    .setCause(ete)
                    .buildAndThrow();
		}

		try {
			message = bigDataPersistenceService.loadAllBigFilesFromMessage(message);
			gwSubmissionService.submitToGateway(message);
		} catch (DomibusConnectorGatewaySubmissionException e) {
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
//		messagePersistenceService.mergeMessageWithDatabase(message);
//		messagePersistenceService.setDeliveredToGateway(message);

        //also send evidence back to backend client:

		LOGGER.trace("#processMessage: persist evidence originalMessage [{}] into database", submissionAcceptanceConfirmationMessage);
        messagePersistenceService.persistMessageIntoDatabase(submissionAcceptanceConfirmationMessage, DomibusConnectorMessageDirection.CONNECTOR_TO_BACKEND);
		backendDeliveryService.deliverMessageToBackend(submissionAcceptanceConfirmationMessage);

		LOGGER.info("Successfully sent originalMessage {} to gateway.", message);

	}



}
