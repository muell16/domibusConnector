package eu.domibus.connector.gateway.link.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformer;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.gateway.link.DomibusConnectorSubmissionService;
import eu.domibus.connector.ws.submission.service.DomibusConnectorSubmissionWS;

@Component
public class DomibusConnectorSubmissionServiceClient implements DomibusConnectorSubmissionService {
	
	@Resource
	DomibusConnectorSubmissionWS submissionClient;

	@Override
	public boolean submitMessage(DomibusConnectorMessage message) {
		DomibusConnectorMessageType request = DomibusConnectorDomainMessageTransformer.transformDomainToTransition(message);
		
		DomibsConnectorAcknowledgementType ack = submissionClient.submitMessage(request);
		
		return ack.isResult(); 
	}

}
