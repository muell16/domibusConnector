package eu.domibus.connector.gateway.link.jms.impl;

import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformer;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageResponseType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.gateway.link.jms.GatewaySubmissionTransportStatusService;
import eu.domibus.connector.jms.gateway.DomibusConnectorAsyncDeliverToConnectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GatewayLinkAsyncDeliveryService implements DomibusConnectorAsyncDeliverToConnectorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayLinkAsyncDeliveryService.class);

    @Autowired
    private DomibusConnectorGatewayDeliveryService controllerService;

    @Autowired
    private GatewaySubmissionTransportStatusService transportStatusService;

    @Override
    public void deliverMessage(DomibusConnectorMessageType deliverMessageRequest) {
        LOGGER.debug("Deliver Message....");

        DomibusConnectorMessage message = DomibusConnectorDomainMessageTransformer.transformTransitionToDomain(deliverMessageRequest);

        controllerService.deliverMessageFromGatewayToController(message);
    }

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
