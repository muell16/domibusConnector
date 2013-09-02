package eu.ecodex.connector.common.db.dao;

import eu.ecodex.connector.common.db.model.ECodexMessage;
import eu.ecodex.connector.common.db.model.ECodexMessageInfo;
import eu.ecodex.connector.common.exception.PersistenceException;

public interface ECodexMessageInfoDao {

    void persistMessageInfo(ECodexMessageInfo messageInfo);

    void mergeMessageInfo(ECodexMessageInfo messageInfo);

    ECodexMessageInfo getMessageInfoForMessage(ECodexMessage message) throws PersistenceException;

}
