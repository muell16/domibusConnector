package eu.ecodex.connector.common.db;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import eu.ecodex.connector.common.MessageState;

@Entity
public class MessageStateLog {

    @Id
    @GeneratedValue
    private Long id;
    private String messageId;
    private final MessageState messageState;
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
