package eu.domibus.connector.link.service;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import org.springframework.stereotype.Service;

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
        String linkPartnerName = "";
        if (direction.getTarget() == MessageTargetSource.BACKEND) {
            linkPartnerName = message.getMessageDetails().getConnectorBackendClientName();
        } else if (direction.getTarget() == MessageTargetSource.GATEWAY) {
            linkPartnerName = message.getMessageDetails().getGatewayName();
        }
        Optional<ActiveLinkPartner> activeLinkPartnerByName = dcActiveLinkManagerService.getActiveLinkPartnerByName(linkPartnerName);
        if (activeLinkPartnerByName.isPresent()) {
            activeLinkPartnerByName.get().getSubmitToLink().submitToLink(message, activeLinkPartnerByName.get().getLinkPartner().getLinkPartnerName());
        } else {
            String errorMessage = String.format("The LinkPartner with name [%s] could not found/is not active!", linkPartnerName);
            throw new DomibusConnectorSubmitToLinkException(message, errorMessage);
        }
    }
}
