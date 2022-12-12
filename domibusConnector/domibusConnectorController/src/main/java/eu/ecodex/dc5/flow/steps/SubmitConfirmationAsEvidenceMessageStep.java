package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.ecodex.dc5.flow.events.MessageReadyForTransportEvent;
import eu.ecodex.dc5.message.ConfirmationCreatorService;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.controller.spring.ConnectorMessageProcessingProperties;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * This step sends a by the connector generated evidence
 * <p>
 * <p>
 * For this purpose a new DomibusConnectorMessage
 * with a new DomibusConnectorMessageId is created
 * this message is not stored into the DB, it is only
 * used within the Queues
 */
@Component
@RequiredArgsConstructor
public class SubmitConfirmationAsEvidenceMessageStep {

    private static final Logger LOGGER = LogManager.getLogger(SubmitConfirmationAsEvidenceMessageStep.class);

    //    private final SubmitMessageToLinkModuleQueueStep submitMessageToLinkModuleQueueStep;
    private final ConfigurationPropertyManagerService configurationPropertyLoaderService;
    private final ConfirmationCreatorService confirmationCreator;
    private final DomibusConnectorMessageIdGenerator messageIdGenerator;

    private final ApplicationEventPublisher eventPublisher;

    private final DC5MessageRepo messageRepo;

