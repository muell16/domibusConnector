package eu.domibus.connector.web.dto;

import java.util.Date;
import java.util.LinkedList;

public class WebMessageDetail extends WebMessage {

	private String backendMessageId;
	private String ebmsMessageId;
	private String conversationId;
	private String originalSender;
	private String finalRecipient;
	private String direction;
	private Date confirmed;
	private Date rejected;
	
	private LinkedList<WebMessageEvidence> evidences = new LinkedList<WebMessageEvidence>();
	private LinkedList<WebMessageError> errors = new LinkedList<WebMessageError>();
	

	public String getBackendMessageId() {
		return backendMessageId;
	}


	public void setBackendMessageId(String backendMessageId) {
		this.backendMessageId = backendMessageId;
	}


	public String getEbmsMessageId() {
		return ebmsMessageId;
	}


	public void setEbmsMessageId(String ebmsMessageId) {
		this.ebmsMessageId = ebmsMessageId;
	}


	public String getConversationId() {
		return conversationId;
	}


	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}


	public String getOriginalSender() {
		return originalSender;
	}


	public void setOriginalSender(String originalSender) {
		this.originalSender = originalSender;
	}


	public String getFinalRecipient() {
		return finalRecipient;
	}


	public void setFinalRecipient(String finalRecipient) {
		this.finalRecipient = finalRecipient;
	}


	public String getDirection() {
		return direction;
	}


	public void setDirection(String direction) {
		this.direction = direction;
	}


	public Date getConfirmed() {
		return confirmed;
	}
	
	public String getConfirmedString() {
		return confirmed!=null?confirmed.toString():null;
	}


	public void setConfirmed(Date confirmed) {
		this.confirmed = confirmed;
	}
	
	public void setConfirmedString(String confirmed) {
	}


	public Date getRejected() {
		return rejected;
	}
	
	public String getRejectedString() {
		return rejected!=null?rejected.toString():null;
	}


	public void setRejected(Date rejected) {
		this.rejected = rejected;
	}
	
	public void setRejectedString(String rejected) {
	}


	public LinkedList<WebMessageEvidence> getEvidences() {
		return evidences;
	}


	public LinkedList<WebMessageError> getErrors() {
		return errors;
	}


}
