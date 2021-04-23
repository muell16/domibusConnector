package eu.domibus.connector.link.service;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class SubmitToLinkServiceImpl implements SubmitToLinkService {

    private final DCActiveLinkManagerService dcActiveLinkManagerService;

    public SubmitToLinkServiceImpl(DCActiveLinkManagerService activeLinkManagerService) {
        this.dcActiveLinkManagerService = activeLinkManagerService;
    }

    @Override
    public void submitToLink(DomibusConnectorMessage message) throws DomibusConnectorSubmitToLinkException {
        DomibusConnectorMessageDirection direction = message.getMessageDetails().getDirection();
        DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName;
        if (direction.getTarget() == MessageTargetSource.BACKEND) {
            if (StringUtils.isEmpty(message.getMessageDetails().getConnectorBackendClientName())) {
                throw new DomibusConnectorSubmitToLinkException(message, "The backendClientName is empty!");
            }
            linkPartnerName = new DomibusConnectorLinkPartner.LinkPartnerName(message.getMessageDetails().getConnectorBackendClientName());
        } else if (direction.getTarget() == MessageTargetSource.GATEWAY) {
            if (StringUtils.isEmpty(message.getMessageDetails().getGatewayName())) {
                throw new DomibusConnectorSubmitToLinkException(message, "The gatewayName is empty!");
            }
            linkPartnerName = new DomibusConnectorLinkPartner.LinkPartnerName(message.getMessageDetails().getGatewayName());
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
}
