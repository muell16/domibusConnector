package eu.domibus.connector.gateway.link.jms.impl;

import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageResponseType;
//import eu.domibus.connector.jms.gateway.DomibusConnectorAsyncSubmitToGatewayReceiveResponseService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("gwlink-jms")
public class HandleFromGwToConnectorSentResponse {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandleFromGwToConnectorSentResponse.class);

    @Autowired
    public TransportStatusService transportStatusService;

//    @Override
    public void submitResponse(DomibusConnectorMessageResponseType response) {
        LOGGER.debug("Received SubmitResponse");

        TransportStatusService.DomibusConnectorTransportState transportState = new TransportStatusService.DomibusConnectorTransportState();

        transportState.setConnectorTransportId(new TransportStatusService.TransportId(response.getResponseForMessageId()));
        transportState.setRemoteMessageId(response.getAssignedMessageId());

        if (response.isResult()) {
            transportState.setStatus(TransportState.ACCEPTED);
        } else {
            transportState.setStatus(TransportState.FAILED);

            //DomibusConnectorDomainMessageTransformer.
            //TODO: map error list
            //transportState.setMessageErrorList();
        }
        transportStatusService.updateTransportToGatewayStatus(new TransportStatusService.TransportId(response.getResponseForMessageId()), transportState);
    }

}