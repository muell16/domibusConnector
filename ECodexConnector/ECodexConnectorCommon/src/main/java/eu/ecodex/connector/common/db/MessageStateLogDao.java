package eu.ecodex.connector.common.db;

import eu.ecodex.connector.common.MessageState;

public interface MessageStateLogDao {

    void saveMessageStateLog(String messageId, MessageState messageState);

}
