package eu.domibus.connector.ui.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedList;

@Getter
@Setter
public class WebMessage {

	@Override
	public String toString() {
		return "WebMessage [connectorMessageId=" + connectorMessageId + ", ebmsMessageId=" + ebmsId
				+ ", backendMessageId=" + backendMessageId + ", created=" + created + ", messageInfo=" + messageInfo
				+ "]";
	}

	private String connectorMessageId;

	private String fromParty;
	private String toParty;
	private String domain;
	private String ebmsId;
	private String initiator;
	private String responder;
	private String backendMessageId;
	private String conversationId;

	private String backendName;
	private String messageDirection;

	private String messageContentState;
	private String prvStates;

	// display all those fields in ui
	private LocalDateTime deliveredToNationalSystem;
	private LocalDateTime deliveredToGateway;
	private LocalDateTime created;
	private LocalDateTime confirmed;
	private LocalDateTime rejected;

//	----
	private WebMessageDetail messageInfo = new WebMessageDetail();
	private LinkedList<WebMessageEvidence> evidences = new LinkedList<>();
	private LinkedList<WebMessageFile> files = new LinkedList<>();

}
