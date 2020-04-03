package eu.domibus.connector.persistence.model;

import eu.domibus.connector.domain.model.DetachedSignatureMimeType;

import javax.persistence.*;

@Entity
@Table(name = PDomibusConnectorDetachedSignature.TABLE_NAME)
//@IdClass(PDomibusConnectorDetachedSignaturePK.class)
public class PDomibusConnectorDetachedSignature {

    public static final String TABLE_NAME = "DC_MSG_CONTENT_DETACHED_SIGNATURE";

    @Id
    private Long id;


    @MapsId
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "ID", referencedColumnName = "ID")
    PDomibusConnectorMsgCont content;

    @Lob
    @Column(name = "SIGNATURE")
    private byte detachedSignature[];

    @Column(name = "SIGNATURE_NAME")
    private String detachedSignatureName;

    @Column(name = "SIGNATURE_TYPE")
    private DetachedSignatureMimeType mimeType;

    public PDomibusConnectorMsgCont getContent() {
        return content;
    }

    public void setContent(PDomibusConnectorMsgCont content) {
        this.content = content;
    }

    public byte[] getDetachedSignature() {
        return detachedSignature;
    }

    public void setDetachedSignature(byte[] detachedSignature) {
        this.detachedSignature = detachedSignature;
    }

    public String getDetachedSignatureName() {
        return detachedSignatureName;
    }

    public void setDetachedSignatureName(String detachedSignatureName) {
        this.detachedSignatureName = detachedSignatureName;
    }

    public DetachedSignatureMimeType getMimeType() {
        return mimeType;
    }

    public void setMimeType(DetachedSignatureMimeType mimeType) {
        this.mimeType = mimeType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
