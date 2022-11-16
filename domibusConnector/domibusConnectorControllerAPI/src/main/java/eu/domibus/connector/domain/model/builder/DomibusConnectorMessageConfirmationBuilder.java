package eu.domibus.connector.domain.model.builder;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.ecodex.dc5.message.model.DC5Confirmation;
import org.apache.commons.lang3.ArrayUtils;

public final class DomibusConnectorMessageConfirmationBuilder {

    public static DomibusConnectorMessageConfirmationBuilder createBuilder() { return new DomibusConnectorMessageConfirmationBuilder(); };

    private DomibusConnectorEvidenceType evidenceType;
    private byte evidence[];

    private DomibusConnectorMessageConfirmationBuilder() {}


    public DomibusConnectorMessageConfirmationBuilder setEvidenceType(DomibusConnectorEvidenceType evidenceType) {
        this.evidenceType = evidenceType;
        return this;
    }

    public DomibusConnectorMessageConfirmationBuilder setEvidence(byte[] evidence) {
        this.evidence = evidence;
        return this;
    }

    public DC5Confirmation build() {
        if (evidence == null) {
            evidence = new byte[0];
            //throw new IllegalArgumentException("Evidence is not allowed to be null!");
        }
        if (evidenceType == null) {
            throw new IllegalArgumentException("Evidence type must be set!");
        }
        return DC5Confirmation.builder()
                .evidenceType(evidenceType)
                .evidence(evidence)
                .build();
    }

    public DomibusConnectorMessageConfirmationBuilder copyPropertiesFrom(DC5Confirmation c) {
        if (c == null) {
            throw new IllegalArgumentException("Cannot copy properties from null object!");
        }
        this.evidence = ArrayUtils.clone(c.getEvidence());
        this.evidenceType = c.getEvidenceType();
        return this;
    }
}
