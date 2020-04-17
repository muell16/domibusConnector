package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageExceptionBuilder;
import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.process.util.CreateConfirmationMessageBuilderFactoryImpl;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
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
                    createConfirmationMessageBuilderFactoryImpl.createConfirmationMessageBuilder(message, DomibusConnectorEvidenceType.SUBMISSION_REJECTION);

            rejectionMessage = confirmationMessageBuilder
                    .setRejectionReason(DomibusConnectorRejectionReason.OTHER)
                    .useNationalIdAsRefToMessageId()
                    .switchFromToParty()
                    .setDetails(errorMessage)
                    .build();

        } catch (Exception e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not build evidence of type SUBMISSION_REJECTION! ")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
        }
        try {
            rejectionMessage.persistEvidenceToMessage();
        } catch (Exception e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not persist evidence of type SUBMISSION_REJECTION to original message!")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
        }


        try {
            backendDeliveryService.deliverMessageToBackend(rejectionMessage.getEvidenceMessage());
            LOGGER.info("Setting originalMessage status to rejected");

        } catch (Exception e) {
            LOGGER.error("Exception occured while deliver evidence of of type SUBMISSION_REJECTION to backend!", e);
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not send evidence of type SUBMISSION_REJECTION to backend!")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
        }

    }

}
