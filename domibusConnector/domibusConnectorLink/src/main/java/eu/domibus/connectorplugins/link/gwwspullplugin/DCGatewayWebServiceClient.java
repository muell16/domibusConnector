package eu.domibus.connectorplugins.link.gwwspullplugin;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.*;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageId;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.link.service.PullFromLinkPartner;
import eu.domibus.connector.link.service.SubmitToLinkPartner;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.ws.gateway.webservice.DomibusConnectorGatewayWebService;
import eu.domibus.connector.ws.gateway.webservice.GetMessageByIdRequest;
import eu.domibus.connector.ws.gateway.webservice.ListPendingMessageIdsRequest;
import eu.domibus.connector.ws.gateway.webservice.ListPendingMessageIdsResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


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

    @Autowired
    DCGatewayPullPluginConfigurationProperties configurationProperties;


    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void submitToLink(DomibusConnectorMessage message, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) throws DomibusConnectorSubmitToLinkException {
        TransportStateService.DomibusConnectorTransportState transportState = new TransportStateService.DomibusConnectorTransportState();
        transportState.setStatus(TransportState.PENDING);
        TransportStateService.TransportId transportId = transportStateService.createTransportFor(message, linkPartnerName);
        transportStateService.updateTransportToGatewayStatus(transportId, transportState);

        DomibusConnectorMessageType domibusConnectorMessageType = transformerService.transformDomainToTransition(message);
        if(configurationProperties.isMultiDomainEnabled()) {
            String[] partyIdSplit = message.getMessageDetails().getFromParty().getPartyIdType().split(":");
            String domain = configurationProperties.getDomainAssignment().get(partyIdSplit[partyIdSplit.length-1]);
            domibusConnectorMessageType.setDomain(domain);
        }
        DomibsConnectorAcknowledgementType domibsConnectorAcknowledgementType = gatewayWebService.submitMessage(domibusConnectorMessageType);

        transportState = new TransportStateService.DomibusConnectorTransportState();
        transportState.setRemoteMessageId(domibsConnectorAcknowledgementType.getMessageId());
        transportState.setText(domibsConnectorAcknowledgementType.getResultMessage());
        if (domibsConnectorAcknowledgementType.isResult()) {
            transportState.setStatus(TransportState.ACCEPTED);
        } else {
            transportState.setStatus(TransportState.FAILED);
        }
        transportStateService.updateTransportToGatewayStatus(transportId, transportState);

    }


    public void pullMessagesFrom(DomibusConnectorLinkPartner.LinkPartnerName linkPartner) {
        try (MDC.MDCCloseable mdcCloseable = MDC.putCloseable(LoggingMDCPropertyNames.MDC_LINK_PARTNER_NAME, linkPartner.getLinkName())) {
            List<java.lang.String> messageIds;

            if(configurationProperties.isMultiDomainEnabled()) {
                String domainNames[] = configurationProperties.getDomainNames().split(";");
                for (String domainName : domainNames) {
                    ListPendingMessageIdsRequest req = new ListPendingMessageIdsRequest();
                    req.setDomain(domainName);
                    messageIds = listPendingMessages(req);
                    messageIds.stream().forEach(id -> this.pullMessage(linkPartner,Optional.of(domainName), id));
                }
            } else {
                messageIds = listPendingMessages(new ListPendingMessageIdsRequest());
                messageIds.stream().forEach(id -> this.pullMessage(linkPartner,Optional.empty(), id));
            }
        }
    }

    protected List<java.lang.String> listPendingMessages(ListPendingMessageIdsRequest request) {
        ListPendingMessageIdsResponse listPendingMessageIdsResponse = gatewayWebService.listPendingMessageIds(request);

        return listPendingMessageIdsResponse.getMessageIds();
    }

    private void pullMessage(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName, Optional<String> domain, java.lang.String remoteMessageId) {

        DomibusConnectorMessageId connectorMessageId = messageIdGenerator.generateDomibusConnectorMessageId();
        try (MDC.MDCCloseable mdcCloseable = MDC.putCloseable(LoggingMDCPropertyNames.MDC_REMOTE_MSG_ID, remoteMessageId);
            MDC.MDCCloseable conMsgId = MDC.putCloseable(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, connectorMessageId.getConnectorMessageId());
        ) {
            LOGGER.trace("Pulling message with id [{}] from [{}]", remoteMessageId, linkPartnerName);
            GetMessageByIdRequest getMessageByIdRequest = new GetMessageByIdRequest();
            getMessageByIdRequest.setMessageId(remoteMessageId);
            if(domain.isPresent()) {
                getMessageByIdRequest.setDomain(domain.get());
            }
            DomibusConnectorMessageType messageById = gatewayWebService.getMessageById(getMessageByIdRequest);

            DomibusConnectorMessage message = transformerService.transformTransitionToDomain(messageById, connectorMessageId);


            Optional<ActiveLinkPartner> activeLinkPartnerByName = dcActiveLinkManagerService.getActiveLinkPartnerByName(linkPartnerName);
            submitToConnector.submitToConnector(message, activeLinkPartnerByName.get().getLinkPartner());

        }
    }


}
