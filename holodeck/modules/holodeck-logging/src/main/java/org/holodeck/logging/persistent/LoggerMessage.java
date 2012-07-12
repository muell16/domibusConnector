package org.holodeck.logging.persistent;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author Stefan Mueller
 * @author Tim Nowosadtko
 */

@Entity
@Table(name = "LoggerMessage")
public class LoggerMessage implements java.io.Serializable {
	private static final long serialVersionUID = 1200796957717630663L;

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy="uuid")

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

	public LoggerMessage() {
	}
	

	public LoggerMessage(String messageId, String sender, String fromRole,
			String recipient, String toRole, String service, String action,
			String conversationId, String pmode) {
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

}