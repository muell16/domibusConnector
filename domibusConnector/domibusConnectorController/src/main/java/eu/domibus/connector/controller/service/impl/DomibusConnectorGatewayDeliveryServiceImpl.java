package eu.domibus.connector.controller.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;

@Component("domibusConnectorGatewayDeliveryServiceImpl")
public class DomibusConnectorGatewayDeliveryServiceImpl implements DomibusConnectorGatewayDeliveryService {

	@Resource
	private DomibusConnectorPersistenceService persistenceService;
	
	@Override
	public void deliverMessageFromGateway(DomibusConnectorMessage message) throws DomibusConnectorControllerException {
		
		//TODO: generate connector-internal message id
		
		try {
			persistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.GW_TO_NAT);
		}catch(PersistenceException e) {
			throw new DomibusConnectorControllerException("Message could not be persisted! ", e);
		}
		
		//TODO: persist message content over own persistence service
		
		//TODO: store message on internal queue for processing
	
	}

}
