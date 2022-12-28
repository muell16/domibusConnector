package eu.domibus.connectorplugins.link.gwwspullplugin;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.*;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5MessageId;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.link.service.PullFromLinkPartner;
import eu.domibus.connector.link.service.SubmitToLinkPartner;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.ws.gateway.webservice.DomibusConnectorGatewayWebService;
import eu.domibus.connector.ws.gateway.webservice.GetMessageByIdRequest;
import eu.domibus.connector.ws.gateway.webservice.ListPendingMessageIdsRequest;
import eu.domibus.connector.ws.gateway.webservice.ListPendingMessageIdsResponse;
import eu.ecodex.dc5.transport.model.DC5TransportRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;


public class DCGatewayWebServiceClient implements SubmitToLinkPartner, PullFromLinkPartner {

    private static final Logger LOGGER = LogManager.getLogger(DCGatewayWebServiceClient.class);

    @Autowired
    DomibusConnectorGatewayWebService gatewayWebService;

    @Autowired
    DomibusConnectorDomainMessageTransformerService transformerService;

    @Autowired
    TransportStateService transportStateService;

    @Autowired
    SubmitToConnector submitToConnector;

    @Autowired
    DCActiveLinkManagerService dcActiveLinkManagerService;

    @Autowired
    DomibusConnectorMessageIdGenerator messageIdGenerator;


    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void submitToLink(DC5TransportRequest.TransportRequestId transportRequestId, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) throws DomibusConnectorSubmitToLinkException {
        //TODO
        TransportStateService.DomibusConnectorTransportState transportState = new TransportStateService.DomibusConnectorTransportState();
        transportState.setStatus(TransportState.PENDING);

//        TransportStateService.TransportId transportId = transportStateService.getTransportStepById(message.getTransportId());
        transportStateService.updateTransportToGatewayStatus(new TransportStateService.TransportId(transportRequestId.getTransportId()), transportState);
        DC5TransportRequest transportRequest = transportStateService.getTransportRequest(transportRequestId);

        DomibusConnectorMessageType domibusConnectorMessageType = transformerService.transformDomainToTransition(transportRequest.getMessage());
        DomibsConnectorAcknowledgementType domibsConnectorAcknowledgementType = gatewayWebService.submitMessage(domibusConnectorMessageType);

        transportState = new TransportStateService.DomibusConnectorTransportState();
        transportState.setRemoteMessageId(domibsConnectorAcknowledgementType.getMessageId());
        transportState.setText(domibsConnectorAcknowledgementType.getResultMessage());
        if (domibsConnectorAcknowledgementType.isResult()) {
            transportState.setStatus(TransportState.ACCEPTED);
        } else {
            transportState.setStatus(TransportState.FAILED);
        }
        transportStateService.updateTransportToGatewayStatus(transportRequest.getTransportRequestId(), transportState);

    }


    public void pullMessagesFrom(DomibusConnectorLinkPartner.LinkPartnerName linkPartner) {
        try (MDC.MDCCloseable mdcCloseable = MDC.putCloseable(LoggingMDCPropertyNames.MDC_LINK_PARTNER_NAME, linkPartner.getLinkName())) {
            ListPendingMessageIdsRequest req = new ListPendingMessageIdsRequest();
            ListPendingMessageIdsResponse listPendingMessageIdsResponse = gatewayWebService.listPendingMessageIds(req);

            List<java.lang.String> messageIds = listPendingMessageIdsResponse.getMessageIds();
            messageIds.stream().forEach(id -> this.pullMessage(linkPartner, id));
        }
    }

    private void pullMessage(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName, java.lang.String remoteMessageId) {

        DC5MessageId connectorMessageId = messageIdGenerator.generateDomibusConnectorMessageId();
        try (MDC.MDCCloseable mdcCloseable = MDC.putCloseable(LoggingMDCPropertyNames.MDC_REMOTE_MSG_ID, remoteMessageId);
            MDC.MDCCloseable conMsgId = MDC.putCloseable(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, connectorMessageId.getConnectorMessageId());
        ) {
            LOGGER.trace("Pulling message with id [{}] from [{}]", remoteMessageId, linkPartnerName);
            GetMessageByIdRequest getMessageByIdRequest = new GetMessageByIdRequest();
            getMessageByIdRequest.setMessageId(remoteMessageId);
            DomibusConnectorMessageType messageById = gatewayWebService.getMessageById(getMessageByIdRequest);

//            DC5Message message = transformerService.transformTransitionToDomain(MessageTargetSource.BACKEND, linkPartnerName, messageById, connectorMessageId);
//            Optional<ActiveLinkPartner> activeLinkPartnerByName = dcActiveLinkManagerService.getActiveLinkPartnerByName(linkPartnerName);

            SubmitToConnector.ReceiveMessageFlowResult receiveMessageFlowResult = submitToConnector
                    .receiveMessage(messageById, (toMessage, p) -> transformerService.transformTransitionToDomain(MessageTargetSource.BACKEND,
                    linkPartnerName,
                    toMessage, connectorMessageId));
            if (receiveMessageFlowResult.isSuccess()) {
                LOGGER.debug("Pulling message with id [{}] from [{}] is [{}]", remoteMessageId, linkPartnerName, receiveMessageFlowResult.isSuccess());
            } else {
                LOGGER.warn("Pulling message with id [{}] from [{}] failed with [{}] due [{}]",
                        remoteMessageId,
                        linkPartnerName, receiveMessageFlowResult.getErrorCode().orElse(null),
                        receiveMessageFlowResult.getError().map(Throwable::getMessage).orElse(null)
                        );
            }
        }
    }


}
