package eu.domibus.connector.persistence.model;

import eu.domibus.connector.domain.model.DomibusConnectorMessageId;
import eu.domibus.connector.persistence.service.impl.helper.StoreType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;
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

    @Column(name = "PAYLOAD_SIZE")
    private long size = -1;

    @Column(name = "CONNECTOR_MESSAGE_ID")
    private DomibusConnectorMessageId messageId;

//    @ManyToOne
//    @JoinColumn(name="MESSAGE_ID")
    @Transient
    private PDomibusConnectorMessage message;

    @OneToOne(optional = true, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "DETACHED_SIGNATURE_ID", referencedColumnName = "ID")
    private PDomibusConnectorDetachedSignature detachedSignature;

    @javax.persistence.Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "DELETED")
    private Date deleted;

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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
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
    }

    public void setStorageReferenceId(String storageReferenceId) {
        this.storageReferenceId = storageReferenceId;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    public DomibusConnectorMessageId getMessageId() {
        return messageId;
    }

    public void setMessageId(DomibusConnectorMessageId messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        ToStringBuilder toString = new ToStringBuilder(this);
        toString.append("id", this.id);
        toString.append("messageId", this.getMessage().getId());
        return toString.build();
    }

}
