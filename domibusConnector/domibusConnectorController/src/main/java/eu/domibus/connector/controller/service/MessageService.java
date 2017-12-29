package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.domain.Message;

public interface MessageService {

    void handleMessage(Message message) throws DomibusConnectorControllerException, DomibusConnectorMessageException;
}
