package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.process.MessageDeliveredToNationalSystemProcessor;
import eu.domibus.connector.controller.service.DomibusConnectorBackendSubmissionService;
import eu.domibus.connector.controller.service.queue.PutMessageOnQueue;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

@Component("DomibusConnectorBackendDeliveryServiceImpl")
public class DomibusConnectorBackendSubmissionServiceImpl implements DomibusConnectorBackendSubmissionService {
	
	private static final Logger logger = LoggerFactory.getLogger(DomibusConnectorBackendSubmissionServiceImpl.class);

	@Autowired
	@Qualifier(PutMessageOnQueue.BACKEND_TO_CONTROLLER_QUEUE)
	private PutMessageOnQueue putMessageOnQueue;

	@Autowired
	private MessageDeliveredToNationalSystemProcessor messageDeliveredToNationalSystemProcessor;

	@Override
	public void submitToController(DomibusConnectorMessage message) {
		putMessageOnQueue.putMessageOnMessageQueue(message);
	}

}
