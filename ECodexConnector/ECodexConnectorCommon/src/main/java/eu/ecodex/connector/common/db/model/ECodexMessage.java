package eu.ecodex.connector.common.db.model;

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

import eu.ecodex.connector.common.enums.ECodexMessageDirection;

@Entity
@Table(name = "ECODEX_MESSAGES")
public class ECodexMessage {

    @Id
    @TableGenerator(name = "ecodexSeqStore", table = "ECODEX_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "ECODEX_MESSAGES.ID", valueColumnName = "SEQ_VALUE", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ecodexSeqStore")
    private Long id;

    @Column(name = "EBMS_MESSAGE_ID", unique = true)
    private String ebmsMessageId;

    @Column(name = "NAT_MESSAGE_ID", unique = true)
    private String nationalMessageId;

    @Column(name = "CONVERSATION_ID")
    private String conversationId;

    @Column(name = "DIRECTION")
    @Enumerated(EnumType.STRING)
    private ECodexMessageDirection direction;

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
    private ECodexMessageInfo messageInfo;

    @OneToMany(mappedBy = "message", fetch = FetchType.EAGER)
    private Set<ECodexEvidence> evidences;

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

    public ECodexMessageDirection getDirection() {
        return direction;
    }

    public void setDirection(ECodexMessageDirection direction) {
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

    public ECodexMessageInfo getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(ECodexMessageInfo messageInfo) {
        this.messageInfo = messageInfo;
    }

    public Set<ECodexEvidence> getEvidences() {
        return evidences;
    }

    public void setEvidences(Set<ECodexEvidence> evidences) {
        this.evidences = evidences;
    }
}
