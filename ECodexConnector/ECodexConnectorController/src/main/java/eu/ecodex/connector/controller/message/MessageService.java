package eu.ecodex.connector.controller.message;

import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;

public interface MessageService {

    void handleMessage(Message message) throws ECodexConnectorControllerException;
}
