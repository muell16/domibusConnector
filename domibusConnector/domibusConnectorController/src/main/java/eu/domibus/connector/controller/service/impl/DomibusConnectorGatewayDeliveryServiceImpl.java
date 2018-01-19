package eu.domibus.connector.controller.service.impl;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;

@Component("domibusConnectorGatewayDeliveryServiceImpl")
public class DomibusConnectorGatewayDeliveryServiceImpl extends AbstractDeliveryServiceImpl implements DomibusConnectorGatewayDeliveryService {

	private static final Logger logger = LoggerFactory.getLogger(DomibusConnectorGatewayDeliveryServiceImpl.class);
	
	@Value("${domibus.connector.internal.gateway.to.controller.queue}")
	private String internalGWToControllerQueueName;
	
	@Resource
	private DomibusConnectorPersistenceService persistenceService;
	
	@Resource
	private DomibusConnectorMessageIdGenerator messageIdGenerator;
	
	@Override
	public void deliverMessageFromGateway(DomibusConnectorMessage message) throws DomibusConnectorControllerException {
		
		//Check consistence of message:
		// Either a message content, or at least one confirmation must exist for processing
		if(!checkMessageForProcessability(message))
			throw new DomibusConnectorControllerException("Message cannot be processed as it contains neither message content, nor message confirmation!");
		
		String connectorMessageId = messageIdGenerator.generateDomibusConnectorMessageId();
		
		if(StringUtils.isEmpty(connectorMessageId))
			throw new DomibusConnectorControllerException("domibus connector message ID not generated!");
		
		message.setConnectorMessageId(connectorMessageId);
		
		try {
			persistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.GW_TO_NAT);
		}catch(PersistenceException e) {
			throw new DomibusConnectorControllerException("Message could not be persisted! ", e);
		}
		
		//TODO: persist message content over own persistence service
		
		putMessageOnMessageQueue(connectorMessageId);
	
	}
	
	private boolean checkMessageForProcessability(DomibusConnectorMessage message) {
		
		if(message == null)
			return false;
		
		if(message.getMessageContent()!=null)
			return true;
		
		if(!CollectionUtils.isEmpty(message.getMessageConfirmations()))
			return true;
		
		return false;
	}

	@Override
	Logger getLogger() {
		return logger;
	}

	@Override
	String getQueueName() {
		return internalGWToControllerQueueName;
	}

}
