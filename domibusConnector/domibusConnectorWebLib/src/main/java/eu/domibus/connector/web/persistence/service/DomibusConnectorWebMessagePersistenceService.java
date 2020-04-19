package eu.domibus.connector.web.persistence.service;

import java.util.Date;
import java.util.LinkedList;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.dto.WebMessageDetail;

public interface DomibusConnectorWebMessagePersistenceService {
	LinkedList<WebMessage> getAllMessages();
	WebMessageDetail getMessageByConnectorId(String connectorMessageId);
	LinkedList<WebMessage> getMessagesWithinPeriod(Date from, Date to);
	WebMessageDetail findMessageByNationalId(String nationalMessageId, DomibusConnectorMessageDirection direction);
	WebMessageDetail findMessageByEbmsId(String ebmsMessageId, DomibusConnectorMessageDirection direction);
	LinkedList<WebMessage> findMessagesByConversationId(String conversationId);
}