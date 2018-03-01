package eu.domibus.connector.gateway.link.impl;

import javax.annotation.Resource;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformer;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.ws.gateway.submission.webservice.DomibusConnectorGatewaySubmissionWebService;

@Component
public class DomibusConnectorGatewaySubmissionServiceClient implements DomibusConnectorGatewaySubmissionService {
	
	@Resource(name="gwSubmissionClient")
	DomibusConnectorGatewaySubmissionWebService submissionClient;

	@Override
	public void submitToGateway(DomibusConnectorMessage message) throws DomibusConnectorGatewaySubmissionException {
		DomibusConnectorMessageType request = DomibusConnectorDomainMessageTransformer.transformDomainToTransition(message);
		
		DomibsConnectorAcknowledgementType ack = submissionClient.submitMessage(request);
		
		if(ack==null || !ack.isResult()) {
			if(ack!=null && !StringUtils.isEmpty(ack.getResultMessage()))
				throw new DomibusConnectorGatewaySubmissionException(ack.getResultMessage());
			else
				throw new DomibusConnectorGatewaySubmissionException("Undefined submission error!");
		}
	}

}
