package eu.domibus.connector.web.dto;

import java.util.Date;

public class WebMessage {

	private String connectorMessageId;
	private String ebmsMessageId;
	private String backendMessageId;
	private String service;
	private String action;
	private String fromPartyId;
	private String fromPartyIdType;
	private String toPartyId;
	private String toPartyIdType;
	private String originalSender;
	private String finalRecipient;
	private String backendName;
	private Date deliveredToNationalSystem;
	private Date deliveredToGateway;
	private Date created;
	
	
	public String getConnectorMessageId() {
		return connectorMessageId;
	}


	public void setConnectorMessageId(String connectorMessageId) {
		this.connectorMessageId = connectorMessageId;
	}



	public String getEbmsMessageId() {
		return ebmsMessageId;
	}


	public void setEbmsMessageId(String ebmsMessageId) {
		this.ebmsMessageId = ebmsMessageId;
	}


	public String getBackendMessageId() {
		return backendMessageId;
	}


	public void setBackendMessageId(String backendMessageId) {
		this.backendMessageId = backendMessageId;
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


	public String getFromPartyId() {
		return fromPartyId;
	}


	public void setFromPartyId(String fromPartyId) {
		this.fromPartyId = fromPartyId;
	}


	public String getFromPartyIdType() {
		return fromPartyIdType;
	}


	public void setFromPartyIdType(String fromPartyIdType) {
		this.fromPartyIdType = fromPartyIdType;
	}


	public String getToPartyId() {
		return toPartyId;
	}


	public void setToPartyId(String toPartyId) {
		this.toPartyId = toPartyId;
	}


	public String getToPartyIdType() {
		return toPartyIdType;
	}


	public void setToPartyIdType(String toPartyIdType) {
		this.toPartyIdType = toPartyIdType;
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


	public String getBackendName() {
		return backendName;
	}


	public void setBackendName(String backendClient) {
		this.backendName = backendClient;
	}



	public Date getDeliveredToNationalSystem() {
		return deliveredToNationalSystem;
	}
	
	public String getDeliveredToNationalSystemString() {
		return deliveredToNationalSystem!=null?deliveredToNationalSystem.toString():null;
	}


	public void setDeliveredToNationalSystem(Date deliveredToBackend) {
		this.deliveredToNationalSystem = deliveredToBackend;
	}
	
	public void setDeliveredToNationalSystemString(String deliveredToBackend) {
	}


	public Date getDeliveredToGateway() {
		return deliveredToGateway;
	}
	
	public String getDeliveredToGatewayString() {
		return deliveredToGateway!=null?deliveredToGateway.toString():null;
	}


	public void setDeliveredToGateway(Date deliveredToGateway) {
		this.deliveredToGateway = deliveredToGateway;
	}

	public void setDeliveredToGatewayString(String deliveredToGateway) {
	}


	public Date getCreated() {
		return created;
	}
	
	public String getCreatedString() {
		return created!=null?created.toString():null;
	}


	public void setCreated(Date created) {
		this.created = created;
	}

	public void setCreatedString(String created) {
	}

}