    /**
     * sends the supplied confirmation as
     * evidence message in the same direction
     * as the supplied business message
     * <p>
     * for this purpose a new message is generated
     * this message is NOT stored into the DB
     *
     * @param businessMessage the business message
     * @param confirmation    the confirmation
     */
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "SubmitConfirmationAsEvidenceMessageStep#sameDirection")
    public void submitSameDirection(DomibusConnectorMessageId messageId, DC5Message businessMessage, DC5Confirmation confirmation) {
        validateParameters(businessMessage);
        DC5Message.DC5MessageBuilder evidenceMessage = buildEvidenceMessage(messageId, businessMessage, confirmation);
//        submitMessageToLinkModuleQueueStep.submitMessage(evidenceMessage);
        DC5Message build = evidenceMessage.build();
        DC5Message msg = messageRepo.save(build);

        String linkName = getLinkName(businessMessage, businessMessage.getDirection().getTarget());

        MessageReadyForTransportEvent messageReadyForTransportEvent = MessageReadyForTransportEvent.of(msg.getId(), linkName, businessMessage.getTarget());
        eventPublisher.publishEvent(messageReadyForTransportEvent);
    }

    private String getLinkName(DC5Message businessMessage, MessageTargetSource target) {
        if (target == MessageTargetSource.BACKEND) {
            return businessMessage.getBackendLinkName();
        } else if (target == MessageTargetSource.GATEWAY) {
            return businessMessage.getGatewayLinkName();
        } else {
            throw new IllegalArgumentException("MessageTargetSource not known!");
        }
    }

    /**
     * sends the supplied confirmation as
     * evidence message in the opposite direction
     * as the supplied business message
     * <p>
     * for this purpose a new message is generated
     * this message is NOT stored into the DB
     *
     * @param messageId       the connector message id of the new confirmation message
     * @param businessMessage the business message
     * @param confirmation    the confirmation
     */
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "SubmitConfirmationAsEvidenceMessageStep#oppositeDirection")
    public void submitOppositeDirection(DomibusConnectorMessageId messageId, DC5Message businessMessage, DC5Confirmation confirmation) {
        validateParameters(businessMessage);

//        DC5Ebms.DC5EbmsBuilder ebmsData = businessMessage.getEbmsData().toBuilder();

        DC5EcxAddress sender = businessMessage.getEbmsData().getReceiver()
                .toBuilder()
                .role(businessMessage.getEbmsData().getSender().getRole()
                        .toBuilder()
                        .build())
                .build();
        DC5EcxAddress receiver = businessMessage.getEbmsData().getSender()
                .toBuilder()
                .role(businessMessage.getEbmsData().getReceiver().getRole()
                        .toBuilder()
                        .build())
                .build();

        DC5Message.DC5MessageBuilder dc5MessageBuilder = buildEvidenceMessage(messageId, businessMessage, confirmation);
        DC5Message msg = dc5MessageBuilder
                .connectorMessageId(messageIdGenerator.generateDomibusConnectorMessageId())
                .target(businessMessage.getSource())
                .source(businessMessage.getTarget())
                .build();
        msg.getEbmsData().setReceiver(receiver);
        msg.getEbmsData().setSender(sender);

        msg = messageRepo.save(msg);
        String linkName = getLinkName(businessMessage, businessMessage.getDirection().getSource());

        MessageReadyForTransportEvent messageReadyForTransportEvent = MessageReadyForTransportEvent.of(msg.getId(), linkName, businessMessage.getDirection().getSource());
        eventPublisher.publishEvent(messageReadyForTransportEvent);

    }


    private DC5Message.DC5MessageBuilder buildEvidenceMessage(DomibusConnectorMessageId messageId, DC5Message businessMessage, DC5Confirmation confirmation) {

        if (messageId == null) {
            messageId = messageIdGenerator.generateDomibusConnectorMessageId();
            LOGGER.info("MessageId is null starting new message transport.");
        }

        try (final CloseableThreadContext.Instance ctc =
                     CloseableThreadContext.put(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, messageId.getConnectorMessageId())) {

            DomibusConnectorEvidenceType evidenceType = confirmation.getEvidenceType();
            DC5Action evidenceAction = confirmationCreator.createEvidenceAction(evidenceType);

            DC5Ebms.DC5EbmsBuilder ebmsDataBuilder;
            if (businessMessage.getEbmsData() != null) {
                ebmsDataBuilder = businessMessage.getEbmsData().toBuilder();
                ebmsDataBuilder.refToEbmsMessageId(businessMessage.getEbmsData().getEbmsMessageId());
            } else {
                ebmsDataBuilder = DC5Ebms.builder();
            }
            ebmsDataBuilder.id(null);
            ebmsDataBuilder.ebmsMessageId(null);
            ebmsDataBuilder.conversationId(null);
            ebmsDataBuilder.action(evidenceAction);


            DC5Message.DC5MessageBuilder msgBuilder = DC5Message.builder();
            DC5BackendData.DC5BackendDataBuilder dc5BackendDataBuilder;
            if (businessMessage.getBackendData() != null) {
                dc5BackendDataBuilder = businessMessage.getBackendData().toBuilder();
                dc5BackendDataBuilder.refToBackendMessageId(businessMessage.getBackendData().getBackendMessageId());
                msgBuilder.backendData(DC5BackendData.builder().build());
            } else {
//                dc5BackendDataBuilder = DC5BackendData.builder();

            }
//            dc5BackendDataBuilder.backendConversationId(null)
//                .id(null)
//                .backendMessageId(null);


            msgBuilder.id(null)
                    .businessDomainId(businessMessage.getBusinessDomainId())
                    .ebmsData(ebmsDataBuilder.build())
//                    .backendData(dc5BackendDataBuilder.build())
                    .backendLinkName(businessMessage.getBackendLinkName())
                    .gatewayLinkName(businessMessage.getGatewayLinkName())
                    .transportedMessageConfirmation(confirmation);
            return msgBuilder;


//            NewMessageStoredEvent newMessageStoredEvent = NewMessageStoredEvent.of(msg.getId());
//            eventPublisher.publishEvent(newMessageStoredEvent);

//            return msg;

//            DC5Ebms messageDetails = DomibusConnectorMessageDetailsBuilder.create()
//                    .copyPropertiesFrom(businessMessage.getEbmsData())
//                    .withAction(evidenceAction)
//                    .build();
//            messageDetails.setRefToMessageId(businessMessage.getEbmsData().getEbmsMessageId());
//            messageDetails.setRefToBackendMessageId(businessMessage.getEbmsData().getBackendMessageId());

//            DC5Message evidenceMessage = DomibusConnectorMessageBuilder.createBuilder()
//                    .addTransportedConfirmations(confirmation)
//                    .setMessageDetails(messageDetails)
//                    .build();

//            evidenceMessage.setMessageLaneId(businessMessage.getMessageLaneId());
//            if (evidenceMessage.getMessageLaneId() == null) {
//                evidenceMessage.setMessageLaneId(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
//            }
//
//            evidenceMessage.setConnectorMessageId(messageId);
//            evidenceMessage.getEbmsData().setCausedBy(businessMessage.getConnectorMessageId());
//            LOGGER.info("Sending evidence as confirmation message with ID [{}]", evidenceMessage.getConnectorMessageId());
//            return null;

        }
    }

    private void validateParameters(DC5Message businessMessage) {
        if (businessMessage == null) {
            throw new IllegalArgumentException("The businessMessage cannot be null here!");
        }
        if (businessMessage.getEbmsData() == null) {
            throw new IllegalArgumentException("The messageDetails of the businessMessage cannot be null here!");
        }
        if (businessMessage.getDirection() == null) {
            throw new IllegalArgumentException("The direction is not allowed to be null here!");
        }
    }

    public boolean isSendCreatedTriggerEvidenceBack(DomibusConnectorBusinessDomain.BusinessDomainId messageDomain) {
        ConnectorMessageProcessingProperties processingProperties =
                configurationPropertyLoaderService.loadConfiguration(messageDomain, ConnectorMessageProcessingProperties.class);
        boolean result = processingProperties.isSendGeneratedEvidencesToBackend();
        LOGGER.debug("Evidence will be submitted back to Backend as EvidenceMessage: [{}]", result);
        return result;
    }
}
