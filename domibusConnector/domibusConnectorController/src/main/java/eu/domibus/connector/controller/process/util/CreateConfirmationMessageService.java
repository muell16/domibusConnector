package eu.domibus.connector.controller.process.util;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.persistence.service.DomibusConnectorActionPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.style.ToStringCreator;
import org.springframework.stereotype.Component;

@Component
public class CreateConfirmationMessageService implements ConfirmationMessageBuilderFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateConfirmationMessageService.class);

    DomibusConnectorEvidencesToolkit evidencesToolkit;
    DomibusConnectorEvidencePersistenceService evidencePersistenceService;
    private DomibusConnectorActionPersistenceService actionPersistenceService;
    private DomibusConnectorMessageIdGenerator messageIdGenerator;

    @Autowired
    public void setEvidencesToolkit(DomibusConnectorEvidencesToolkit evidencesToolkit) {
        this.evidencesToolkit = evidencesToolkit;
    }

    @Autowired
    public void setActionPersistenceService(DomibusConnectorActionPersistenceService actionPersistenceService) {
        this.actionPersistenceService = actionPersistenceService;
    }

    @Autowired
    public void setEvidencePersistenceService(DomibusConnectorEvidencePersistenceService evidencePersistenceService) {
        this.evidencePersistenceService = evidencePersistenceService;
    }

    @Autowired
    public void setMessageIdGenerator(DomibusConnectorMessageIdGenerator idGenerator) {
        this.messageIdGenerator = idGenerator;
    }

    @Override
    public ConfirmationMessageBuilder createConfirmationMessageBuilder(DomibusConnectorMessage message, DomibusConnectorEvidenceType evidenceType) {
        ConfirmationMessageBuilder confirmationMessageBuilder = new ConfirmationMessageBuilder(message, evidenceType);

        DomibusConnectorAction messageAction = createEvidenceAction(evidenceType);
        confirmationMessageBuilder.setAction(messageAction);

        return confirmationMessageBuilder;
    }

    @Override
    public DomibusConnectorAction createEvidenceAction(DomibusConnectorEvidenceType type) throws DomibusConnectorControllerException {
        switch (type) {
            case DELIVERY:
                return actionPersistenceService.getDeliveryNonDeliveryToRecipientAction();
            case NON_DELIVERY:
                return actionPersistenceService.getDeliveryNonDeliveryToRecipientAction();
            case RETRIEVAL:
                return actionPersistenceService.getRetrievalNonRetrievalToRecipientAction();
            case NON_RETRIEVAL:
                return actionPersistenceService.getRetrievalNonRetrievalToRecipientAction();
            default:
                throw new DomibusConnectorControllerException("Illegal Evidence type " + type + "! No Action found!");
        }
    }


    public class DomibusConnectorMessageConfirmationWrapper {

        public DomibusConnectorMessageConfirmation messageConfirmation;
        public DomibusConnectorMessage originalMesssage;
        public DomibusConnectorMessage evidenceMessage;

        private DomibusConnectorMessageConfirmationWrapper(){}

        private void setMessageConfirmation(DomibusConnectorMessageConfirmation messageConfirmation) {
            this.messageConfirmation = messageConfirmation;
        }

        private void setOriginalMesssage(DomibusConnectorMessage originalMesssage) {
            this.originalMesssage = originalMesssage;
        }

        private void setEvidenceMessage(DomibusConnectorMessage evidenceMessage) {
            this.evidenceMessage = evidenceMessage;
        }

        public DomibusConnectorMessage getEvidenceMessage() {
            return this.evidenceMessage;
        }

        public DomibusConnectorMessageConfirmation getMessageConfirmation() {
            return this.messageConfirmation;
        }

        public void persistEvidenceToMessage() {
            LOGGER.trace("#persistEvidenceToMessage: persist evidence [{}] to message [{}]", messageConfirmation, originalMesssage);
            evidencePersistenceService.persistEvidenceForMessageIntoDatabase(originalMesssage, messageConfirmation);
        }

    }

    public class ConfirmationMessageBuilder {
        DomibusConnectorMessage originalMessage;
        DomibusConnectorEvidenceType evidenceType;
        DomibusConnectorRejectionReason rejectionReason;
        String rejectionDetails;
        DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
        private DomibusConnectorAction action;

        private ConfirmationMessageBuilder(DomibusConnectorMessage message, DomibusConnectorEvidenceType evidenceType) {
            this.originalMessage = message;
            this.evidenceType = evidenceType;
            DomibusConnectorMessageDetails originalDetails = originalMessage.getMessageDetails();
            BeanUtils.copyProperties(originalDetails, details);
        }

        public ConfirmationMessageBuilder setRejectionReason(DomibusConnectorRejectionReason rejectionReason) {
            this.rejectionReason = rejectionReason;
            return this;
        }

        public ConfirmationMessageBuilder setDetails(String details) {
            this.rejectionDetails = details;
            return this;
        }

        public ConfirmationMessageBuilder setAction(DomibusConnectorAction action) {
            this.action = action;
            return this;
        }

        /**
         * switches the toParty with fromParty
         *  neccessary if the evidence goes in the other direction as the original message
         * @return the builder object
         */
        public ConfirmationMessageBuilder switchFromToParty() {
            LOGGER.debug("[{}]: switching fromParty with toParty in messageDetails", this);
            DomibusConnectorParty fromParty = details.getFromParty();
            DomibusConnectorParty toParty = details.getToParty();
            details.setFromParty(toParty);
            details.setToParty(fromParty);
            return this;
        }

        public DomibusConnectorMessageConfirmationWrapper build() {
            try {
                DomibusConnectorMessageConfirmation messageConfirmation = evidencesToolkit.createEvidence(evidenceType, originalMessage, rejectionReason, rejectionDetails);
                originalMessage.addConfirmation(messageConfirmation);

                DomibusConnectorMessageDetails originalDetails = originalMessage.getMessageDetails();

                if (originalDetails.getEbmsMessageId() != null) {
                    details.setRefToMessageId(originalDetails.getEbmsMessageId());
                } else if (originalDetails.getBackendMessageId() != null) {
                    details.setRefToMessageId(originalDetails.getBackendMessageId());
                } else {
                    String error = String.format("Cannot set refToMessageId: both ebmsMessageId and backendMessageId of original originalMessage [%s] are null!",
                            originalMessage);
                    throw new RuntimeException(error);
                }

                details.setAction(action);

                DomibusConnectorMessage evidenceMessage = new DomibusConnectorMessage(details, messageConfirmation);
                evidenceMessage.setConnectorMessageId(messageIdGenerator.generateDomibusConnectorMessageId());

                DomibusConnectorMessageConfirmationWrapper wrapper = new DomibusConnectorMessageConfirmationWrapper();
                wrapper.setEvidenceMessage(evidenceMessage);
                wrapper.setOriginalMesssage(originalMessage);
                wrapper.setMessageConfirmation(messageConfirmation);
                return wrapper;

            } catch (DomibusConnectorEvidencesToolkitException e) {
                LOGGER.error("A Exception occured while generating evdince of type [{}]", evidenceType);
                throw new RuntimeException(e);
            }
        }

        public String toString() {
            ToStringCreator toStringCreator = new ToStringCreator(this);
            toStringCreator.append("originalMessage", this.originalMessage);
            toStringCreator.append("evidenceType", this.evidenceType);
            return toStringCreator.toString();
        }
    }
}
