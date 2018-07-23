package eu.domibus.connector.persistence.service.web;

import java.util.LinkedList;

import eu.domibus.connector.web.dto.WebMessage;

public interface DomibusConnectorWebMessagePersistenceService {
	LinkedList<WebMessage> getAllMessages();
	WebMessage getMessageByConnectorId(String connectorMessageId);
}
