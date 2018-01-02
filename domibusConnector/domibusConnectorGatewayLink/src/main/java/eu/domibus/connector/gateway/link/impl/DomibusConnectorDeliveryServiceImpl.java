package eu.domibus.connector.gateway.link.impl;

import org.springframework.stereotype.Component;

import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.ws.delivery.service.DomibusConnectorDeliveryWS;

@Component("domibusConnectorDeliveryServiceImpl")
public class DomibusConnectorDeliveryServiceImpl implements DomibusConnectorDeliveryWS {

	
	@Override
	public DomibsConnectorAcknowledgementType deliverMessage(DomibusConnectorMessageType deliverMessageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
