package eu.domibus.connector.common.db.dao;

import java.util.List;

import eu.domibus.connector.common.db.model.DomibusConnectorMessage;

public interface DomibusConnectorMessageDao {

    void saveNewMessage(DomibusConnectorMessage message);

    DomibusConnectorMessage mergeMessage(DomibusConnectorMessage message);

    DomibusConnectorMessage findMessageByNationalId(String nationalMessageId);

    DomibusConnectorMessage findMessageByEbmsId(String ebmsMessageId);

    List<DomibusConnectorMessage> findOutgoingUnconfirmedMessages();

    List<DomibusConnectorMessage> findIncomingUnconfirmedMessages();

    DomibusConnectorMessage confirmMessage(DomibusConnectorMessage message);

    DomibusConnectorMessage rejectMessage(DomibusConnectorMessage message);

    List<DomibusConnectorMessage> findMessagesByConversationId(String conversationId);
}
