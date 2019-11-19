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

//    @Autowired
//    private DomibusConnectorEvidencesToolkit evidencesToolkit;
//    @Autowired
//    private DomibusConnectorEvidencePersistenceService evidencePersistenceService;
    @Autowired
    private DomibusConnectorMessagePersistenceService messagePersistenceService;
    @Autowired
    private DomibusConnectorBackendDeliveryService backendDeliveryService;
    @Autowired
    private CreateConfirmationMessageBuilderFactoryImpl createConfirmationMessageBuilderFactoryImpl;

    public void setMessagePersistenceService(DomibusConnectorMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

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
                    .setDetails(errorMessage)
                    .build();

//            // immediately persist new evidence into database
//            evidencePersistenceService.persistEvidenceForMessageIntoDatabase(message,
//                    rejectionMessage.getMessageConfirmation(),
//                    new DomibusConnectorMessage.DomibusConnectorMessageId(message.getConnectorMessageId()));
        } catch (Exception e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not build evidence of type SUBMISSION_REJECTION! ")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
        }

        rejectionMessage.persistEvidenceToMessage();



        try {
            backendDeliveryService.deliverMessageToBackend(rejectionMessage.getEvidenceMessage());
            LOGGER.info("Setting originalMessage confirmation [{}] as delivered to national system!", rejectionMessage.getMessageConfirmation().getEvidenceType());
            LOGGER.info("Setting originalMessage status to rejected");
//            messagePersistenceService.rejectMessage(message);

        } catch (PersistenceException persistenceException) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not set evidence of type SUBMISSION_REJECTION as delivered!")
                    .setSource(this.getClass())
                    .setCause(persistenceException)
                    .build();

        } catch (Exception e) {
            throw DomibusConnectorMessageExceptionBuilder.createBuilder()
                    .setMessage(message)
                    .setText("Could not set evidence of type SUBMISSION_REJECTION as delivered!")
                    .setSource(this.getClass())
                    .setCause(e)
                    .build();
        }

    }

}
