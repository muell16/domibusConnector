package eu.domibus.connector.controller.listener;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.controller.process.DomibusConnectorMessageProcessor;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;

@Component("GatewayToBackendMessageListener")
public class GatewayToBackendMessageListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(GatewayToBackendMessageListener.class);
	
	@Resource(name="GatewayToBackendMessageProcessor")
	private DomibusConnectorMessageProcessor messageProcessor;
	
	@Resource(name="GatewayToBackendConfirmationProcessor")
	private DomibusConnectorMessageProcessor confirmationProcessor;
	
	@Resource
	private DomibusConnectorPersistenceService persistenceService;
	
	@Override
	@Transactional
	@JmsListener(destination = "${domibus.connector.internal.gateway.to.controller.queue}") //Funktioniert das????
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				TextMessage msg = (TextMessage) message;

				String connectorMessageId = msg.getText();
				logger.info("received messageID {} from queue.", connectorMessageId);
				DomibusConnectorMessage connectorMessage = null;
				try {
					connectorMessage = persistenceService.findMessageByConnectorMessageId(connectorMessageId);
				} catch (PersistenceException e) {
					logger.error("Message {} could not be loaded", connectorMessageId, e);
				}
				
				if(connectorMessage!=null) {
					if(connectorMessage.getMessageContent()!=null) {
						// as there is a message content, it cannot only be a confirmation message
						messageProcessor.processMessage(connectorMessage);
					}else if(!CollectionUtils.isEmpty(connectorMessage.getMessageConfirmations())) {
						// as there is no message content, but at least one message confirmation,
						// it is a confirmation message
						confirmationProcessor.processMessage(connectorMessage);
					}
				}
				
			} else {
				throw new IllegalArgumentException("Message must be of type TextMessage");
			}
		} catch (JMSException e) {
			logger.error("Exception receiving message from queue.");
		}
		
	}

}
