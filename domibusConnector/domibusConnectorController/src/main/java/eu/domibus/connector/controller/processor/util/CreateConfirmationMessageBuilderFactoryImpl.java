package eu.domibus.connector.controller.processor.util;

import eu.domibus.connector.common.service.ConfigurationPropertyLoaderService;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDetailsBuilder;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.style.ToStringCreator;
import org.springframework.stereotype.Component;

import javax.annotation.concurrent.NotThreadSafe;

@Component
@RequiredArgsConstructor
public class CreateConfirmationMessageBuilderFactoryImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateConfirmationMessageBuilderFactoryImpl.class);

    private final DomibusConnectorEvidencesToolkit evidencesToolkit;
    private final DomibusConnectorEvidencePersistenceService evidencePersistenceService;
    private final DomibusConnectorMessageIdGenerator messageIdGenerator;
    private final ConfigurationPropertyLoaderService configurationPropertyLoaderService;
    private final DCMessagePersistenceService messagePersistenceService;


    public ConfirmationMessageBuilder createConfirmationMessageBuilderFromBusinessMessageAndConfirmation(DomibusConnectorMessage originalMessage, DomibusConnectorMessageConfirmation confirmation) {
        DomibusConnectorEvidenceType evidenceType = confirmation.getEvidenceType();
        ConfirmationMessageBuilder confirmationMessageBuilder = new ConfirmationMessageBuilder(originalMessage, evidenceType);

        DomibusConnectorAction messageAction = createEvidenceAction(evidenceType);
        confirmationMessageBuilder.setAction(messageAction);
        confirmationMessageBuilder.setConfirmation(confirmation);
        confirmationMessageBuilder.messageTarget = originalMessage.getMessageDetails().getDirection().getTarget();

        return confirmationMessageBuilder;
    }


    public ConfirmationMessageBuilder createConfirmationMessageBuilderFromBusinessMessage(DomibusConnectorMessage message, DomibusConnectorEvidenceType evidenceType) {
        ConfirmationMessageBuilder confirmationMessageBuilder = new ConfirmationMessageBuilder(message, evidenceType);

        DomibusConnectorAction messageAction = createEvidenceAction(evidenceType);
        confirmationMessageBuilder.setAction(messageAction);
        confirmationMessageBuilder.setRejectionReason(DomibusConnectorRejectionReason.OTHER);

        return confirmationMessageBuilder;
    }

    public DomibusConnectorAction createEvidenceAction(DomibusConnectorEvidenceType type) throws DomibusConnectorControllerException {

        EvidenceActionServiceConfigurationProperties evidenceActionServiceConfigurationProperties =
                configurationPropertyLoaderService.loadConfiguration(DomibusConnectorMessageLane.getDefaultMessageLaneId(), EvidenceActionServiceConfigurationProperties.class);

        switch (type) {
            case DELIVERY:
                return evidenceActionServiceConfigurationProperties
                        .getDelivery().getConnectorAction();
            case NON_DELIVERY:
                return evidenceActionServiceConfigurationProperties
                        .getNonDelivery().getConnectorAction();
            case RETRIEVAL:
                return evidenceActionServiceConfigurationProperties
                        .getRetrieval().getConnectorAction();
            case NON_RETRIEVAL:
                return evidenceActionServiceConfigurationProperties
                        .getNonRetrieval().getConnectorAction();
            case RELAY_REMMD_FAILURE:
                return evidenceActionServiceConfigurationProperties
                        .getRelayREMMDFailure().getConnectorAction();
            case RELAY_REMMD_REJECTION:
                return evidenceActionServiceConfigurationProperties
                        .getRelayREEMDRejection().getConnectorAction();
            case RELAY_REMMD_ACCEPTANCE:
                return evidenceActionServiceConfigurationProperties
                        .getRelayREEMDAcceptance().getConnectorAction();
            case SUBMISSION_ACCEPTANCE:
                return evidenceActionServiceConfigurationProperties
                        .getSubmissionAcceptance().getConnectorAction();
            case SUBMISSION_REJECTION:
                return evidenceActionServiceConfigurationProperties
                        .getSubmissionRejection().getConnectorAction();
            default:
                throw new DomibusConnectorControllerException("Illegal Evidence type " + type + "! No Action found!");
        }
    }


    /**
     * A Wrapper class which contains the message confirmation (ETSI-REM evidence)
     * the business message the confirmation is related to
     * and the evidenceMessage
     */
    public class DomibusConnectorMessageConfirmationWrapper {

        DomibusConnectorMessageConfirmation messageConfirmation;
        DomibusConnectorMessage originalMesssage;
        DomibusConnectorMessage evidenceMessage;
        DomibusConnectorMessageId causedBy;

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

        private void setCausedBy(DomibusConnectorMessageId causedBy) {
            this.causedBy = causedBy;
        }

        public DomibusConnectorMessage getEvidenceMessage() {
            return this.evidenceMessage;
        }

        public DomibusConnectorMessageConfirmation getMessageConfirmation() {
            return this.messageConfirmation;
        }

//        /**
//         * Message must already be persisted!
//         * Call persistMessage first
//         *
//         */
//        public void persistEvidenceToBusinessMessage() {
//            LOGGER.trace("#persistEvidenceToMessage: persist evidence [{}] to businessMessage [{}]", messageConfirmation, originalMesssage);
//            evidencePersistenceService.persistEvidenceMessageForBusinessMessage(evidenceMessage, originalMesssage);
//        }

        public void persistMessage() {
            LOGGER.trace("#persistMessage: persisting message");
            if (this.evidenceMessage.getMessageDetails() == null || this.evidenceMessage.getMessageDetails().getDirection() == null) {
                throw new IllegalArgumentException("Message Details or MessageDirection of message are not allowed to be null!");
            }
            messagePersistenceService.persistMessageIntoDatabase(this.evidenceMessage, this.evidenceMessage.getMessageDetails().getDirection());
        }

//        /**
//         * Persists the current evidence message into the DB
//         * and persists the evidence to the related business message
//         */
//        public void persistEvidenceMessageAndPersistEvidenceToBusinessMessage() {
//            this.persistMessage();
//            this.persistEvidenceToBusinessMessage();
//        }

        public DomibusConnectorEvidenceType getEvidenceType() {
            return this.evidenceMessage.getTransportedMessageConfirmations().get(0).getEvidenceType();
        }

        public DomibusConnectorMessageId getCausedByConnectorMessageId() {
            return this.causedBy;
        }

        public DomibusConnectorMessage getOriginalMessage() {
            return this.originalMesssage;
        }
    }

    /**
     * The ConfirmationMessageBuilder helps to build a ConfirmationMessage
     */
    @NotThreadSafe
    public class ConfirmationMessageBuilder {
        DomibusConnectorMessage originalMessage;
        DomibusConnectorEvidenceType evidenceType;
        DomibusConnectorRejectionReason rejectionReason = null; //DomibusConnectorRejectionReason.OTHER;
        String rejectionDetails;
        DomibusConnectorMessageDetails details;
        private DomibusConnectorAction action;
        private MessageTargetSource messageTarget = null;
        private DomibusConnectorMessageConfirmation messageConfirmation = null;

        private ConfirmationMessageBuilder(DomibusConnectorMessage message, DomibusConnectorEvidenceType evidenceType) {
            this.originalMessage = message;
            this.evidenceType = evidenceType;
            DomibusConnectorMessageDetails originalDetails = originalMessage.getMessageDetails();
            this.details = DomibusConnectorMessageDetailsBuilder.create()
                    .copyPropertiesFrom(originalDetails)
                    .build();
            //by default ref to message id is the EBMSID of the related msg
            this.details.setRefToMessageId(originalDetails.getEbmsMessageId());
            this.details.setRefToBackendMessageId(originalDetails.getBackendMessageId());
            this.details.setEbmsMessageId(null);
            this.details.setBackendMessageId(null);
            this.messageTarget = details.getDirection().getTarget();
        }

        public ConfirmationMessageBuilder setConfirmation(DomibusConnectorMessageConfirmation confirmation) {
            if (this.rejectionReason != null || this.rejectionDetails != null) {
                throw new IllegalArgumentException("A confirmation can only be set, when rejectionReason and rejectionDetails are null!");
            }
            this.messageConfirmation = confirmation;
            return this;
        }

        public ConfirmationMessageBuilder setRejectionReason(DomibusConnectorRejectionReason rejectionReason) {
            if (rejectionReason == null) {
                throw new IllegalArgumentException("rejectionReason cannot be null!");
            }
            if (this.messageConfirmation != null) {
                throw new IllegalArgumentException("A rejectionReason can only be set if confirmation is null!");
            }
            this.rejectionReason = rejectionReason;
            return this;
        }

        public ConfirmationMessageBuilder withDirection(MessageTargetSource evidenceMessageTarget) {
            this.messageTarget = evidenceMessageTarget;
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
        public ConfirmationMessageBuilder switchFromToAttributes() {
            switchOriginalSenderFinalRecipient();
            switchFromToParty();
            return this;
        }

        private ConfirmationMessageBuilder switchFromToParty() {
            LOGGER.debug("[{}]: switching fromParty with toParty in messageDetails", this);
            DomibusConnectorParty fromParty = details.getFromParty();
            DomibusConnectorParty toParty = details.getToParty();
            details.setFromParty(toParty);
            details.setToParty(fromParty);
            return this;
        }

        private ConfirmationMessageBuilder switchOriginalSenderFinalRecipient() {
            String finalRecipient = details.getFinalRecipient();
            String originalSender = details.getOriginalSender();
            details.setOriginalSender(finalRecipient);
            details.setFinalRecipient(originalSender);
            return this;
        }

//        public ConfirmationMessageBuilder revertMessageDirection() {
//            this.switchOriginalSenderFinalRecipient();
//            this.switchFromToAttributes();
//            this.switchTarget();
//            return this;
//        }

        public ConfirmationMessageBuilder switchMessageDirection() {
            if (this.details.getDirection().getTarget() == MessageTargetSource.BACKEND) {
                messageTarget = MessageTargetSource.GATEWAY;
            } else if (this.details.getDirection().getTarget() == MessageTargetSource.GATEWAY) {
                messageTarget = MessageTargetSource.BACKEND;
            }
            return this;
        }


        private DomibusConnectorMessageConfirmationWrapper prepareBuild() {
            try {
                if (this.messageConfirmation == null) {
                    if (rejectionReason == null) {
                        throw new IllegalStateException("No rejectionReason has been set!");
                    }
                    LOGGER.debug("Evidence has not been created yet");
                    this.messageConfirmation = evidencesToolkit.createEvidence(evidenceType, originalMessage, rejectionReason, rejectionDetails);
                } else {
                    LOGGER.debug("Evidence has already been created, reusing the created evidence!");
                }
//                DomibusConnectorMessageConfirmation messageConfirmation = evidencesToolkit.createEvidence(evidenceType, originalMessage, rejectionReason, rejectionDetails);
//                originalMessage.addConfirmation(messageConfirmation);

                details.setAction(action);
                details.setCausedBy(originalMessage.getConnectorMessageId());
                if (messageTarget == MessageTargetSource.BACKEND) {
                    details.setDirection(DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);
                } else if (messageTarget == MessageTargetSource.GATEWAY) {
                    details.setDirection(DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
                } else {
                    throw new ConfirmationMessageBuilderException("The evidence message target MUST be set!, call withDirection of the builder!");
                }


                DomibusConnectorMessageDetails newDetails = DomibusConnectorMessageDetailsBuilder.create().copyPropertiesFrom(details).build();
                DomibusConnectorMessage evidenceMessage = new DomibusConnectorMessage(newDetails, messageConfirmation);

                DomibusConnectorMessageId messageConnectorId = messageIdGenerator.generateDomibusConnectorMessageId();
                LOGGER.debug("#prepareBuild: Generated new ConnectorMessageId [{}]", messageConnectorId);
                evidenceMessage.setConnectorMessageId(messageConnectorId);

                DomibusConnectorMessageConfirmationWrapper wrapper = new DomibusConnectorMessageConfirmationWrapper();
                wrapper.setEvidenceMessage(evidenceMessage);
                wrapper.setOriginalMesssage(originalMessage);
                wrapper.setMessageConfirmation(messageConfirmation);
                return wrapper;
            } catch (DomibusConnectorEvidencesToolkitException e) {
                String message = String.format("A Exception occured while generating evidence of type [%s]", evidenceType);
                LOGGER.error(message, e);
                throw new ConfirmationMessageBuilderException(message, e);
            }
        }

        /**
         * starts building the ETSI-REM evidence with the already provided parameters
         * @return a {@link DomibusConnectorMessageConfirmationWrapper} which contains the related business message and the ConfirmationMessage
         */
        public DomibusConnectorMessageConfirmationWrapper buildFromEvidenceTriggerMessage(DomibusConnectorMessage message) {
            this.messageTarget = message.getMessageDetails().getDirection().getTarget();

            DomibusConnectorMessageConfirmationWrapper wrapper = this.prepareBuild();
            wrapper.setCausedBy(message.getConnectorMessageId());
            return wrapper;
        }

        /**
         * starts building the ETSI-REM evidence with the already provided parameters
         * @return a {@link DomibusConnectorMessageConfirmationWrapper} which contains the related business message and the ConfirmationMessage
         */
        public DomibusConnectorMessageConfirmationWrapper build() {

            DomibusConnectorMessageConfirmationWrapper wrapper = prepareBuild();
            wrapper.setCausedBy(originalMessage.getConnectorMessageId());
            return wrapper;

        }

        public String toString() {
            ToStringCreator toStringCreator = new ToStringCreator(this);
            toStringCreator.append("originalMessage", this.originalMessage);
            toStringCreator.append("evidenceType", this.evidenceType);
            return toStringCreator.toString();
        }
    }


    public static class ConfirmationMessageBuilderException extends DomibusConnectorControllerException {

        public ConfirmationMessageBuilderException() {
        }

        public ConfirmationMessageBuilderException(String arg0) {
            super(arg0);
        }

        public ConfirmationMessageBuilderException(Throwable arg0) {
            super(arg0);
        }

        public ConfirmationMessageBuilderException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }
    }
}
