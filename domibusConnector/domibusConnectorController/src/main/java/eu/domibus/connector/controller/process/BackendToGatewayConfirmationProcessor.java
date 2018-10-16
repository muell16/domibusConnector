package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.process.util.CreateConfirmationMessageService;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.service.*;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.MDCHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static eu.domibus.connector.tools.logging.LoggingMarker.BUSINESS_LOG;

/**
 * Takes an confirmation originalMessage from backend
 * and creates a new confirmation of the same confirmation type
 * and sends it to the gw
 *
 */
@Component(BackendToGatewayConfirmationProcessor.BACKEND_TO_GW_CONFIRMATION_PROCESSOR_BEAN_NAME)
public class BackendToGatewayConfirmationProcessor implements DomibusConnectorMessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BackendToGatewayConfirmationProcessor.class);

	public static final String BACKEND_TO_GW_CONFIRMATION_PROCESSOR_BEAN_NAME = "BackendToGatewayConfirmationProcessor";

    private CreateConfirmationMessageService confirmationMessageService;
    private DomibusConnectorMessagePersistenceService messagePersistenceService;
	private DomibusConnectorGatewaySubmissionService gwSubmissionService;

    //setter
    @Autowired
    public void setConfirmationMessageService(CreateConfirmationMessageService confirmationMessageService) {
        this.confirmationMessageService = confirmationMessageService;
    }

    @Autowired
    public void setMessagePersistenceService(DomibusConnectorMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    @Autowired
    public void setGwSubmissionService(DomibusConnectorGatewaySubmissionService gwSubmissionService) {
        this.gwSubmissionService = gwSubmissionService;
    }

    @Override
    @Transactional(propagation=Propagation.NEVER)
    @StoreMessageExceptionIntoDatabase
    @MDC(name = LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_PROCESSOR_PROPERTY_NAME, value = BACKEND_TO_GW_CONFIRMATION_PROCESSOR_BEAN_NAME)
	public void processMessage(DomibusConnectorMessage message) {
        if (!DomainModelHelper.isEvidenceMessage(message)) {
            throw new IllegalArgumentException("The originalMessage is not an evidence originalMessage!");
        }

		String refToOriginalMessage = message.getMessageDetails().getRefToMessageId();
        LOGGER.debug("#processMessage: refToMessageId is [{}]", refToOriginalMessage);
        DomibusConnectorEvidenceType evidenceType = DomainModelHelper.getEvidenceTypeOfEvidenceMessage(message);

        DomibusConnectorMessage originalMessage = messagePersistenceService.findMessageByEbmsId(refToOriginalMessage);
        LOGGER.debug("#processMessage: processing evidence [{}] of original message [{}]", evidenceType, originalMessage);
        if (originalMessage == null) {
            throw new RuntimeException(String.format("No message for refToMessageId [%s] found!", refToOriginalMessage));
        }

        CreateConfirmationMessageService.ConfirmationMessageBuilder confirmationMessageBuilder
                = confirmationMessageService.createConfirmationMessageBuilder(originalMessage, evidenceType);

        CreateConfirmationMessageService.DomibusConnectorMessageConfirmationWrapper wrappedConfirmation =
                confirmationMessageBuilder
                .switchFromToParty()
                .build();

        wrappedConfirmation.persistEvidenceToMessage();
        DomibusConnectorMessageConfirmation confirmation = wrappedConfirmation.getMessageConfirmation();
        originalMessage.addConfirmation(confirmation);

        DomibusConnectorMessage evidenceMessage = wrappedConfirmation.getEvidenceMessage();

        submitToGateway(evidenceMessage, originalMessage);
        setDeliveredToGateway(evidenceMessage);


        if (!messagePersistenceService.checkMessageConfirmed(originalMessage)) {
            messagePersistenceService.confirmMessage(originalMessage);
        }


        LOGGER.info(BUSINESS_LOG, "Successfully sent evidence of type {} for originalMessage {} to gateway.", confirmation.getEvidenceType(), originalMessage);
	}

	private void submitToGateway(DomibusConnectorMessage evidenceMessage, DomibusConnectorMessage originalMessage) {
        try {
            gwSubmissionService.submitToGateway(evidenceMessage);
        } catch (DomibusConnectorGatewaySubmissionException gwse) {
            DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(originalMessage)
                    .setText("Could not send Evidence Message to Gateway!")
                    .setSource(this.getClass())
                    .setCause(gwse)
                    .buildAndThrow();
        }
    }

    private void setDeliveredToGateway(DomibusConnectorMessage evidenceMessage) {
        try {
            messagePersistenceService.setDeliveredToGateway(evidenceMessage);
        } catch(PersistenceException persistenceException) {
            LOGGER.error("persistence Exception occured", persistenceException);
        }
    }


}
