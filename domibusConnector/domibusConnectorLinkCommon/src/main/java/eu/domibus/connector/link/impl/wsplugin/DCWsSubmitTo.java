package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.LinkMode;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWebService;
import eu.domibus.connector.ws.gateway.submission.webservice.DomibusConnectorGatewaySubmissionWebService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Optional;


@Component
@Profile(DCWsPluginConfiguration.DC_WS_PLUGIN_PROFILE_NAME)
public class DCWsSubmitTo implements SubmitToLink {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    DCActiveLinkManagerService activeLinkManagerService;

    @Autowired
    DCWsClientWebServiceClientFactory webServiceClientFactory;

    @Autowired
    TransportStatusService transportStatusService;

    @Autowired
    DomibusConnectorDomainMessageTransformerService transformerService;


    @Override
    public void submitToLink(DomibusConnectorMessage message, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) throws DomibusConnectorSubmitToLinkException {
        TransportStatusService.TransportId transportId = transportStatusService.createOrGetTransportFor(message, linkPartnerName);
        Optional<ActiveLinkPartner> optionalActiveLinkPartner = activeLinkManagerService.getActiveLinkPartner(linkPartnerName);
        if (!optionalActiveLinkPartner.isPresent()) {
            throw new RuntimeException("No Link Partner found!");
        }

        DCWsActiveLinkPartner activeLinkPartner = (DCWsActiveLinkPartner) optionalActiveLinkPartner.get();
        DomibusConnectorLinkPartner linkPartner = activeLinkPartner.getLinkPartner();


        if (linkPartner.getLinkMode() == LinkMode.PUSH && LinkType.BACKEND == linkPartner.getLinkType()) {
            handlePushBackendLink(transportId, message, activeLinkPartner);
        } else if (linkPartner.getLinkMode() == LinkMode.PUSH && LinkType.GATEWAY == linkPartner.getLinkType()) {
            handlePushGatewayLink(transportId, message, activeLinkPartner);
        } else if (linkPartner.getLinkMode() == LinkMode.PULL) {
            handlePullLink(transportId, message, activeLinkPartner);
        }
    }

    private void handlePullLink(TransportStatusService.TransportId transportId, DomibusConnectorMessage message, DCWsActiveLinkPartner activeLinkPartner) {
        LOGGER.trace("handlePullLink");
        TransportStatusService.DomibusConnectorTransportState state = new TransportStatusService.DomibusConnectorTransportState();
        state.setConnectorTransportId(transportId);
        state.setStatus(TransportState.PENDING);
        state.setText("Waiting for message getting pulled");
        transportStatusService.updateTransportStatus(state);
    }

    private void handlePushGatewayLink(TransportStatusService.TransportId transportId, DomibusConnectorMessage message, DCWsActiveLinkPartner linkPartner) {
        LOGGER.trace("#handlePushGatewayLink");
        DomibusConnectorGatewaySubmissionWebService gateway = webServiceClientFactory.createGateway(linkPartner);
        DomibusConnectorMessageType domibusConnectorMessageType = transformerService.transformDomainToTransition(message);
        DomibsConnectorAcknowledgementType ack = gateway.submitMessage(domibusConnectorMessageType);

        setTransportStateByAck(transportId, ack);

    }

    private void handlePushBackendLink(TransportStatusService.TransportId transportId, DomibusConnectorMessage message, DCWsActiveLinkPartner linkPartner) {
        LOGGER.trace("#handlePushBackendLink");
        DomibusConnectorBackendDeliveryWebService backendWsClient = webServiceClientFactory.createBackendWsClient(linkPartner);
        @NotNull DomibusConnectorMessageType domibusConnectorMessageType = transformerService.transformDomainToTransition(message);
        DomibsConnectorAcknowledgementType ack = backendWsClient.deliverMessage(domibusConnectorMessageType);

        setTransportStateByAck(transportId, ack);
    }

    private void setTransportStateByAck(TransportStatusService.TransportId transportId, DomibsConnectorAcknowledgementType ack) {
        TransportStatusService.DomibusConnectorTransportState state = new TransportStatusService.DomibusConnectorTransportState();
        state.setConnectorTransportId(transportId);
        state.setRemoteMessageId(ack.getMessageId());
        state.setText(ack.getResultMessage());
        if (ack.isResult()) {
            state.setStatus(TransportState.ACCEPTED);
        } else {
            state.setStatus(TransportState.FAILED);
        }
        transportStatusService.updateTransportStatus(state);
    }


}
