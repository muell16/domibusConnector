package eu.domibus.connector.link.service;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageTransportException;
import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.ecodex.dc5.transport.model.DC5TransportRequest;
import eu.ecodex.dc5.transport.repo.DC5TransportRequestRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubmitToLinkServiceImpl implements SubmitToLinkService {

    public static final String SUBMIT_TO_LINK_SERVICE = "SubmitToLinkPartnerService";

    private final DCActiveLinkManagerService dcActiveLinkManagerService;

    private final DC5TransportRequestRepo transportRequestRepo;

    @MDC(name = LoggingMDCPropertyNames.MDC_DC_MESSAGE_PROCESSOR_PROPERTY_NAME, value = SUBMIT_TO_LINK_SERVICE)
    public void submitToLink(DC5Message message) throws DomibusConnectorSubmitToLinkException {
        DomibusConnectorMessageDirection direction = message.getDirection();
        DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName;
        if (direction.getTarget() == MessageTargetSource.BACKEND) {
            if (StringUtils.isEmpty(message.getBackendLinkName())) {
                throw new DomibusConnectorSubmitToLinkException(message, "The backendClientName is empty!");
            }
            linkPartnerName = new DomibusConnectorLinkPartner.LinkPartnerName(message.getBackendLinkName());
        } else if (direction.getTarget() == MessageTargetSource.GATEWAY) {
            if (StringUtils.isEmpty(message.getGatewayLinkName())) {
                throw new DomibusConnectorSubmitToLinkException(message, "The gatewayName is empty!");
            }
            linkPartnerName = new DomibusConnectorLinkPartner.LinkPartnerName(message.getGatewayLinkName());
        } else {
            throw new IllegalArgumentException("MessageTarget not valid!");
        }

        Optional<SubmitToLinkPartner> submitToLinkPartner = dcActiveLinkManagerService.getSubmitToLinkPartner(linkPartnerName);
        if (submitToLinkPartner.isPresent()) {
            submitToLinkPartner.ifPresent(s -> s.submitToLink(message, linkPartnerName));
        } else {
            String errorMessage = String.format("The LinkPartner with name [%s] could not be found/is not active!", linkPartnerName);
            throw new DomibusConnectorSubmitToLinkException(message, errorMessage);
        }
    }

    @Override
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_MESSAGE_PROCESSOR_PROPERTY_NAME, value = SUBMIT_TO_LINK_SERVICE)
    public void submitToLink(SubmitToLinkEvent event) throws DomibusConnectorSubmitToLinkException {
        DC5TransportRequest.TransportRequestId transportRequestId = event.getTransportRequestId();
        Optional<DC5TransportRequest> byTransportRequestId = transportRequestRepo.findByTransportRequestId(transportRequestId);
        if (!byTransportRequestId.isPresent()) {
            throw new DomibusConnectorMessageTransportException(transportRequestId, String.format("Cannot find transport request with id [%s] in DB", transportRequestId));
        }
        DC5TransportRequest transportRequest = byTransportRequestId.get();

        DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName = DomibusConnectorLinkPartner.LinkPartnerName.of(transportRequest.getLinkName());
        DC5Message message = transportRequest.getMessage();

        Optional<SubmitToLinkPartner> submitToLinkPartner = dcActiveLinkManagerService.getSubmitToLinkPartner(linkPartnerName);
        if (submitToLinkPartner.isPresent()) {
            submitToLinkPartner.ifPresent(s -> s.submitToLink(message, linkPartnerName));
        } else {
            String errorMessage = String.format("The LinkPartner with name [%s] could not be found/is not active!", linkPartnerName);
            throw new DomibusConnectorSubmitToLinkException(message, errorMessage);
        }

    }
}
