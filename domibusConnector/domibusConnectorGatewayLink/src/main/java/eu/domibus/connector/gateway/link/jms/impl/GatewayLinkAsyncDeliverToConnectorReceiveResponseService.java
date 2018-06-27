package eu.domibus.connector.gateway.link.jms.impl;

import eu.domibus.connector.domain.transition.DomibusConnectorMessageResponseType;
import eu.domibus.connector.gateway.link.jms.GatewaySubmissionTransportStatusService;
import eu.domibus.connector.jms.gateway.DomibusConnectorAsyncDeliverToConnectorReceiveResponseService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class GatewayLinkAsyncDeliverToConnectorReceiveResponseService implements DomibusConnectorAsyncDeliverToConnectorReceiveResponseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayLinkAsyncDeliverToConnectorReceiveResponseService.class);

    @Autowired
    GatewaySubmissionTransportStatusService transportStatusService;

    @Override
    public void deliverResponse(DomibusConnectorMessageResponseType response) {
        LOGGER.debug("Received Response....");
        //response.getMessageId()
        GatewaySubmissionTransportStatusService.DomibusConnectorTransportState transportState = new GatewaySubmissionTransportStatusService.DomibusConnectorTransportState();

        transportState.setForTransport(response.getResponseForMessageId());
        transportState.setRemoteTransportId(response.getAssignedMessageId());

        if (response.isResult()) {
            transportState.setStatus(GatewaySubmissionTransportStatusService.TransportState.ACCEPTED);
        } else {
            transportState.setStatus(GatewaySubmissionTransportStatusService.TransportState.FAILED);

            //DomibusConnectorDomainMessageTransformer.
            //TODO: map error list
            //transportState.setMessageErrorList();
        }


        transportStatusService.setTransportStatusForTransportToGateway(transportState);
    }

}
