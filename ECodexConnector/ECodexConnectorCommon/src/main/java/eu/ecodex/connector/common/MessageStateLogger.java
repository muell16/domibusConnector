package eu.ecodex.connector.common;

import eu.ecodex.connector.common.db.MessageStateLogDao;

public class MessageStateLogger {

    private MessageStateLogDao loggerDao;

    public void setLoggerDao(MessageStateLogDao loggerDao) {
        this.loggerDao = loggerDao;
    }

    public void logMessageState(String messageId, MessageState messageState) {
        loggerDao.saveMessageStateLog(messageId, messageState);
    }
}
