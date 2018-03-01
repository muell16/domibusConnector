package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.service.DomibusConnectorBackendSubmissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

@Component("DomibusConnectorBackendDeliveryServiceImpl")
public class DomibusConnectorBackendSubmissionServiceImpl extends AbstractDomibusConnectorControllerAPIServiceImpl
		implements DomibusConnectorBackendSubmissionService {
	
	private static final Logger logger = LoggerFactory.getLogger(DomibusConnectorBackendSubmissionServiceImpl.class);
	
	@Value("${domibus.connector.internal.backend.to.controller.queue}")
	private String internalBackendToControllerQueueName;


	@Override
	Logger getLogger() {
		return logger;
	}

	@Override
	String getQueueName() {
		return internalBackendToControllerQueueName;
	}

	@Override
	public void submitToController(DomibusConnectorMessage message) {
		putMessageOnMessageQueue(message.getConnectorMessageId());
	}

}
