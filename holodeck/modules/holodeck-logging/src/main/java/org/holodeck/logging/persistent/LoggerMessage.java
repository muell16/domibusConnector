package org.holodeck.logging.persistent;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * This Class represents a database table for logging ebms messages
 * 
 * @author Stefan Mueller
 * @author Tim Nowosadtko
 */
@Entity
@Table(name = "LoggerMessage")
public class LoggerMessage implements java.io.Serializable {
	private static final long serialVersionUID = 1200796957717630663L;
	
	public final static String MESSAGE_SENT_INIT_STATUS = "SENT_INIT";
	public final static String MESSAGE_SENT_OK_STATUS = "SENT_OK";
	public final static String MESSAGE_SENT_KO_STATUS = "SENT_KO";
	public final static String MESSAGE_RECEIVED_STATUS = "MESSAGE_RECEIVED";
	public final static String MESSAGE_DOWNLOADED_STATUS = "MESSAGE_DOWNLOADED";

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy="uuid")
	@Column(name = "id")
	protected String id;
	
	@Column(name = "messageId")
	protected String messageId;

	@Column(name = "sender")
	protected String sender;

	@Column(name = "fromRole")
	protected String fromRole;

	@Column(name = "recipient")
	protected String recipient;

	@Column(name = "toRole")
	protected String toRole;

	@Column(name = "service")
	protected String service;

	@Column(name = "action")
	protected String action;

	@Column(name = "conversationId")
	protected String conversationId;
	
	@Column(name = "pmode")
	protected String pmode;
	
	@Column(name = "timestamp")
	protected Date timestamp;
	
	@Column(name = "status")
	protected String status;


	public LoggerMessage() {
		timestamp = new Date();
	}
	

	public LoggerMessage(String messageId, String sender, String fromRole,
			String recipient, String toRole, String service, String action,
			String conversationId, String pmode, String status) {
		super();
		this.messageId = messageId;
		this.sender = sender;
		this.fromRole = fromRole;
		this.recipient = recipient;
		this.toRole = toRole;
		this.service = service;
		this.action = action;
		this.conversationId = conversationId;
		this.pmode = pmode;
		this.status = status;
		
		this.timestamp = new Date();
	}
	
	public LoggerMessage(MessageInfo mi){
		this.messageId = mi.getMessageId();
		this.sender = mi.getSender();
		this.fromRole = mi.getFromRole();
		this.recipient = mi.getRecipient();
		this.toRole = mi.getToRole();
		this.service = mi.getService();
		this.action = mi.getAction();
		this.conversationId = mi.getConversationId();
		this.pmode = mi.getPmode();
		this.status = mi.getStatus();
		
		
		this.timestamp = new Date();
	}
	
	/*
	 * Setter and Getter
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getPmode() {
		return pmode;
	}

	public void setPmode(String pmode) {
		this.pmode = pmode;
	}


	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getFromRole() {
		return fromRole;
	}

	public void setFromRole(String fromRole) {
		this.fromRole = fromRole;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getToRole() {
		return toRole;
	}

	public void setToRole(String toRole) {
		this.toRole = toRole;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}