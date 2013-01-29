package eu.ecodex.connector.common.db.dao;

import java.util.List;

import eu.ecodex.connector.common.db.model.ECodexMessage;

public interface ECodexMessageDao {

    void saveNewMessage(ECodexMessage message);

    ECodexMessage mergeMessage(ECodexMessage message);

    ECodexMessage findMessageByNationalId(String nationalMessageId);

    ECodexMessage findMessageByEbmsId(String ebmsMessageId);

    List<ECodexMessage> findOutgoingUnconfirmedMessages();

    List<ECodexMessage> findIncomingUnconfirmedMessages();
}
