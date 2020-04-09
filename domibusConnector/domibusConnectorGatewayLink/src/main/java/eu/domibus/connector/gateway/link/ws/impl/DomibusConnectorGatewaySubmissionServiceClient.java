package eu.domibus.connector.gateway.link.ws.impl;

import javax.annotation.Resource;

import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.transition.tools.PrintDomibusConnectorMessageType;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.apache.cxf.common.util.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.ws.gateway.submission.webservice.DomibusConnectorGatewaySubmissionWebService;
import org.springframework.stereotype.Service;

@Service
@Profile("gwlink-ws")
public class DomibusConnectorGatewaySubmissionServiceClient implements DomibusConnectorGatewaySubmissionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorGatewaySubmissionServiceClient.class);

	@Resource(name="gwSubmissionClient")
	private DomibusConnectorGatewaySubmissionWebService submissionClient;

	@Autowired
    private TransportStatusService transportStatusService;



	@Override
	public void submitToGateway(DomibusConnectorMessage message) throws DomibusConnectorGatewaySubmissionException {
		DomibusConnectorMessageType request = DomibusConnectorDomainMessageTransformerService.transformDomainToTransition(message);

		if (LOGGER.isTraceEnabled()) {
		    LOGGER.trace("Printing out request message:");
            LOGGER.trace("message=[{}]", PrintDomibusConnectorMessageType.messageToString(request));
        }


        TransportStatusService.DomibusConnectorTransportState state = new TransportStatusService.DomibusConnectorTransportState();
        TransportStatusService.TransportId transportId = transportStatusService.createOrGetTransportFor(message, new DomibusConnectorLinkPartner.LinkPartnerName("default_gw"));

        LOGGER.debug("#submitToGateway: calling webservice to send request");
		DomibsConnectorAcknowledgementType ack = null;
		try {
			ack = submissionClient.submitMessage(request);
		} catch(Exception e) {
			state.setStatus(TransportState.FAILED);
            transportStatusService.updateTransportToGatewayStatus(transportId, state);
            
			throw new DomibusConnectorGatewaySubmissionException(e);
		}
		LOGGER.debug("#submitToGateway: received [{}] from gw", ack);

        if (ack != null && ack.isResult()) {
            String ebmsId = ack.getMessageId();
            LOGGER.info(LoggingMarker.BUSINESS_LOG,"GW accepted message and sent id [{}] back", ebmsId);
//            message.getMessageDetails().setEbmsMessageId(ebmsId);

            state.setRemoteMessageId(ebmsId);
            state.setStatus(TransportState.ACCEPTED);
            transportStatusService.updateTransportToGatewayStatus(transportId, state);
        } else if (ack != null){
            state.setStatus(TransportState.FAILED);
            transportStatusService.updateTransportToGatewayStatus(transportId, state);
            LOGGER.info(LoggingMarker.BUSINESS_LOG,"GW declined message and sent [{}] back", ack.getResultMessage());
            if (!StringUtils.isEmpty(ack.getResultMessage()))
                throw new DomibusConnectorGatewaySubmissionException(ack.getResultMessage());
            else
                throw new DomibusConnectorGatewaySubmissionException("Undefined submission error!");
        } else {
            throw new DomibusConnectorGatewaySubmissionException("Unknown result from gateway!");
        }

	}

}
