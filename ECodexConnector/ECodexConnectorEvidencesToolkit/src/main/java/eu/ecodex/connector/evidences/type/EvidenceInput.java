package eu.ecodex.connector.evidences.type;

public class EvidenceInput {

	String messageId;
	String messageState;	//TODO: unused
	String comment;
	String recipientAddress;
	String recipientName;

	public EvidenceInput() {
	}

	public EvidenceInput(String messageId, String messageState, String comment,
			String recipientAddress, String recipientName) {
		super();
		this.messageId = messageId;
		this.messageState = messageState;
		this.comment = comment;
		this.recipientAddress = recipientAddress;
		this.recipientName = recipientName;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessageState() {
		return messageState;
	}

	public void setMessageState(String messageState) {
		this.messageState = messageState;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getRecipientAddress() {
		return recipientAddress;
	}

	public void setRecipientAddress(String recipientAddress) {
		this.recipientAddress = recipientAddress;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
}
