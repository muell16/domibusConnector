package eu.ecodex.connector.common.db.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import eu.ecodex.connector.common.enums.ECodexMessageDirection;

@Entity
@Table(name = "ECODEX_MESSAGES")
@SequenceGenerator(sequenceName = "ECMSG_SEQ", name = "ECMSG_SEQ_GEN")
public class ECodexMessage {

    @Id
    @GeneratedValue(generator = "ECMSG_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "EBMS_MESSAGE_ID")
    private String ebmsMessageId;

    @Column(name = "NAT_MESSAGE_ID")
    private String nationalMessageId;

    @Column(name = "CONVERSATION_ID")
    private String conversationId;

    @Column(name = "DIRECTION")
    @Enumerated(EnumType.STRING)
    private ECodexMessageDirection direction;

    @Column(name = "HASH_VALUE")
    private String hashValue;

    @Column(name = "UPDATED")
    private Date updated;

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

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
