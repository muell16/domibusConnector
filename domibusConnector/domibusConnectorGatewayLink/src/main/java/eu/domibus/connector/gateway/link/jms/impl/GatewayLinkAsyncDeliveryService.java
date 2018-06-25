package eu.domibus.connector.gateway.link.jms.impl;

import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformer;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryAsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GatewayLinkAsyncDeliveryService implements DomibusConnectorGatewayDeliveryAsyncService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorGatewayDeliveryAsyncService.class);

    @Autowired
    private DomibusConnectorGatewayDeliveryService controllerService;

    @Override
    public void deliverMessage(DomibusConnectorMessageType deliverMessageRequest) {
        LOGGER.debug("Deliver Message....");

        DomibusConnectorMessage message = DomibusConnectorDomainMessageTransformer.transformTransitionToDomain(deliverMessageRequest);

        controllerService.deliverMessageFromGateway(message);
    }


}
