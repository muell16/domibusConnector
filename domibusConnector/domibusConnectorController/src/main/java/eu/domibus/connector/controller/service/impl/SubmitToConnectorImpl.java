package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendSubmissionService;
import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmitToConnectorImpl implements SubmitToConnector {

    @Autowired
    DomibusConnectorGatewayDeliveryService fromGatewayToConnector;

    @Autowired
    DomibusConnectorBackendSubmissionService fromBackendToConnector;

    @Override
    public void submitToConnector(DomibusConnectorMessage message, DomibusConnectorLinkPartner linkPartner) throws DomibusConnectorSubmitToLinkException {
        if (linkPartner.getLinkType() == LinkType.GATEWAY) {
            message.getMessageDetails().setDirection(DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);
            fromGatewayToConnector.deliverMessageFromGatewayToController(message);
        }
        if (linkPartner.getLinkType() == LinkType.BACKEND) {
            message.getMessageDetails().setDirection(DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
            fromBackendToConnector.submitToController(message);
        }
    }

}
