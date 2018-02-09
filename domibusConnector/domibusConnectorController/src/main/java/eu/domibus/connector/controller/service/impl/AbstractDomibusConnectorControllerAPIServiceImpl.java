package eu.domibus.connector.controller.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public abstract class AbstractDomibusConnectorControllerAPIServiceImpl {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	void putMessageOnMessageQueue(String connectorMessageId) {
		getLogger().debug("Putting message '{}' on queue '{}'.", connectorMessageId, getQueueName());
		try {
			jmsTemplate.send(getQueueName(), new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(connectorMessageId);
				}

			});
		} catch (JmsException je) {
			getLogger().error("Exception putting message '{}' on queue '{}'!", connectorMessageId, getQueueName());
		}

		getLogger().info("Message with ID '{}' put on queue'{}'.", connectorMessageId, getQueueName());
	}
	
	abstract Logger getLogger();
	
	abstract String getQueueName();

}
