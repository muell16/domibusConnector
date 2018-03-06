package eu.domibus.connector.controller.listener;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import eu.domibus.connector.controller.process.DomibusConnectorMessageProcessor;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

@Component("GatewayToBackendMessageListener")
public class GatewayToBackendMessageListener extends AbstractControllerMessageListener implements MessageListener {

	private static final String queueName = "domibus.connector.internal.gateway.to.controller.queue";
	
	private static final Logger logger = LoggerFactory.getLogger(BackendToGatewayMessageListener.class);
	
	@Resource(name="GatewayToBackendMessageProcessor")
	private DomibusConnectorMessageProcessor messageProcessor;
	
	@Resource(name="GatewayToBackendConfirmationProcessor")
	private DomibusConnectorMessageProcessor confirmationProcessor;
	
	@Override
	@Transactional
	@JmsListener(destination = "${"+queueName+"}") //Funktioniert das????
	public void onMessage(Message message) {
		handleMessage(message);
	}

	@Override
	Logger getLogger() {
		return logger;
	}

	@Override
	String getQueueName() {
		return queueName;
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
