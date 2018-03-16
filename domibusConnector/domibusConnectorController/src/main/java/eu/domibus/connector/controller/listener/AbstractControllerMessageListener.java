package eu.domibus.connector.controller.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import eu.domibus.connector.controller.helper.SetMessageOnLoggingContext;
import eu.domibus.connector.controller.process.DomibusConnectorMessageProcessor;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.slf4j.Logger;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;


public abstract class AbstractControllerMessageListener {
	
	
	@Autowired
	private DomibusConnectorMessagePersistenceService persistenceService;
	
	void handleMessage(Message message) {
		getLogger().debug("#handleMessage: jms message: [{}]", message);
		try {
			if (message instanceof TextMessage) {
				TextMessage msg = (TextMessage) message;

				String connectorMessageId = msg.getText();
				SetMessageOnLoggingContext.putConnectorMessageIdOnMDC(connectorMessageId);
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
	
	void startProcessing(DomibusConnectorMessage connectorMessage) {
		getLogger().trace("#startProcessing: with message [{}]", connectorMessage);
		try {
			if (connectorMessage.getMessageContent() != null) {
				// as there is a message content, it cannot only be a confirmation message
				getLogger().trace("#startProcessing: with message [{}] as message", connectorMessage);
				getMessageProcessor().processMessage(connectorMessage);
			} else if (!CollectionUtils.isEmpty(connectorMessage.getMessageConfirmations())) {
				// as there is no message content, but at least one message confirmation,
				// it is a confirmation message
				getLogger().trace("#startProcessing: with message [{}] as confirmation", connectorMessage);
				getConfirmationProcessor().processMessage(connectorMessage);
			}
		} catch (Exception e) {
			getLogger().error("#startProcessing: Exception occured ", e);
			//TODO: put message on error queue or reprocess?
		}
	}
	
	abstract Logger getLogger();
	
	abstract String getQueueName();

	abstract DomibusConnectorMessageProcessor getMessageProcessor();

	abstract DomibusConnectorMessageProcessor getConfirmationProcessor();

}
