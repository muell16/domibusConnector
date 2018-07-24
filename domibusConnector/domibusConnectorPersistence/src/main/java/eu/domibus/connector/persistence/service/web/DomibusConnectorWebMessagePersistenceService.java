package eu.domibus.connector.persistence.service.web;

import java.util.LinkedList;

import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.dto.WebMessageDetail;

public interface DomibusConnectorWebMessagePersistenceService {
	LinkedList<WebMessage> getAllMessages();
	WebMessageDetail getMessageByConnectorId(String connectorMessageId);
}
