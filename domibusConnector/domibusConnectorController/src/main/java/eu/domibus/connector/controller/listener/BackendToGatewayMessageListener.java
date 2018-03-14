package eu.domibus.connector.controller.listener;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.controller.process.DomibusConnectorMessageProcessor;

@Component("BackendToGatewayMessageListener")
public class BackendToGatewayMessageListener extends AbstractControllerMessageListener implements MessageListener {
	
	private static final String QUEUE_NAME = "domibus.connector.internal.backend.to.controller.queue";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BackendToGatewayMessageListener.class);
	
	@Resource(name="BackendToGatewayMessageProcessor")
	private DomibusConnectorMessageProcessor messageProcessor;
	
	@Resource(name="BackendToGatewayConfirmationProcessor")
	private DomibusConnectorMessageProcessor confirmationProcessor;
	
	@Override
	@Transactional
	@JmsListener(destination = "${"+ QUEUE_NAME +"}") //Funktioniert das????
	public void onMessage(Message message) {
		handleMessage(message);
	}

	@Override
	Logger getLogger() {
		return LOGGER;
	}

	@Override
	String getQueueName() {
		return QUEUE_NAME;
	}

	@Override
	DomibusConnectorMessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	@Override
	DomibusConnectorMessageProcessor getConfirmationProcessor() {
		return confirmationProcessor;
	}

}
