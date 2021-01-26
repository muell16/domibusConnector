package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.DomibusConnectorBackendException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.process.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreateSubmissionRejectionAndReturnItService {

    private final static Logger LOGGER = LogManager.getLogger(CreateSubmissionRejectionAndReturnItService.class);

    @Autowired
    private DomibusConnectorBackendDeliveryService backendDeliveryService;
    @Autowired
    private CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactoryImpl;


    public void setBackendDeliveryService(DomibusConnectorBackendDeliveryService backendDeliveryService) {
        this.backendDeliveryService = backendDeliveryService;
    }

    public void setCreateConfirmationMessageBuilderFactoryImpl(CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactoryImpl) {
        this.createConfirmationMessageBuilderFactoryImpl = createConfirmationMessageBuilderFactoryImpl;
    }

    @StoreMessageExceptionIntoDatabase(passException = false)
    public void createSubmissionRejectionAndReturnIt(DomibusConnectorMessage message, String errorMessage){
        LOGGER.debug("#createSubmissionRejectionAndReturnIt: with error [{}]", errorMessage);

        CreateConfirmationMessageBuilderFactoryImpl.DomibusConnectorMessageConfirmationWrapper rejectionMessage = null;
        try {
            CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder confirmationMessageBuilder =
                    createConfirmationMessageBuilderFactoryImpl.createConfirmationMessageBuilderFromBusinessMessage(message, DomibusConnectorEvidenceType.SUBMISSION_REJECTION);

            rejectionMessage = confirmationMessageBuilder
                    .setRejectionReason(DomibusConnectorRejectionReason.OTHER)
                    .switchFromToAttributes()
                    .setDetails(errorMessage)
                    .withDirection(MessageTargetSource.BACKEND)
                    .build();

            rejectionMessage.persistMessage();

            backendDeliveryService.deliverMessageToBackend(rejectionMessage.getEvidenceMessage());
            LOGGER.info("Setting originalMessage status to rejected");

        } catch (DomibusConnectorBackendException e) {
            LOGGER.error("Exception occured while deliver evidence of of type SUBMISSION_REJECTION to backend!", e);
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not send evidence of type SUBMISSION_REJECTION to backend!")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
        } catch (Exception e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not build evidence of type SUBMISSION_REJECTION! ")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
        }

    }

}
