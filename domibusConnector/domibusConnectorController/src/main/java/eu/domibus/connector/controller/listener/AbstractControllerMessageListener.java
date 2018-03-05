package eu.domibus.connector.controller.listener;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;

public abstract class AbstractControllerMessageListener {
	
	
	@Resource
	private DomibusConnectorPersistenceService persistenceService;
	
	void handleMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				TextMessage msg = (TextMessage) message;

				String connectorMessageId = msg.getText();
				getLogger().info("received messageID [{}] from queue [{}].", connectorMessageId, getQueueName());
				DomibusConnectorMessage connectorMessage = null;
				try {
					connectorMessage = persistenceService.findMessageByConnectorMessageId(connectorMessageId);
				} catch (PersistenceException e) {
					getLogger().error("Message [{}] could not be loaded from database!", connectorMessageId, e);
				}
				
				if(connectorMessage!=null) {
					startProcessing(connectorMessage);
				}else {
					getLogger().error("Message [{}] is null!");
				}
				
			} else {
				throw new IllegalArgumentException("Message must be of type TextMessage");
			}
		} catch (JMSException e) {
			getLogger().error("Exception receiving message from queue [{}].", getQueueName());
		}
	}
	
	abstract void startProcessing(DomibusConnectorMessage connectorMessage);
	
	abstract Logger getLogger();
	
	abstract String getQueueName();

}
