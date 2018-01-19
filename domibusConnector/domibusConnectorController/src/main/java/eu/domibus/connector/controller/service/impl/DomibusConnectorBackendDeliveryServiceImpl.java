package eu.domibus.connector.controller.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

@Component("DomibusConnectorBackendDeliveryServiceImpl")
public class DomibusConnectorBackendDeliveryServiceImpl extends AbstractDeliveryServiceImpl implements DomibusConnectorBackendDeliveryService {

	private static final Logger logger = LoggerFactory.getLogger(DomibusConnectorBackendDeliveryServiceImpl.class);
	
	@Value("${domibus.connector.internal.controller.to.backend.queue}")
	private String internalControllerToBackendQueueName;
	
	@Override
	public void deliverMessageToBackend(DomibusConnectorMessage message) throws DomibusConnectorControllerException {
		putMessageOnMessageQueue(message.getConnectorMessageId());
	}

	@Override
	Logger getLogger() {
		return logger;
	}

	@Override
	String getQueueName() {
		return internalControllerToBackendQueueName;
	}

}
