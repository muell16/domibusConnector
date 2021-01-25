package eu.domibus.connector.persistence.model;


import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.persistence.*;

import eu.domibus.connector.domain.enums.MessageTargetSource;

import java.io.Serializable;

import eu.domibus.connector.persistence.model.converter.ZonedDateTimeToTimestampJpaConverter;
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

    @Column(name = "BACKEND_NAME", length = 255)
    private String backendName;

    @Column(name = "GATEWAY_NAME", length = 255)
    private String gatewayName;

    @Column(name = "CONNECTOR_MESSAGE_ID", unique = true, nullable = false, length = 255)
    private String connectorMessageId;
    
    @Column(name = "CONVERSATION_ID", length = 255)
    private String conversationId;

//    @Column(name = "DIRECTION", length = 10)
//    @Enumerated(EnumType.STRING)
//    private PMessageDirection direction;

    @Column(name = "DIRECTION_SOURCE", length = 10)
    private MessageTargetSource directionSource;

    @Column(name = "DIRECTION_TARGET", length = 10)
    private MessageTargetSource directionTarget;

    @Column(name = "HASH_VALUE")
    private String hashValue;

    @Column(name = "DELIVERED_BACKEND")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveredToNationalSystem;

    @Column(name = "DELIVERED_GW")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveredToGateway;

    @Convert(converter = ZonedDateTimeToTimestampJpaConverter.class)
    @Column(name = "CONFIRMED")
//    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime confirmed;

    @Convert(converter = ZonedDateTimeToTimestampJpaConverter.class)
    @Column(name = "REJECTED")
//    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime rejected;

    @Column(name = "UPDATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Column(name = "CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @OneToOne(mappedBy = "message", fetch = FetchType.EAGER)
    private PDomibusConnectorMessageInfo messageInfo;

    /**
     * This messages here are related to the BusinessMessage
     */
    @OneToMany(mappedBy = "businessMessage", fetch = FetchType.LAZY)
    private Set<PDomibusConnectorEvidence> relatedEvidences = new HashSet<>();

    /**
     * This evidences here are transported with the message
     */
    @OneToMany(mappedBy = "transportMessage", fetch = FetchType.EAGER)
    private Set<PDomibusConnectorEvidence> transportedEvidences = new HashSet<>();

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

    public Set<PDomibusConnectorEvidence> getTransportedEvidences() {
        return transportedEvidences;
    }

    public void setTransportedEvidences(Set<PDomibusConnectorEvidence> transportedEvidences) {
        this.transportedEvidences = transportedEvidences;
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

    public ZonedDateTime getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(ZonedDateTime confirmed) {
        this.confirmed = confirmed;
    }

    public ZonedDateTime getRejected() {
        return rejected;
    }

    public void setRejected(ZonedDateTime rejected) {
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

    public @Nonnull Set<PDomibusConnectorEvidence> getRelatedEvidences() {
        return relatedEvidences;
    }

    public void setRelatedEvidences(Set<PDomibusConnectorEvidence> evidences) {
        this.relatedEvidences = evidences;
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

    public MessageTargetSource getDirectionSource() {
        return directionSource;
    }

    public void setDirectionSource(MessageTargetSource directionSource) {
        this.directionSource = directionSource;
    }

    public MessageTargetSource getDirectionTarget() {
        return directionTarget;
    }

    public void setDirectionTarget(MessageTargetSource directionTarget) {
        this.directionTarget = directionTarget;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
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
