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

    /**
     * There is only a constructor available with messageDetails and
     * messageContent as they are mandatory as a minimum for a message.
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
