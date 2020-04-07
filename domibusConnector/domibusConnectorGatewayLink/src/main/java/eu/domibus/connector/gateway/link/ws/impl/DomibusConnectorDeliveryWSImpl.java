package eu.domibus.connector.gateway.link.ws.impl;

import eu.domibus.connector.tools.logging.SetMessageOnLoggingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWebService;


public class DomibusConnectorDeliveryWSImpl implements DomibusConnectorGatewayDeliveryWebService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorDeliveryWSImpl.class);

    public static final String BEAN_NAME = "domibusConnectorDeliveryServiceImpl";

    @Autowired
    private DomibusConnectorGatewayDeliveryService controllerService;

    @Autowired
    DomibusConnectorDomainMessageTransformerService transformerService;

    @Override
    public DomibsConnectorAcknowledgementType deliverMessage(DomibusConnectorMessageType deliverMessageRequest) {
        LOGGER.debug("#deliverMessage: deliverRequest [{}] from gw received", deliverMessageRequest);
        DomibusConnectorMessage domainMessage = transformerService.transformTransitionToDomain(deliverMessageRequest);
        SetMessageOnLoggingContext.putConnectorMessageIdOnMDC(domainMessage.getConnectorMessageId());
        DomibsConnectorAcknowledgementType ack = new DomibsConnectorAcknowledgementType();
        try {
            controllerService.deliverMessageFromGatewayToController(domainMessage);
        } catch (DomibusConnectorControllerException e) {
            LOGGER.warn("#deliverMessage: failed to process message in controller!", e);
            ack.setResult(false);
            ack.setResultMessage("Message could not be processed! " + e.getMessage());
            return ack;
        }
        ack.setResult(true);
        return ack;
    }

}
