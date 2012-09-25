package eu.ecodex.connector.common.db;

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

import eu.ecodex.connector.common.MessageState;

@Entity
@Table(name = "MESSAGE_STATE_LOG")
@SequenceGenerator(sequenceName = "ECMSL_SEQ", name = "ECMSL_SEQ_GEN")
public class MessageStateLog {

    @Id
    @GeneratedValue(generator = "ECMSL_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "MESSAGE_ID")
    private String messageId;
    @Column(name = "MESSAGE_STATE")
    @Enumerated(EnumType.STRING)
    private final MessageState messageState;
    @Column(name = "UPDATED")
    private Date updated;

    public MessageStateLog(String messageId, MessageState messageState, Date updated) {
        super();
        this.messageId = messageId;
        this.messageState = messageState;
        this.updated = updated;
    }

    public MessageStateLog(String messageId, MessageState messageState) {
        super();
        this.messageId = messageId;
        this.messageState = messageState;
        updated = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public MessageState getMessageState() {
        return messageState;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}
