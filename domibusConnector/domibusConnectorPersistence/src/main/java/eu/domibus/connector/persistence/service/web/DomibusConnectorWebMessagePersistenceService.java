package eu.domibus.connector.persistence.service.web;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.dto.WebMessageDetail;

public interface DomibusConnectorWebMessagePersistenceService {
	LinkedList<WebMessage> getAllMessages();
	WebMessageDetail getMessageByConnectorId(String connectorMessageId);
	LinkedList<WebMessage> getMessagesWithinPeriod(Date from, Date to);
	WebMessageDetail findMessageByNationalId(String nationalMessageId);
	WebMessageDetail findMessageByEbmsId(String ebmsMessageId);
	LinkedList<WebMessage> findMessagesByConversationId(String conversationId);
}
