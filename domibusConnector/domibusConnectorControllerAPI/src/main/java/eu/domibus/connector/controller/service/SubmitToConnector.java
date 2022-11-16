package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.message.model.DC5Message;

/**
 * Generic interface to submit a message to the connector
 * from any link modules (gwjmsplugin, ...)
 */
public interface SubmitToConnector {

    default public void submitToConnector(DC5Message message, DomibusConnectorLinkPartner linkPartner) throws DomibusConnectorSubmitToLinkException {
        submitToConnector(message, linkPartner.getLinkPartnerName(), linkPartner.getLinkType());
    }

    public void submitToConnector(DC5Message message, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName
            , LinkType linkType) throws DomibusConnectorSubmitToLinkException;

}
