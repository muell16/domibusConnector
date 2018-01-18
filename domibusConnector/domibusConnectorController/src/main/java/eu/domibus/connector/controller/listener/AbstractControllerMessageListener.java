package eu.domibus.connector.controller.listener;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;

public abstract class AbstractControllerMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(BackendToGatewayMessageListener.class);
	
	
	@Resource
	private DomibusConnectorPersistenceService persistenceService;
	
	void handleMessage(Message message, String queueName) {
		try {
			if (message instanceof TextMessage) {
				TextMessage msg = (TextMessage) message;

				String connectorMessageId = msg.getText();
				logger.info("received messageID {} from queue {}.", connectorMessageId);
				DomibusConnectorMessage connectorMessage = null;
				try {
					connectorMessage = persistenceService.findMessageByConnectorMessageId(connectorMessageId);
				} catch (PersistenceException e) {
					logger.error("Message {} could not be loaded from database!", connectorMessageId, e);
				}
				
				if(connectorMessage!=null) {
					startProcessing(connectorMessage);
				}else {
					logger.error("Message {} is null!");
				}
				
			} else {
				throw new IllegalArgumentException("Message must be of type TextMessage");
			}
		} catch (JMSException e) {
			logger.error("Exception receiving message from queue.");
		}
	}
	
	abstract void startProcessing(DomibusConnectorMessage connectorMessage);

}
