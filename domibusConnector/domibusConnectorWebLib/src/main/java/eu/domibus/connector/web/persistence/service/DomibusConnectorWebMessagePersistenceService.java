package eu.domibus.connector.web.persistence.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.Optional;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.dto.WebMessageDetail;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DomibusConnectorWebMessagePersistenceService {
	LinkedList<WebMessage> getAllMessages();
	Optional<WebMessageDetail> getMessageByConnectorId(String connectorMessageId);
	LinkedList<WebMessage> getMessagesWithinPeriod(Date from, Date to);
	Optional<WebMessageDetail>  findMessageByNationalId(String nationalMessageId, DomibusConnectorMessageDirection direction);
	Optional<WebMessageDetail>  findMessageByEbmsId(String ebmsMessageId, DomibusConnectorMessageDirection direction);
	LinkedList<WebMessage> findMessagesByConversationId(String conversationId);

	Page<WebMessage> findAll(Example<WebMessage> example, Pageable pageable);
//	long count(Example<WebMessage> example, Pageable pageable);
	long count(Example<WebMessage> example);
	Optional<WebMessageDetail> getOutgoingMessageByBackendMessageId(String backendMessageId);
	Optional<WebMessageDetail> getIncomingMessageByEbmsMessageId(String ebmsMessageId);

}
