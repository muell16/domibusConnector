package eu.domibus.connector.persistence.model;

import eu.domibus.connector.persistence.service.impl.helper.StoreType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import javax.persistence.*;

/**
 *  This class stores message content metadata for
 *   <ul>
 *      <li>message attachments</li>
 *      <li>message content xml</li>
 *      <li>message content document</li>
 *   </ul>
 *
 *   The storage itself is delegated to a storage provider
 *    the name and reference of the storage provider is also stored
 * 
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Entity
@Table(name="DOMIBUS_CONNECTOR_MSG_CONT")
public class PDomibusConnectorMsgCont implements Serializable {

    @Id
    @Column(name="ID")
    @TableGenerator(name = "seqStoreMsgContent", table = "DOMIBUS_CONNECTOR_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "DOMIBUS_CONNECTOR_MSG_CONT.ID", valueColumnName = "SEQ_VALUE", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStoreMsgContent")
    private Long id;

    @Column(name = "STORAGE_PROVIDER_NAME")
    private String storageProviderName;

    @Column(name = "STORAGE_REFERENCE_ID")
    private String storageReferenceId;

    @Deprecated
    @Column(name="CONTENT")
    @Lob
    private byte[] content;

    @Deprecated
    @Column(name="CHECKSUM")
    private String checksum;

    @Column(name="DIGEST")
    private String digest;
    
    @Column(name="CONTENT_TYPE")
    private StoreType contentType;

    @Column(name = "PAYLOAD_NAME")
    private String payloadName;

    @Column(name = "PAYLOAD_IDENTIFIER")
    private String payloadIdentifier;

    @Column(name = "PAYLOAD_DESCRIPTION")
    private String payloadDescription;

    @Column(name = "PAYLOAD_MIMETYPE")
    private String payloadMimeType;
    
    @ManyToOne
    @JoinColumn(name="MESSAGE_ID")
    private PDomibusConnectorMessage message;

    @OneToOne(optional = true, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "content")
    private PDomibusConnectorDetachedSignature detachedSignature;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayloadName() {
        return payloadName;
    }

    public void setPayloadName(String payloadName) {
        this.payloadName = payloadName;
    }

    public String getPayloadMimeType() {
        return payloadMimeType;
    }

    public void setPayloadMimeType(String payloadMimeType) {
        this.payloadMimeType = payloadMimeType;
    }

    public String getPayloadIdentifier() {
        return payloadIdentifier;
    }

    public void setPayloadIdentifier(String payloadIdentifier) {
        this.payloadIdentifier = payloadIdentifier;
    }

    public String getPayloadDescription() {
        return payloadDescription;
    }

    public void setPayloadDescription(String payloadDescription) {
        this.payloadDescription = payloadDescription;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public StoreType getContentType() {
        return contentType;
    }

    public void setContentType(StoreType contentType) {
        this.contentType = contentType;
    }

    public PDomibusConnectorMessage getMessage() {
        return message;
    }

    public void setMessage(PDomibusConnectorMessage message) {
        this.message = message;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getStorageProviderName() {
        return storageProviderName;
    }

    public void setStorageProviderName(String storageProviderName) {
        this.storageProviderName = storageProviderName;
    }

    public String getStorageReferenceId() {
        return storageReferenceId;
    }

    public PDomibusConnectorDetachedSignature getDetachedSignature() {
        return detachedSignature;
    }

    public void setDetachedSignature(PDomibusConnectorDetachedSignature detachedSignature) {
        this.detachedSignature = detachedSignature;
        detachedSignature.setContent(this);
    }

    public void setStorageReferenceId(String storageReferenceId) {
        this.storageReferenceId = storageReferenceId;
    }

    @Override
    public String toString() {
        ToStringBuilder toString = new ToStringBuilder(this);
        toString.append("id", this.id);
        toString.append("messageId", this.getMessage().getId());
        return toString.build();
    }

}
