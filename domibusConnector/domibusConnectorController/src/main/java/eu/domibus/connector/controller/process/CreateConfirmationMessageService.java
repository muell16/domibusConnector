package eu.domibus.connector.controller.process;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
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

    @Autowired
    public void setEvidencesToolkit(DomibusConnectorEvidencesToolkit evidencesToolkit) {
        this.evidencesToolkit = evidencesToolkit;
    }

    @Autowired
    public void setEvidencePersistenceService(DomibusConnectorEvidencePersistenceService evidencePersistenceService) {
        this.evidencePersistenceService = evidencePersistenceService;
    }

    public ConfirmationMessageBuilder createConfirmationMessageBuilder(DomibusConnectorMessage message, DomibusConnectorEvidenceType evidenceType) {
        return new ConfirmationMessageBuilder();
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

                this.evidenceMessage = new DomibusConnectorMessage(details, messageConfirmation);;

            } catch (DomibusConnectorEvidencesToolkitException e) {
                LOGGER.error("A Exception occured while generating evdince of type [{}]", evidenceType);
                throw new RuntimeException(e);
            }
        }


        public DomibusConnectorMessage build() {
            internalBuild();
            return this.evidenceMessage;
        }

        public ConfirmationMessageBuilder save() {
            internalBuild();
            evidencePersistenceService.persistEvidenceForMessageIntoDatabase(message, messageConfirmation);
            return this;
        }
    }
}
