package eu.domibus.connector.controller.service;

import eu.domibus.connector.common.exception.DomibusConnectorMessageException;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;

public interface MessageService {

    void handleMessage(Message message) throws DomibusConnectorControllerException, DomibusConnectorMessageException;
}
