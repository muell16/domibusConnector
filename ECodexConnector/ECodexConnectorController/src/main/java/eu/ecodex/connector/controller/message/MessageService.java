package eu.ecodex.connector.controller.message;

import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;

public interface MessageService {

    void handleMessage(String messageId) throws ECodexConnectorControllerException;
}
