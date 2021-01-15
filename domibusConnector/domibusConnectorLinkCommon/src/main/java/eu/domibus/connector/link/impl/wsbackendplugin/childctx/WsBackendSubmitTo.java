package eu.domibus.connector.link.impl.wsbackendplugin.childctx;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.enums.LinkMode;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.impl.wsbackendplugin.WsBackendPluginActiveLinkPartner;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWebService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Optional;

public class WsBackendSubmitTo implements SubmitToLink {

    @Autowired
    DCActiveLinkManagerService linkManagerService;

    @Autowired
    WsBackendPluginWebServiceClientFactory webServiceClientFactory;

    @Autowired
    DomibusConnectorDomainMessageTransformerService transformerService;

    @Autowired
    TransportStateService transportStateService;


    @Override
    public void submitToLink(DomibusConnectorMessage message, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) throws DomibusConnectorSubmitToLinkException {
        Optional<ActiveLinkPartner> activeLinkPartnerByName = linkManagerService.getActiveLinkPartnerByName(linkPartnerName);
        if (activeLinkPartnerByName.isPresent()) {
            WsBackendPluginActiveLinkPartner activeLinkPartner = (WsBackendPluginActiveLinkPartner) activeLinkPartnerByName.get();
            if (activeLinkPartner.getLinkPartner().getLinkMode() == LinkMode.PUSH) {
                pushMessage(message, activeLinkPartner);
            } else {
                makeMessageReadyForPull(message, activeLinkPartner);
            }

        } else {
            throw new RuntimeException(String.format("No LinkPartner found with name [%s]", linkPartnerName));
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void makeMessageReadyForPull(DomibusConnectorMessage message, WsBackendPluginActiveLinkPartner activeLinkPartner) {
        DomibusConnectorBackendDeliveryWebService backendWsClient = webServiceClientFactory.createBackendWsClient(activeLinkPartner);

        TransportStateService.TransportId transportId = transportStateService.createOrGetTransportFor(message, activeLinkPartner.getLinkPartner().getLinkPartnerName());

        TransportStateService.DomibusConnectorTransportState state = new TransportStateService.DomibusConnectorTransportState();
        state.setStatus(TransportState.PENDING);
        state.setText("Message ready for pull by client");
        transportStateService.updateTransportToBackendClientStatus(transportId, state);

    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void pushMessage(DomibusConnectorMessage message, WsBackendPluginActiveLinkPartner activeLinkPartner) {
        DomibusConnectorBackendDeliveryWebService backendWsClient = webServiceClientFactory.createBackendWsClient(activeLinkPartner);

        TransportStateService.TransportId transportId = transportStateService.createOrGetTransportFor(message, activeLinkPartner.getLinkPartner().getLinkPartnerName());

        TransportStateService.DomibusConnectorTransportState state = new TransportStateService.DomibusConnectorTransportState();
        DomibusConnectorMessageType domibusConnectorMessageType = transformerService.transformDomainToTransition(message);
        try {
            DomibsConnectorAcknowledgementType domibsConnectorAcknowledgementType = backendWsClient.deliverMessage(domibusConnectorMessageType);

            state.setStatus(domibsConnectorAcknowledgementType.isResult() ? TransportState.ACCEPTED : TransportState.FAILED);
            state.setRemoteMessageId(domibsConnectorAcknowledgementType.getMessageId());
            state.setText(domibsConnectorAcknowledgementType.getResultMessage());

        } catch (Exception e) {
            state.setStatus(TransportState.FAILED);
            state.setText(e.getMessage());
        }
        transportStateService.updateTransportToBackendClientStatus(transportId, state);

    }

}
