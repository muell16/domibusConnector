package eu.domibus.connector.common.db.model;

import java.util.Date;
import java.util.Set;

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

import eu.domibus.connector.common.enums.MessageDirection;

@Entity
@Table(name = "DOMIBUS_CONNECTOR_MESSAGES")
public class DomibusConnectorMessage {

    @Id
    @TableGenerator(name = "seqStore", table = "DOMIBUS_CONNECTOR_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "DOMIBUS_CONNECTOR_MESSAGES.ID", valueColumnName = "SEQ_VALUE", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStore")
    private Long id;

    @Column(name = "EBMS_MESSAGE_ID", unique = true)
    private String ebmsMessageId;

    @Column(name = "NAT_MESSAGE_ID", unique = true)
    private String nationalMessageId;

    @Column(name = "CONVERSATION_ID")
    private String conversationId;

    @Column(name = "DIRECTION")
    @Enumerated(EnumType.STRING)
    private MessageDirection direction;

    @Column(name = "HASH_VALUE")
    private String hashValue;

    @Column(name = "DELIVERED_NAT")
    private Date deliveredToNationalSystem;

    @Column(name = "DELIVERED_GW")
    private Date deliveredToGateway;

    @Column(name = "CONFIRMED")
    private Date confirmed;

    @Column(name = "REJECTED")
    private Date rejected;

    @Column(name = "UPDATED", nullable = false)
    private Date updated;

    @OneToOne(mappedBy = "message", fetch = FetchType.EAGER)
    private DomibusConnectorMessageInfo messageInfo;

    @OneToMany(mappedBy = "message", fetch = FetchType.EAGER)
    private Set<DomibusConnectorEvidence> evidences;

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

    public String getNationalMessageId() {
        return nationalMessageId;
    }

    public void setNationalMessageId(String nationalMessageId) {
        this.nationalMessageId = nationalMessageId;
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

    public DomibusConnectorMessageInfo getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(DomibusConnectorMessageInfo messageInfo) {
        this.messageInfo = messageInfo;
    }

    public Set<DomibusConnectorEvidence> getEvidences() {
        return evidences;
    }

    public void setEvidences(Set<DomibusConnectorEvidence> evidences) {
        this.evidences = evidences;
    }
}
