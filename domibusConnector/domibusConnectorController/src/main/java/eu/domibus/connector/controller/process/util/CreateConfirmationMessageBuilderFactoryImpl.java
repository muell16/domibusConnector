package eu.domibus.connector.controller.process.util;

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
import eu.domibus.connector.domain.model.builder.DomibusConnectorPartyBuilder;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.persistence.service.impl.DomibusConnectorPModePersistenceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.style.ToStringCreator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreateConfirmationMessageBuilderFactoryImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateConfirmationMessageBuilderFactoryImpl.class);

    private final DomibusConnectorEvidencesToolkit evidencesToolkit;
    private final DomibusConnectorEvidencePersistenceService evidencePersistenceService;
    private final DomibusConnectorMessageIdGenerator messageIdGenerator;
    private final ConfigurationPropertyLoaderService configurationPropertyLoaderService;
    private final DomibusConnectorPModePersistenceService pModePersistenceService;

    public CreateConfirmationMessageBuilderFactoryImpl(DomibusConnectorEvidencesToolkit evidencesToolkit,
                                                       DomibusConnectorEvidencePersistenceService evidencePersistenceService,
                                                       DomibusConnectorMessageIdGenerator messageIdGenerator,
                                                       ConfigurationPropertyLoaderService configurationPropertyLoaderService,
                                                       DomibusConnectorPModePersistenceService pModePersistenceService) {
        this.evidencesToolkit = evidencesToolkit;
        this.evidencePersistenceService = evidencePersistenceService;
        this.messageIdGenerator = messageIdGenerator;
        this.configurationPropertyLoaderService = configurationPropertyLoaderService;
        this.pModePersistenceService = pModePersistenceService;
    }


    public ConfirmationMessageBuilder createConfirmationMessageBuilder(DomibusConnectorMessage message, DomibusConnectorEvidenceType evidenceType) {
        ConfirmationMessageBuilder confirmationMessageBuilder = new ConfirmationMessageBuilder(message, evidenceType);

        DomibusConnectorAction messageAction = createEvidenceAction(evidenceType);
        confirmationMessageBuilder.setAction(messageAction);

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
        
        public void setEvidenceDeliveredToGateway() {
        	LOGGER.trace("#setEvidenceDeliveredToGateway: evidence [{}] to message [{}]", this.evidenceMessage.getMessageConfirmations().get(0).getEvidenceType(), evidenceMessage.getConnectorMessageId());
        	evidencePersistenceService.setEvidenceDeliveredToGateway(
        			new DomibusConnectorMessage.DomibusConnectorMessageId(originalMesssage.getConnectorMessageId()), 
        			this.evidenceMessage.getMessageConfirmations().get(0).getEvidenceType());
        }

        public void setEvidenceDeliveredToBackend() {
        	LOGGER.trace("#setEvidenceDeliveredToBackend: evidence [{}] to message [{}]", this.evidenceMessage.getMessageConfirmations().get(0).getEvidenceType(), evidenceMessage.getConnectorMessageId());
        	evidencePersistenceService.setEvidenceDeliveredToNationalSystem(
        			new DomibusConnectorMessage.DomibusConnectorMessageId(originalMesssage.getConnectorMessageId()), 
        			this.evidenceMessage.getMessageConfirmations().get(0).getEvidenceType());
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
        
        public void switchMessageTarget() {
        	LOGGER.debug("[{}]: switching originalSender with finalRecipient in messageDetails", this);
        	MessageTargetSource messageTarget = this.evidenceMessage.getMessageDetails().getDirection().getTarget();
        	if (messageTarget == MessageTargetSource.BACKEND) {
        		this.evidenceMessage.getMessageDetails().setDirection(DomibusConnectorMessageDirection.CONNECTOR_TO_GATEWAY);
            } else if (messageTarget == MessageTargetSource.GATEWAY) {
            	this.evidenceMessage.getMessageDetails().setDirection(DomibusConnectorMessageDirection.CONNECTOR_TO_BACKEND);
            } else {
                throw new RuntimeException("The evidence message target MUST be set!, call withDirection of the builder!");
            }
           
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
        private MessageTargetSource messageTarget = null;

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
        }

        public ConfirmationMessageBuilder setRejectionReason(DomibusConnectorRejectionReason rejectionReason) {
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
        public ConfirmationMessageBuilder swithOriginalSenderFinalRecipient() {
            LOGGER.debug("[{}]: switching originalSender with finalRecipient in messageDetails", this);
            String finalRecipient = details.getFinalRecipient();
            String originalSender = details.getOriginalSender();
            details.setFinalRecipient(originalSender);
            details.setOriginalSender(finalRecipient);
            return this;
        }

        /**
         * switches the toParty with fromParty
         *  neccessary if the evidence goes in the other direction as the original message
         * @return the builder object
         */
        public ConfirmationMessageBuilder switchFromToParty() {
            DomibusConnectorMessageLane.MessageLaneId defaultMessageLaneId = DomibusConnectorMessageLane.getDefaultMessageLaneId();
            LOGGER.debug("[{}]: switching fromParty [{}] with toParty [{}] in messageDetails", this, details.getFromParty(), details.getToParty());
            DomibusConnectorParty fromParty = DomibusConnectorPartyBuilder.createBuilder().copyPropertiesFrom(details.getFromParty()).build();
            DomibusConnectorParty toParty = DomibusConnectorPartyBuilder.createBuilder().copyPropertiesFrom(details.getToParty()).build();


            fromParty.setRoleType(DomibusConnectorParty.PartyRoleType.RESPONDER);
            fromParty.setRole(details.getToParty().getRole()); //switch roles
            Optional<DomibusConnectorParty> lookedUpFromParty = pModePersistenceService.getConfiguredSingle(defaultMessageLaneId, fromParty);
            if (!lookedUpFromParty.isPresent()) {
                throw new RuntimeException(String.format("Cannot switch parties. No Party [%s] found in pmodes with RoleType INITIATOR", fromParty));
            }
            details.setToParty(lookedUpFromParty.get());
            LOGGER.debug("To party is: [{}]", lookedUpFromParty.get());

            toParty.setRoleType(DomibusConnectorParty.PartyRoleType.INITIATOR);
            toParty.setRole(details.getFromParty().getRole()); //switch roles
            Optional<DomibusConnectorParty> lookedUpToParty = pModePersistenceService.getConfiguredSingle(defaultMessageLaneId, toParty);
            if (!lookedUpToParty.isPresent()) {
                throw new RuntimeException(String.format("Cannot switch parties. No Party [%s] found in pmodes with RoleType RESPONDER", toParty));
            }
            details.setFromParty(lookedUpToParty.get());
            LOGGER.debug("From party is: [{}]", lookedUpToParty.get());

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

                details.setAction(action);
                details.setCausedBy(originalMessage.getConnectorMessageId());
                if (messageTarget == MessageTargetSource.BACKEND) {
                    details.setDirection(DomibusConnectorMessageDirection.CONNECTOR_TO_BACKEND);
                } else if (messageTarget == MessageTargetSource.GATEWAY) {
                    details.setDirection(DomibusConnectorMessageDirection.CONNECTOR_TO_GATEWAY);
                } else {
                    throw new RuntimeException("The evidence message target MUST be set!, call withDirection of the builder!");
                }


                DomibusConnectorMessageDetails newDetails = DomibusConnectorMessageDetailsBuilder.create().copyPropertiesFrom(details).build();
                DomibusConnectorMessage evidenceMessage = new DomibusConnectorMessage(newDetails, messageConfirmation);
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
