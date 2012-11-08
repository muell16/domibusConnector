package eu.ecodex.connector.common.message;

/**
 * This is a container object that represents all data of a message.
 * 
 * @author riederb
 * 
 */
public class Message {

    private final MessageDetails messageDetails;
    private final MessageContent messageContent;
    private MessageAttachment[] attachments;
    private MessageConfirmation[] confirmations;

    public Message(MessageDetails messageDetails, MessageContent messageContent) {
        super();
        this.messageDetails = messageDetails;
        this.messageContent = messageContent;
    }

    public MessageAttachment[] getAttachments() {
        return attachments;
    }

    public void setAttachments(MessageAttachment[] attachments) {
        this.attachments = attachments;
    }

    public MessageConfirmation[] getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(MessageConfirmation[] confirmations) {
        this.confirmations = confirmations;
    }

    public MessageDetails getMessageDetails() {
        return messageDetails;
    }

    public MessageContent getMessageContent() {
        return messageContent;
    }

}
