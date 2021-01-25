package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageTransportException;
import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.process.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * This MessageTransportExceptionProcessor generates for the message a NON_DELIVERY confirmation/evidence
 * and sends this evidence to the gateway/the sending party
 * The message is also marked as rejected in the database
 *
 * It handles the Use Case of rejecting a messsage
 *
 */
@Component(DomibusConnectorDeliveryRejectionProcessor.DELIVERY_REJECTION_PROCESSOR_BEAN_NAME)
public class DomibusConnectorDeliveryRejectionProcessor implements DomibusConnectorMessageTransportExceptionProcessor {

    public static final String DELIVERY_REJECTION_PROCESSOR_BEAN_NAME = "DeliveryRejectionProcessor";

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorDeliveryRejectionProcessor.class);


    @Autowired
    private DCMessagePersistenceService messagePersistenceService;

    @Autowired
    CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactoryImpl;

    @Autowired
    private DomibusConnectorGatewaySubmissionService gwSubmissionService;

    //setter
    public void setMessagePersistenceService(DCMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    public void setCreateConfirmationMessageBuilderFactoryImpl(CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactoryImpl) {
        this.createConfirmationMessageBuilderFactoryImpl = createConfirmationMessageBuilderFactoryImpl;
    }

    public void setGwSubmissionService(DomibusConnectorGatewaySubmissionService gwSubmissionService) {
        this.gwSubmissionService = gwSubmissionService;
    }

    @Override
    @StoreMessageExceptionIntoDatabase
    @Transactional
    @MDC(name = LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_PROCESSOR_PROPERTY_NAME, value = DELIVERY_REJECTION_PROCESSOR_BEAN_NAME)
    public void processMessageTransportException(DomibusConnectorMessageTransportException messageTransportException) {
        DomibusConnectorMessage originalMessage = messageTransportException.getConnectorMessage();
        if (DomainModelHelper.isEvidenceMessage(originalMessage)) {
            LOGGER.debug("message transport exception is ignored for evidence message!");
            return;
        }

        CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder confirmationMessageBuilder = createConfirmationMessageBuilderFactoryImpl.createConfirmationMessageBuilder(originalMessage, DomibusConnectorEvidenceType.NON_DELIVERY);

        CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper evidenceMessage = confirmationMessageBuilder
                .setRejectionReason(DomibusConnectorRejectionReason.OTHER)
                .switchFromToParty()
                .withDirection(MessageTargetSource.GATEWAY)
                .build();


        //persist evidence
        try {
            evidenceMessage.persistEvidenceMessageAndPersistEvidenceToBusinessMessage();
        } catch (Exception e) {
            //ignore evidence persisting errors...
            LOGGER.warn("An error occured while saving NON_DELIVERY to business message");
        }

        try {
            //send evidence to GW
            gwSubmissionService.submitToGateway(evidenceMessage.getEvidenceMessage());
        } catch (DomibusConnectorGatewaySubmissionException e) {
            //TODO: only log...no further processing...
            LOGGER.error("An error occured while sending NON_DELIVERY evidence back to GW", e);
        }

        //set message to rejected!
        messagePersistenceService.rejectMessage(originalMessage);
        LOGGER.info(LoggingMarker.BUSINESS_LOG, "Message with connector id [{}] has been marked as rejected in db!", originalMessage.getConnectorMessageId());
    }

}
