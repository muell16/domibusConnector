package eu.domibus.connector.gateway.link.ws.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.ws.gateway.webservice.DomibusConnectorGatewayWebService;
import eu.domibus.connector.ws.gateway.webservice.MessageIds;

@Service
@Profile("gwlink-ws")
@ConditionalOnBean(DomibusConnectorGatewayWebService.class)
public class DomibusConnectorGatewayWebserviceClient implements DomibusConnectorGatewayWebService {

	
	@Override
	public DomibsConnectorAcknowledgementType submitMessage(DomibusConnectorMessageType submitMessageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageIds listPendingMessageIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DomibusConnectorMessageType getMessageById(String getMessageByIdRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
