package eu.ecodex.connector.common.message;

import eu.ecodex.connector.common.enums.ECodexEvidenceType;

/**
 * This is an object that internally represents the evidences for a message. It
 * contains the evidence itself as a byte[] containing a structured document,
 * and an enum type which describes the evidence type. The messageId field is
 * required to connect this confirmation to a message.
 * 
 * @author riederb
 * 
 */
public class MessageConfirmation {

    private String messageId;
    private ECodexEvidenceType evidenceType;
    private byte[] evidence;

    public MessageConfirmation(String messageId, ECodexEvidenceType evidenceType, byte[] evidence) {
        super();
        this.messageId = messageId;
        this.evidenceType = evidenceType;
        this.evidence = evidence;
    }

    public MessageConfirmation() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public ECodexEvidenceType getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(ECodexEvidenceType evidenceType) {
        this.evidenceType = evidenceType;
    }

    public byte[] getEvidence() {
        return evidence;
    }

    public void setEvidence(byte[] evidence) {
        this.evidence = evidence;
    }

}
