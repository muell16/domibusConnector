package eu.domibus.connector.domain;

import java.util.ArrayList;
import java.util.List;


/**
 * This is a container object that represents all data of a message.
 * 
 * @author riederb
 * 
 */

public class Message {

    private final MessageDetails messageDetails;
    private final MessageContent messageContent;
    private List<MessageAttachment> attachments;
    private List<MessageConfirmation> confirmations;
    private Long dbMessageId;

    /**
     * This constructor is for messages which contain content and original
     * documents. {@link MessageDetails} and {@link MessageContent} are
     * mandatory as a minimum for a message.
     * 
     * @param messageDetails
     *            the message details concerning some information on the message
     * @param messageContent
     *            the structured content of the message
     */
    public Message(MessageDetails messageDetails, MessageContent messageContent) {
        super();
        this.messageDetails = messageDetails;
        this.messageContent = messageContent;
    }

    /**
     * This constructor is for evidence messages which are returned by the
     * partner gateway. {@link MessageDetails} and {@link MessageConfirmation}
     * are mandatory as a minimum for an evidence message.
     * 
     * @param messageDetails
     * @param messageConfirmation
     */
    public Message(MessageDetails messageDetails, MessageConfirmation messageConfirmation) {
        super();
        this.messageDetails = messageDetails;
        messageContent = null;
        addConfirmation(messageConfirmation);
    }

    public Message(MessageDetails messageDetails) {
        super();
        this.messageDetails = messageDetails;
        messageContent = null;
    }

    public void addConfirmation(MessageConfirmation confirmation) {
        if (confirmations == null) {
            confirmations = new ArrayList<MessageConfirmation>();
        }
        confirmations.add(confirmation);
    }

    public void addAttachment(MessageAttachment attachment) {
        if (attachments == null) {
            attachments = new ArrayList<MessageAttachment>();
        }
        attachments.add(attachment);
    }

    public MessageDetails getMessageDetails() {
        return messageDetails;
    }

    public MessageContent getMessageContent() {
        return messageContent;
    }

    public List<MessageAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MessageAttachment> attachments) {
        this.attachments = attachments;
    }

    public List<MessageConfirmation> getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(List<MessageConfirmation> confirmations) {
        this.confirmations = confirmations;
    }

    public Long getDbMessageId() {
        return dbMessageId;
    }

    public void setDbMessageId(Long dbMessageId) {
        this.dbMessageId = dbMessageId;
    }

}
