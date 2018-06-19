package eu.domibus.connector.domain.model.builder;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.domain.model.DomibusConnectorMessageError;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for @see eu.domibus.connector.domain.model.DomibusConnectorMessage
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 * 
 */
public final class DomibusConnectorMessageBuilder {

    private DomibusConnectorMessageDetails messageDetails;
	private DomibusConnectorMessageContent messageContent;
	private List<DomibusConnectorMessageAttachment> messageAttachments = new ArrayList<>();
	private List<DomibusConnectorMessageConfirmation> messageConfirmations = new ArrayList<>();
	private List<DomibusConnectorMessageError> messageErrors = new ArrayList<>();
    private String connectorMessageId;
    
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
        this.connectorMessageId = connectorMessageId;
        return this;
    }
    
    /**
     * 
     *  is required!
     * @param msgDetails the messageDetails
     * @return the builder
     */
    public DomibusConnectorMessageBuilder setMessageDetails(DomibusConnectorMessageDetails msgDetails) {
        this.messageDetails = msgDetails;
        return this;
    }
    
    /**
     * is required if no confirmation is added to the message
     * @param msgContent the message content
     * @return the builder
     */
    public DomibusConnectorMessageBuilder setMessageContent(DomibusConnectorMessageContent msgContent) {
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
    public DomibusConnectorMessageBuilder addConfirmation(DomibusConnectorMessageConfirmation confirmation) {
        this.messageConfirmations.add(confirmation);
        return this;
    }

    public DomibusConnectorMessageBuilder addConfirmations(List<DomibusConnectorMessageConfirmation> confirmations) {
        this.messageConfirmations.addAll(confirmations);
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
    public DomibusConnectorMessage build() {
        DomibusConnectorMessage message;
        if (this.messageDetails == null) {
            throw new IllegalArgumentException("Setting message details is required!");
        }        
        if (this.messageContent != null) {
            message = new DomibusConnectorMessage(this.connectorMessageId, this.messageDetails, this.messageContent);
        } else if (this.messageConfirmations.size() > 0) {
            DomibusConnectorMessageConfirmation confirmation = this.messageConfirmations.remove(0);
            message = new DomibusConnectorMessage(this.connectorMessageId, this.messageDetails, confirmation);
        } else {
            throw new IllegalArgumentException("Either messageContent or a messageConfirmation must be set!");
        }
        message.getMessageAttachments().addAll(this.messageAttachments);
        message.getMessageConfirmations().addAll(this.messageConfirmations);
        message.getMessageErrors().addAll(this.messageErrors);

        return message;
    }

}
