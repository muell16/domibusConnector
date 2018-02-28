package eu.domibus.connector.gateway.link.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformer;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWebService;

@Component("domibusConnectorDeliveryServiceImpl")
public class DomibusConnectorDeliveryWSImpl implements DomibusConnectorGatewayDeliveryWebService {

	@Resource
	private DomibusConnectorGatewayDeliveryService controllerService;
	
	@Override
	public DomibsConnectorAcknowledgementType deliverMessage(DomibusConnectorMessageType deliverMessageRequest) {
		DomibusConnectorMessage domainMessage = DomibusConnectorDomainMessageTransformer.transformTransitionToDomain(deliverMessageRequest);
		
		DomibsConnectorAcknowledgementType ack = new DomibsConnectorAcknowledgementType();
		try {
			controllerService.deliverMessageFromGateway(domainMessage);
		} catch (DomibusConnectorControllerException e) {
			ack.setResult(false);
			ack.setResultMessage("Message could not be processed! "+ e.getMessage());
			return ack;
		}
		ack.setResult(true);
		return ack;
	}

}
