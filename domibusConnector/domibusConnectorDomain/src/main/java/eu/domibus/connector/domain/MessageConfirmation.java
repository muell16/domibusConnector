package eu.domibus.connector.domain;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;


/**
 * This is an object that internally represents the evidences for a message. It
 * contains the evidence itself as a byte[] containing a structured document,
 * and an enum type which describes the evidence type. To be able to connect the
 * confirmation to a message it should only be instantiated as a part of a
 * {@link Message} which contains {@link MessageDetails} with the messageId(s).
 * 
 * @author riederb
 * 
 */
public class MessageConfirmation {

    private DomibusConnectorEvidenceType evidenceType;
    private byte[] evidence;

    public MessageConfirmation(DomibusConnectorEvidenceType evidenceType, byte[] evidence) {
        super();
        this.evidenceType = evidenceType;
        this.evidence = evidence;
    }

    public MessageConfirmation(DomibusConnectorEvidenceType evidenceType) {
        super();
        this.evidenceType = evidenceType;
    }

    public MessageConfirmation() {
    }

    public DomibusConnectorEvidenceType getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(DomibusConnectorEvidenceType evidenceType) {
        this.evidenceType = evidenceType;
    }

    public byte[] getEvidence() {
        return evidence;
    }

    public void setEvidence(byte[] evidence) {
        this.evidence = evidence;
    }

}
