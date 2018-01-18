package eu.domibus.connector.controller.service.impl;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;

@Component("domibusConnectorGatewayDeliveryServiceImpl")
public class DomibusConnectorGatewayDeliveryServiceImpl implements DomibusConnectorGatewayDeliveryService {

	private static final Logger logger = LoggerFactory.getLogger(DomibusConnectorGatewayDeliveryServiceImpl.class);
	
	@Value("${domibus.connector.internal.gateway.to.controller.queue}")
	private String internalGWToControllerQueueName;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Resource
	private DomibusConnectorPersistenceService persistenceService;
	
	@Resource
	private DomibusConnectorMessageIdGenerator messageIdGenerator;
	
	@Override
	public void deliverMessageFromGateway(DomibusConnectorMessage message) throws DomibusConnectorControllerException {
		
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
	
	private void putMessageOnMessageQueue(String connectorMessageId) {
		logger.debug("Putting message '{}' on queue '{}'.", connectorMessageId, internalGWToControllerQueueName);
		try {
			jmsTemplate.send(internalGWToControllerQueueName, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(connectorMessageId);
				}

			});
		} catch (JmsException je) {
			logger.error("Exception putting message '{}' on queue '{}'!", connectorMessageId, internalGWToControllerQueueName);
		}

		logger.info("Message with ID '{}' put on queue'{}'.", connectorMessageId, internalGWToControllerQueueName);
	}

}
