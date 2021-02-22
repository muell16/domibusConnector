package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.service.DomibusConnectorBackendSubmissionService;
import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmitToConnectorImpl implements SubmitToConnector {



    private final DomibusConnectorGatewayDeliveryService fromGatewayToConnector;
    private final DomibusConnectorBackendSubmissionService fromBackendToConnector;
    private final DCMessagePersistenceService messagePersistenceService;

    @Override
    public void submitToConnector(DomibusConnectorMessage message, DomibusConnectorLinkPartner linkPartner) {
        if (linkPartner.getLinkType() == LinkType.GATEWAY) {
            message.getMessageDetails().setDirection(DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);
            messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);
            fromGatewayToConnector.deliverMessageFromGatewayToController(message);
        } else if (linkPartner.getLinkType() == LinkType.BACKEND) {
            message.getMessageDetails().setDirection(DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
            message.getMessageDetails().setConnectorBackendClientName(linkPartner.getLinkPartnerName().getLinkName());
            messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
            fromBackendToConnector.submitToController(message);
        } else {
            throw new RuntimeException("Backend/gateway type not known!");
        }
    }

}
