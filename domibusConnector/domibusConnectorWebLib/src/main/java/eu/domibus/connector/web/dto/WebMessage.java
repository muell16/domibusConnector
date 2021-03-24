package eu.domibus.connector.web.dto;

import java.time.ZonedDateTime;
import java.util.LinkedList;

public class WebMessage {

	private String connectorMessageId;
	private String ebmsMessageId;
	private String backendMessageId;
	private String conversationId;
	private String backendName;
	private String directionSource;
	private String directionTarget;
	private ZonedDateTime deliveredToNationalSystem;
	private ZonedDateTime deliveredToGateway;
	private ZonedDateTime created;
	private ZonedDateTime confirmed;
	private ZonedDateTime rejected;
	private WebMessageDetail messageInfo = new WebMessageDetail();
	private LinkedList<WebMessageEvidence> evidences = new LinkedList<WebMessageEvidence>();
	
		
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


	

	public String getBackendName() {
		return backendName;
	}


	public void setBackendName(String backendClient) {
		this.backendName = backendClient;
	}



	public String getConversationId() {
		return conversationId;
	}


	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}


	public String getDirectionSource() {
		return directionSource;
	}


	public void setDirectionSource(String directionSource) {
		this.directionSource = directionSource;
	}


	public String getDirectionTarget() {
		return directionTarget;
	}


	public void setDirectionTarget(String directionTarget) {
		this.directionTarget = directionTarget;
	}


	public ZonedDateTime getConfirmed() {
		return confirmed;
	}


	public void setConfirmed(ZonedDateTime confirmed) {
		this.confirmed = confirmed;
	}


	public WebMessageDetail getMessageInfo() {
		return messageInfo;
	}


	public void setMessageInfo(WebMessageDetail messageInfo) {
		this.messageInfo = messageInfo;
	}


	public ZonedDateTime getDeliveredToNationalSystem() {
		return deliveredToNationalSystem;
	}
	
	public String getDeliveredToNationalSystemString() {
		return deliveredToNationalSystem!=null?deliveredToNationalSystem.toString():null;
	}


	public void setDeliveredToNationalSystem(ZonedDateTime deliveredToBackend) {
		this.deliveredToNationalSystem = deliveredToBackend;
	}
	
	public void setDeliveredToNationalSystemString(String deliveredToBackend) {
	}


	public ZonedDateTime getDeliveredToGateway() {
		return deliveredToGateway;
	}
	
	public String getDeliveredToGatewayString() {
		return deliveredToGateway!=null?deliveredToGateway.toString():null;
	}


	public void setDeliveredToGateway(ZonedDateTime deliveredToGateway) {
		this.deliveredToGateway = deliveredToGateway;
	}

	public void setDeliveredToGatewayString(String deliveredToGateway) {
	}


	public ZonedDateTime getCreated() {
		return created;
	}
	
	public String getCreatedString() {
		return created!=null?created.toString():null;
	}


	public void setCreated(ZonedDateTime created) {
		this.created = created;
	}

	public void setCreatedString(String created) {
	}


	public ZonedDateTime getRejected() {
		return rejected;
	}


	public void setRejected(ZonedDateTime rejected) {
		this.rejected = rejected;
	}
	
	public void setConfirmedString(String confirmed) {
	}


	
	
	public String getRejectedString() {
		return rejected!=null?rejected.toString():null;
	}

	
	public void setRejectedString(String rejected) {
	}

	public LinkedList<WebMessageEvidence> getEvidences() {
		return evidences;
	}


}
