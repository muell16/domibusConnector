package eu.domibus.connector.controller.service.queue;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

import javax.jms.Message;

public interface GetDomibusConnectorMessageFromJmsMessage {

    public DomibusConnectorMessage getMessage(Message message);

}
