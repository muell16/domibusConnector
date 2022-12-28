package eu.domibus.connectorplugins.link.gwwspushplugin;

import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DomibusConnectorMessageId;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWebService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

public class WsGatewayPluginDeliveryServiceEndpointImpl implements DomibusConnectorGatewayDeliveryWebService {

    private static final Logger LOGGER = LogManager.getLogger(WsGatewayPluginDeliveryServiceEndpointImpl.class);

    @Autowired
    SubmitToConnector submitToConnector;

    @Autowired
    DomibusConnectorMessageIdGenerator messageIdGenerator;

    @Autowired
    DomibusConnectorDomainMessageTransformerService transformerService;

    @Autowired
    DomibusConnectorLinkPartner linkPartner;

    @Override
    public DomibsConnectorAcknowledgementType deliverMessage(DomibusConnectorMessageType deliverMessageRequest) {
        String linkName = linkPartner.getLinkPartnerName().getLinkName();
        DomibsConnectorAcknowledgementType ret = new DomibsConnectorAcknowledgementType();
        DomibusConnectorMessageId connectorMessageId = messageIdGenerator.generateDomibusConnectorMessageId();
        try (MDC.MDCCloseable mdc = MDC.putCloseable(LoggingMDCPropertyNames.MDC_LINK_PARTNER_NAME, linkName);
                MDC.MDCCloseable conId = MDC.putCloseable(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, connectorMessageId.getConnectorMessageId())
        ) {
            LOGGER.debug("Message delivered from gateway [{}] assigning connectorMessageId [{}]", linkName, connectorMessageId);

            SubmitToConnector.ReceiveMessageFlowResult receiveMessageFlowResult = submitToConnector.receiveMessage(deliverMessageRequest, (m, p) -> {
                return transformerService.transformTransitionToDomain(MessageTargetSource.GATEWAY, linkPartner.getLinkPartnerName(), m, connectorMessageId);
            });


            if (receiveMessageFlowResult.isSuccess()) {
                ret.setMessageId(connectorMessageId.getConnectorMessageId());
                ret.setResult(receiveMessageFlowResult.isSuccess());
                return ret;
            } else {
                ret.setResultMessage(receiveMessageFlowResult.getError().map(Throwable::getMessage).orElse("Any error"));
                ret.setResult(false);
                return ret;
            }

        } catch (Exception e) {
            LOGGER.error("Exception occured while receiving message from gateway", e);
            ret.setResult(false);
            ret.setResultMessage(e.getMessage());

        }
        return ret;
    }


}
