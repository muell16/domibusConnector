package org.holodeck.logging.persistent;


/**
 * @author Stefan Mueller
 * @author Tim Nowosadtko
 */

public class MessageInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1200796957717630663L;

	protected String messageId;
	protected String sender;
	protected String fromRole;
	protected String recipient;
	protected String toRole;
	protected String service;
	protected String action;
	protected String conversationId;
	protected String pmode;

	public MessageInfo() {
	}
	
	public MessageInfo(String messageId, String sender, String fromRole,
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