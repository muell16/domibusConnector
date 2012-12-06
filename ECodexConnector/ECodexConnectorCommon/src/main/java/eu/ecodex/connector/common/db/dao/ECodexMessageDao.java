package eu.ecodex.connector.common.db.dao;

import eu.ecodex.connector.common.db.model.ECodexMessage;

public interface ECodexMessageDao {

    void saveNewMessage(ECodexMessage message);

    void mergeMessage(ECodexMessage message);
}
