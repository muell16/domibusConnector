package eu.domibus.connector.domain.model.builder;

import eu.domibus.connector.domain.model.*;
import eu.ecodex.dc5.message.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builder for @see eu.ecodex.dc5.message.model.DomibusConnectorMessage
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 * 
 */
public final class DomibusConnectorMessageBuilder {

    private DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId;
    private DC5Ebms messageDetails;
	private DC5MessageContent messageContent;
	private List<DomibusConnectorMessageAttachment> messageAttachments = new ArrayList<>();
	private List<DC5Confirmation> transportedConfirmations = new ArrayList<>();
	private List<DomibusConnectorMessageError> messageErrors = new ArrayList<>();
    private DomibusConnectorMessageId connectorMessageId;
    private List<DC5Confirmation> relatedMessageConfirmations = new ArrayList<>();

    public static DomibusConnectorMessageBuilder createBuilder() {
        return new DomibusConnectorMessageBuilder();
    }

    private DomibusConnectorMessageBuilder() {}

    /**
     * The internal message processing id
     *  is required!
     * @param connectorMessageId - the message id
     * @return the builder
     */
    public DomibusConnectorMessageBuilder setConnectorMessageId(String connectorMessageId) {
        this.connectorMessageId = new DomibusConnectorMessageId(connectorMessageId);
        return this;
    }
    
    /**
     * 
     *  is required!
     * @param msgDetails the messageDetails
     * @return the builder
     */
    public DomibusConnectorMessageBuilder setMessageDetails(DC5Ebms msgDetails) {
        this.messageDetails = msgDetails;
        return this;
    }
    
    /**
     * is required if no confirmation is added to the message
     * @param msgContent the message content
     * @return the builder
     */
    public DomibusConnectorMessageBuilder setMessageContent(DC5MessageContent msgContent) {
        this.messageContent = msgContent;
        return this;
    }
    
    /**
     * add none to multiple message attachments
     *  is optional
     * @param attachment the message attachment
     * @return the builder
     */
    public DomibusConnectorMessageBuilder addAttachment(DomibusConnectorMessageAttachment attachment) {
        this.messageAttachments.add(attachment);
        return this;
    }


    public DomibusConnectorMessageBuilder addAttachments(List<DomibusConnectorMessageAttachment> domibusConnectorMessageAttachments) {
        this.messageAttachments.addAll(domibusConnectorMessageAttachments);
        return this;
    }

    /**
     *  add multiple confirmations to the message
     *  is required if no message content is set!
     * @param confirmation the confirmation
     * @return the builder
     */
    public DomibusConnectorMessageBuilder addTransportedConfirmations(DC5Confirmation confirmation) {
        this.transportedConfirmations.add(confirmation);
        return this;
    }

    public DomibusConnectorMessageBuilder addTransportedConfirmations(List<DC5Confirmation> confirmations) {
        this.transportedConfirmations.addAll(confirmations);
        return this;
    }
    
    
    /**
     * add none or multiple errors
     *  is optional
     * @param error the message error
     * @return the builder
     */
    public DomibusConnectorMessageBuilder addError(DomibusConnectorMessageError error) {
        this.messageErrors.add(error);
        return this;
    }


    public DomibusConnectorMessageBuilder addErrors(List<DomibusConnectorMessageError> errors) {
        this.messageErrors.addAll(errors);
        return this;
    }
    
    /**
     * 
     * @return the created DomibusConnectorMessage
     */
    public DC5Message build() {
        DC5Message message = new DC5Message();
        if (this.messageDetails == null) {
            throw new IllegalArgumentException("Setting message details is required!");
        }        
        message.setEbmsData(this.messageDetails);
        message.setConnectorMessageId(this.connectorMessageId);
        message.setMessageLaneId(this.businessDomainId);
        message.setMessageContent(this.messageContent);
//        message.getMessageAttachments().addAll(this.messageAttachments);
        message.getTransportedMessageConfirmations().addAll(this.transportedConfirmations);
        message.getMessageProcessErrors().addAll(this.messageErrors);
//        message.getRelatedMessageConfirmations().addAll(this.relatedMessageConfirmations);

        this.connectorMessageId = null;
        return message;
    }

    public DomibusConnectorMessageBuilder copyPropertiesFrom(DC5Message message) {
        this.messageDetails = message.getEbmsData()
                .toBuilder()
                .build();
        this.connectorMessageId = message.getConnectorMessageId();
        this.businessDomainId = message.getMessageLaneId();
        if (message.getMessageContent() != null) {
            this.messageContent = DomibusConnectorMessageContentBuilder.createBuilder()
                    .copyPropertiesFrom(message.getMessageContent())
                    .build();
        }
//        this.messageAttachments = message.getMessageAttachments()
//                .stream()
//                .map(a -> DomibusConnectorMessageAttachmentBuilder.createBuilder()
//                        .copyPropertiesFrom(a).build())
//                .collect(Collectors.toList());

        this.transportedConfirmations = message.getTransportedMessageConfirmations()
                .stream()
                .map(c -> DomibusConnectorMessageConfirmationBuilder.createBuilder()
                .copyPropertiesFrom(c).build())
                .collect(Collectors.toList());


//        this.relatedMessageConfirmations = message.getRelatedMessageConfirmations()
//                .stream()
//                .map(c -> DomibusConnectorMessageConfirmationBuilder.createBuilder()
//                        .copyPropertiesFrom(c).build())
//                .collect(Collectors.toList());

        return this;
    }

    public DomibusConnectorMessageBuilder setConnectorMessageId(DomibusConnectorMessageId dcMsgId) {
        this.connectorMessageId = dcMsgId;
        return this;
    }

    public DomibusConnectorMessageBuilder setMessageLaneId(DomibusConnectorBusinessDomain.BusinessDomainId laneId) {
        this.businessDomainId = laneId;
        return this;
    }


    public DomibusConnectorMessageBuilder addRelatedConfirmations(List<DC5Confirmation> collect) {
        this.relatedMessageConfirmations = collect;
        return this;
    }

    public DomibusConnectorMessageBuilder addRelatedConfirmation(DC5Confirmation c) {
        this.relatedMessageConfirmations.add(c);
        return this;
    }

}
