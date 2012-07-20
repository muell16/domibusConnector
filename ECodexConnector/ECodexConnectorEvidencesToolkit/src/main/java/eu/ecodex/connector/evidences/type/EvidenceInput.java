package eu.ecodex.connector.evidences.type;

public class EvidenceInput {

	String messageId;
	String messageState;
	String comment;

	public EvidenceInput() {
	}

	public EvidenceInput(String messageId, String messageState, String comment) {
		super();
		this.messageId = messageId;
		this.messageState = messageState;
		this.comment = comment;
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

}
