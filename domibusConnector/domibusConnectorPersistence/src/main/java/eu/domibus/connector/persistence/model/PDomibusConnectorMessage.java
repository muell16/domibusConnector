package eu.domibus.connector.persistence.model;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import eu.domibus.connector.persistence.model.enums.MessageDirection;
import java.io.Serializable;
import javax.annotation.Nullable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.springframework.core.style.ToStringCreator;

@Entity
@Table(name = "DOMIBUS_CONNECTOR_MESSAGE")
public class PDomibusConnectorMessage implements Serializable {

    @Id
    @Column(name="ID")
    @TableGenerator(name = "seqStoreMessage", table = "DOMIBUS_CONNECTOR_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "DOMIBUS_CONNECTOR_MESSAGE.ID", valueColumnName = "SEQ_VALUE", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStoreMessage")
    private Long id;

    @Column(name = "EBMS_MESSAGE_ID", unique = true, length = 255)
    private String ebmsMessageId;

    @Column(name = "BACKEND_MESSAGE_ID", unique = true, length = 255)
    private String backendMessageId;

    @Column(name = "BACKEND_NAME", unique = true, length = 255)
    private String backendName;

    @Column(name = "CONNECTOR_MESSAGE_ID", unique = true, nullable = false, length = 255)
    private String connectorMessageId;
    
    @Column(name = "CONVERSATION_ID", length = 255)
    private String conversationId;

    @Column(name = "DIRECTION", length = 10)
    @Enumerated(EnumType.STRING)
    private MessageDirection direction;

    @Column(name = "HASH_VALUE")
    private String hashValue;

    @Column(name = "DELIVERED_BACKEND")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveredToNationalSystem;

    @Column(name = "DELIVERED_GW")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveredToGateway;

    @Column(name = "CONFIRMED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmed;

    @Column(name = "REJECTED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rejected;

    @Column(name = "UPDATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Column(name = "CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @OneToOne(mappedBy = "message", fetch = FetchType.EAGER)
    private PDomibusConnectorMessageInfo messageInfo;

    @OneToMany(mappedBy = "message", fetch = FetchType.EAGER)
    private Set<PDomibusConnectorEvidence> evidences = new HashSet<>();

    @PrePersist    
    public void prePersist() {
        this.updated = new Date();
        if (this.created == null) {
            this.created = this.updated;
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updated = new Date();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEbmsMessageId() {
        return ebmsMessageId;
    }

    public void setEbmsMessageId(String ebmsMessageId) {
        this.ebmsMessageId = ebmsMessageId;
    }

    public String getBackendMessageId() {
        return backendMessageId;
    }

    public void setBackendMessageId(String nationalMessageId) {
        this.backendMessageId = nationalMessageId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public MessageDirection getDirection() {
        return direction;
    }

    public void setDirection(MessageDirection direction) {
        this.direction = direction;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public Date getDeliveredToNationalSystem() {
        return deliveredToNationalSystem;
    }

    public void setDeliveredToNationalSystem(Date deliveredToNationalSystem) {
        this.deliveredToNationalSystem = deliveredToNationalSystem;
    }

    public Date getDeliveredToGateway() {
        return deliveredToGateway;
    }

    public void setDeliveredToGateway(Date deliveredToGateway) {
        this.deliveredToGateway = deliveredToGateway;
    }

    public Date getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Date confirmed) {
        this.confirmed = confirmed;
    }

    public Date getRejected() {
        return rejected;
    }

    public void setRejected(Date rejected) {
        this.rejected = rejected;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public PDomibusConnectorMessageInfo getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(PDomibusConnectorMessageInfo messageInfo) {
        this.messageInfo = messageInfo;
    }

    public @Nonnull Set<PDomibusConnectorEvidence> getEvidences() {
        return evidences;
    }

    public void setEvidences(Set<PDomibusConnectorEvidence> evidences) {
        this.evidences = evidences;
    }

    public String getConnectorMessageId() {
        return connectorMessageId;
    }

    public void setConnectorMessageId(String connectorMessageId) {
        this.connectorMessageId = connectorMessageId;
    }

    public String getBackendName() {
        return backendName;
    }

    public void setBackendName(String backendName) {
        this.backendName = backendName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        ToStringCreator builder = new ToStringCreator(this)
                .append("dbId", this.id)
                .append("connectorMessageid", this.connectorMessageId)
                .append("ebmsId", this.ebmsMessageId)
                .append("backendMessageId", this.backendMessageId)
                .append("deliveredToGw", this.deliveredToGateway);                
        return builder.toString();        
    }
    
}
