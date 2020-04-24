package eu.domibus.connector.controller.process.util;

import eu.domibus.connector.common.service.ConfigurationPropertyLoaderService;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDetailsBuilder;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.style.ToStringCreator;
import org.springframework.stereotype.Component;

@Component
public class CreateConfirmationMessageBuilderFactoryImpl implements ConfirmationMessageBuilderFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateConfirmationMessageBuilderFactoryImpl.class);

    private DomibusConnectorEvidencesToolkit evidencesToolkit;
    private DomibusConnectorEvidencePersistenceService evidencePersistenceService;
    private DomibusConnectorMessageIdGenerator messageIdGenerator;
    private ConfigurationPropertyLoaderService configurationPropertyLoaderService;

    @Autowired
    public void setEvidencesToolkit(DomibusConnectorEvidencesToolkit evidencesToolkit) {
        this.evidencesToolkit = evidencesToolkit;
    }

    @Autowired
    public void setConfigurationPropertyLoaderService(ConfigurationPropertyLoaderService configurationPropertyLoaderService) {
        this.configurationPropertyLoaderService = configurationPropertyLoaderService;
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
            evidencePersistenceService.persistEvidenceForMessageIntoDatabase(originalMesssage,
                    messageConfirmation,
                    new DomibusConnectorMessage.DomibusConnectorMessageId(evidenceMessage.getConnectorMessageId()));
        }

        public DomibusConnectorEvidenceType getEvidenceType() {
            return this.evidenceMessage.getMessageConfirmations().get(0).getEvidenceType();
        }

        public DomibusConnectorMessage.DomibusConnectorMessageId getCausedByConnectorMessageId() {
            return new DomibusConnectorMessage.DomibusConnectorMessageId(this.originalMesssage.getConnectorMessageId());
        }

        public DomibusConnectorMessage getOriginalMessage() {
            return this.originalMesssage;
        }
    }

    /**
     * The ConfirmationMessageBuilder helps to build a ConfirmationMessage
     */
    public class ConfirmationMessageBuilder {
        DomibusConnectorMessage originalMessage;
        DomibusConnectorEvidenceType evidenceType;
        DomibusConnectorRejectionReason rejectionReason = DomibusConnectorRejectionReason.OTHER;
        String rejectionDetails;
        DomibusConnectorMessageDetails details;
        private DomibusConnectorAction action;

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
        }

        public ConfirmationMessageBuilder setRejectionReason(DomibusConnectorRejectionReason rejectionReason) {
            this.rejectionReason = rejectionReason;
            return this;
        }

        /**
         * Sets the nationalMessageId as the refToMessageId
         * within the MessageDetails, used when the message is just
         * transported back only to the connectorClient system
         *
         */
        private ConfirmationMessageBuilder useNationalIdAsRefToMessageId() {
            String refToMsg = originalMessage.getMessageDetails().getBackendMessageId();
            if (refToMsg == null) {
                throw new IllegalArgumentException("Cannot use NationalID as refToMsgId because it is NULL!");
            }
            this.details.setRefToMessageId(refToMsg);
            return this;
        }

        /**
         * Sets the ebmsId as the refToMessageId
         * within the MessageDetails, used when the message is
         * transported to the Gateway
         */
        public ConfirmationMessageBuilder useEbmsIdAsRefToMessageId() {
            String refToMsg = originalMessage.getMessageDetails().getEbmsMessageId();
            if (refToMsg == null) {
                throw new IllegalArgumentException("Cannot use EBMSID as refToMsgId because it is NULL!");
            }
            this.details.setRefToMessageId(refToMsg);
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

        /**
         * starts building the ETSI-REM evidence with the already provided parameters
         * @return a {@link DomibusConnectorMessageConfirmationWrapper} which contains the related business message and the ConfirmationMessage
         */
        public DomibusConnectorMessageConfirmationWrapper build() {
            try {
                DomibusConnectorMessageConfirmation messageConfirmation = evidencesToolkit.createEvidence(evidenceType, originalMessage, rejectionReason, rejectionDetails);
                originalMessage.addConfirmation(messageConfirmation);

                if (details.getRefToMessageId() == null) {
                    this.useNationalIdAsRefToMessageId();
                }
                details.setAction(action);
                details.setCausedBy(originalMessage.getConnectorMessageId());


                DomibusConnectorMessage evidenceMessage = new DomibusConnectorMessage(details, messageConfirmation);
                evidenceMessage.setConnectorMessageId(messageIdGenerator.generateDomibusConnectorMessageId());

                DomibusConnectorMessageConfirmationWrapper wrapper = new DomibusConnectorMessageConfirmationWrapper();
                wrapper.setEvidenceMessage(evidenceMessage);
                wrapper.setOriginalMesssage(originalMessage);
                wrapper.setMessageConfirmation(messageConfirmation);
                return wrapper;

            } catch (DomibusConnectorEvidencesToolkitException e) {
                String message = String.format("A Exception occured while generating evidence of type [%s]", evidenceType);
                LOGGER.error(message, e);
                throw new RuntimeException(message, e);
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
