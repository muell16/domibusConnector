package org.holodeck.common.logging.model;


/**
 * @author Hamid Ben Malek
 */
public class MsgInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1200796957717630663L;

	protected String mpc;

	protected String messageId;

	protected String refToMessageId;

	protected String sender;

	protected String fromRole;

	protected String recipient;

	protected String toRole;

	protected String agreementRef;

	protected String pmode;

	protected String service;

	protected String action;

	protected String conversationId;

	public MsgInfo() {
	}

	public MsgInfo(String mpc, String messageId, String refToMessageId,
			String agreementRef, String pmode, String service, String action,
			String conversationId) {
		this.mpc = mpc;
		this.messageId = messageId;
		this.refToMessageId = refToMessageId;
		this.agreementRef = agreementRef;
		this.pmode = pmode;
		this.service = service;
		this.action = action;
		this.conversationId = conversationId;
	}
	
	public MsgInfo(String messageId, String sender, String recipient) {
		this("", messageId, "", "", "", "", "", "");
		setSender(sender);
		setRecipient(recipient);
	}

	public String getMpc() {
		return mpc;
	}

	public void setMpc(String mpc) {
		this.mpc = mpc;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getRefToMessageId() {
		return refToMessageId;
	}

	public void setRefToMessageId(String refToMessageId) {
		this.refToMessageId = refToMessageId;
	}

	public String getFromRole() {
		return fromRole;
	}

	public void setFromRole(String fromRole) {
		this.fromRole = fromRole;
	}



	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
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

	public String getAgreementRef() {
		return agreementRef;
	}

	public void setAgreementRef(String agreementRef) {
		this.agreementRef = agreementRef;
	}

	public String getPmode() {
		return pmode;
	}

	public void setPmode(String pmode) {
		this.pmode = pmode;
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