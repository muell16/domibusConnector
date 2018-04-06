package eu.domibus.connector.controller.listener;

import javax.jms.Message;

import eu.domibus.connector.controller.process.DomibusConnectorMessageProcessor;
import eu.domibus.connector.controller.service.queue.GetDomibusConnectorMessageFromJmsMessage;
import org.slf4j.Logger;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;


public abstract class AbstractControllerMessageListener {
	
	
//	@Autowired
//	private DomibusConnectorMessagePersistenceService persistenceService;

	@Autowired
	GetDomibusConnectorMessageFromJmsMessage getFromJmsMessage;
	
	void handleMessage(Message message) {
		getLogger().debug("#handleMessage: jms message: [{}]", message);
		DomibusConnectorMessage domibusConnectorMessage = getFromJmsMessage.getMessage(message);
		startProcessing(domibusConnectorMessage);
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
