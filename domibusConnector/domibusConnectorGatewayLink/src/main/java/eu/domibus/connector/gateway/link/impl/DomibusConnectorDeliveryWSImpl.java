package eu.domibus.connector.gateway.link.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformer;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;
import eu.domibus.connector.ws.delivery.service.DomibusConnectorDeliveryWS;

@Component("domibusConnectorDeliveryServiceImpl")
public class DomibusConnectorDeliveryWSImpl implements DomibusConnectorDeliveryWS {

	@Resource
	private DomibusConnectorPersistenceService persistenceService;
	
	@Override
	public DomibsConnectorAcknowledgementType deliverMessage(DomibusConnectorMessageType deliverMessageRequest) {
		DomibusConnectorMessage domainMessage = DomibusConnectorDomainMessageTransformer.transformTransitionToDomain(deliverMessageRequest);
		
		DomibsConnectorAcknowledgementType ack = new DomibsConnectorAcknowledgementType();
		try {
			persistenceService.persistMessageIntoDatabase(domainMessage, DomibusConnectorMessageDirection.GW_TO_NAT);
		} catch (PersistenceException e) {
			ack.setResult(false);
			ack.setResultMessage("Message could not be persisted into database!");
			return ack;
		}
		ack.setResult(true);
		return ack;
	}

}
