package eu.domibus.connector.gateway.link.jms.impl;

import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformer;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageResponseType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.jms.gateway.DomibusConnectorAsyncDeliverToConnectorReceiveResponseService;
import eu.domibus.connector.jms.gateway.DomibusConnectorAsyncDeliverToConnectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("gwlink-jms")
public class HandleFromGwToConnectorDeliveredMessage implements DomibusConnectorAsyncDeliverToConnectorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandleFromGwToConnectorDeliveredMessage.class);

    @Autowired
    private DomibusConnectorGatewayDeliveryService controllerService;

    @Autowired
    @Lazy
    private DomibusConnectorAsyncDeliverToConnectorReceiveResponseService sendResponseService;

    public void setControllerService(DomibusConnectorGatewayDeliveryService controllerService) {
        this.controllerService = controllerService;
    }

    public void setSendResponseService(DomibusConnectorAsyncDeliverToConnectorReceiveResponseService sendResponseService) {
        this.sendResponseService = sendResponseService;
    }

    @Override
    public void deliverMessage(DomibusConnectorMessageType deliverMessageRequest) {
        LOGGER.debug("Deliver Message....");

        DomibusConnectorMessage message = DomibusConnectorDomainMessageTransformer.transformTransitionToDomain(deliverMessageRequest);

        DomibusConnectorMessageResponseType response = new DomibusConnectorMessageResponseType();
        response.setResponseForMessageId(deliverMessageRequest.getMessageDetails().getEbmsMessageId());
        try {
            controllerService.deliverMessageFromGatewayToController(message);
            response.setResult(true);
            //response.setAssignedMessageId(message.getConnectorMessageId());
        } catch (Exception e) {
            response.setResult(false);
            response.setResultMessage("Message could not be processed! " + e.getMessage());
        }

        sendResponseService.deliverResponse(response);
    }




}
