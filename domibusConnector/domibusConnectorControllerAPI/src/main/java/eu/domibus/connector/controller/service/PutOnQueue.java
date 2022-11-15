package eu.domibus.connector.controller.service;

import eu.ecodex.dc5.message.model.DomibusConnectorMessage;

public interface PutOnQueue {

    public void putOnQueue(DomibusConnectorMessage message);

    public javax.jms.Queue getQueue();

}
