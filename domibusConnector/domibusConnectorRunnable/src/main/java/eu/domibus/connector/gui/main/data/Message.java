package eu.domibus.connector.gui.main.data;

import java.io.File;

public class Message {

	
	String fromPartyId;
	String fromPartyRole;
	String toPartyId;
	String toPartyRole;
	
	String nationalMessageId;
	String ebmsMessageId;
	
	String originalSender;
	String finalRecipient;
	
	String action;
	String service;
	
	String receivedTimestamp;
	
	File messageDir;
	
	public String getFromPartyId() {
		return fromPartyId;
	}
	public void setFromPartyId(String fromPartyId) {
		this.fromPartyId = fromPartyId;
	}
	public String getFromPartyRole() {
		return fromPartyRole;
	}
	public void setFromPartyRole(String fromPartyRole) {
		this.fromPartyRole = fromPartyRole;
	}
	public String getToPartyId() {
		return toPartyId;
	}
	public void setToPartyId(String toPartyId) {
		this.toPartyId = toPartyId;
	}
	public String getToPartyRole() {
		return toPartyRole;
	}
	public void setToPartyRole(String toPartyRole) {
		this.toPartyRole = toPartyRole;
	}
	public String getNationalMessageId() {
		return nationalMessageId;
	}
	public void setNationalMessageId(String nationalMessageId) {
		this.nationalMessageId = nationalMessageId;
	}
	public String getEbmsMessageId() {
		return ebmsMessageId;
	}
	public void setEbmsMessageId(String ebmsMessageId) {
		this.ebmsMessageId = ebmsMessageId;
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
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getReceivedTimestamp() {
		return receivedTimestamp;
	}
	public void setReceivedTimestamp(String receivedTimestamp) {
		this.receivedTimestamp = receivedTimestamp;
	}
	public File getMessageDir() {
		return messageDir;
	}
	public void setMessageDir(File messageDir) {
		this.messageDir = messageDir;
	}
	
	
	
}
