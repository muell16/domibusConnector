package eu.ecodex.connector.common.message;

import eu.ecodex.connector.common.enums.ConfirmationStateEnum;

public class MessageConfirmation {

    private String messageId;
    private ConfirmationStateEnum confirmationState;
    private byte[] evidence;

    public MessageConfirmation(String messageId, ConfirmationStateEnum confirmationState, byte[] evidence) {
        super();
        this.messageId = messageId;
        this.confirmationState = confirmationState;
        this.evidence = evidence;
    }

    public MessageConfirmation(String messageId, ConfirmationStateEnum confirmationState) {
        super();
        this.messageId = messageId;
        this.confirmationState = confirmationState;
    }

    public MessageConfirmation() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public ConfirmationStateEnum getConfirmationState() {
        return confirmationState;
    }

    public void setConfirmationState(ConfirmationStateEnum confirmationState) {
        this.confirmationState = confirmationState;
    }

    public byte[] getEvidence() {
        return evidence;
    }

    public void setEvidence(byte[] evidence) {
        this.evidence = evidence;
    }

}
