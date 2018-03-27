package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.persistence.service.DomibusConnectorActionPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateConfirmationMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateConfirmationMessageService.class);

    DomibusConnectorEvidencesToolkit evidencesToolkit;
    DomibusConnectorEvidencePersistenceService evidencePersistenceService;
    private DomibusConnectorActionPersistenceService setActionPersistenceService;
    private DomibusConnectorMessageIdGenerator messageIdGenerator;

    @Autowired
    public void setEvidencesToolkit(DomibusConnectorEvidencesToolkit evidencesToolkit) {
        this.evidencesToolkit = evidencesToolkit;
    }

    @Autowired
    public void setActionPersistenceService(DomibusConnectorActionPersistenceService actionPersistenceService) {
        this.setActionPersistenceService = actionPersistenceService;
    }

    @Autowired
    public void setEvidencePersistenceService(DomibusConnectorEvidencePersistenceService evidencePersistenceService) {
        this.evidencePersistenceService = evidencePersistenceService;
    }

    @Autowired
    public void setMessageIdGenerator(DomibusConnectorMessageIdGenerator idGenerator) {
        this.messageIdGenerator = idGenerator;
    }

    public ConfirmationMessageBuilder createConfirmationMessageBuilder(DomibusConnectorMessage message, DomibusConnectorEvidenceType evidenceType) {
        ConfirmationMessageBuilder confirmationMessageBuilder = new ConfirmationMessageBuilder()
                .setMessage(message)
                .setEvidenceType(evidenceType);

        if (evidenceType.equals(DomibusConnectorEvidenceType.DELIVERY ) ||
                evidenceType.equals(DomibusConnectorEvidenceType.NON_DELIVERY)) {
            confirmationMessageBuilder.setAction(setActionPersistenceService.getDeliveryNonDeliveryToRecipientAction());
        }

        //TODO: other evidenceTypes...

        return confirmationMessageBuilder;
    }


    public class ConfirmationMessageBuilder {
        DomibusConnectorMessage message;
        DomibusConnectorEvidenceType evidenceType;
        DomibusConnectorRejectionReason rejectionReason;
        String details;
        private DomibusConnectorAction action;
        private DomibusConnectorMessageConfirmation messageConfirmation;
        private DomibusConnectorMessage evidenceMessage = null;

        public ConfirmationMessageBuilder setMessage(DomibusConnectorMessage message) {
            this.message = message;
            return this;
        }

        public ConfirmationMessageBuilder setEvidenceType(DomibusConnectorEvidenceType evidenceType) {
            this.evidenceType = evidenceType;
            return this;
        }

        public ConfirmationMessageBuilder setRejectionReason(DomibusConnectorRejectionReason rejectionReason) {
            this.rejectionReason = rejectionReason;
            return this;
        }

        public ConfirmationMessageBuilder setDetails(String details) {
            this.details = details;
            return this;
        }

        public ConfirmationMessageBuilder setAction(DomibusConnectorAction action) {
            this.action = action;
            return this;
        }

        private void internalBuild() {
            if (this.evidenceMessage != null) {
                return;
            }
            try {
                this.messageConfirmation = evidencesToolkit.createEvidence(evidenceType, message, rejectionReason, details);
                message.addConfirmation(messageConfirmation);

                DomibusConnectorMessageDetails originalDetails = message.getMessageDetails();
                DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
                BeanUtils.copyProperties(originalDetails, details);

                details.setFromParty(originalDetails.getToParty());
                details.setToParty(originalDetails.getFromParty());
                details.setRefToMessageId(message.getMessageDetails().getEbmsMessageId());
                details.setAction(action);

                this.evidenceMessage = new DomibusConnectorMessage(details, messageConfirmation);
                evidenceMessage.setConnectorMessageId(messageIdGenerator.generateDomibusConnectorMessageId());

            } catch (DomibusConnectorEvidencesToolkitException e) {
                LOGGER.error("A Exception occured while generating evdince of type [{}]", evidenceType);
                throw new RuntimeException(e);
            }
        }


        public DomibusConnectorMessage buildWithoutSave() {
            internalBuild();
            return this.evidenceMessage;
        }

        public DomibusConnectorMessage buildAndSaveMessage() {
            internalBuild();
            evidencePersistenceService.persistEvidenceForMessageIntoDatabase(message, messageConfirmation);
            return this.evidenceMessage;
        }
    }
}
