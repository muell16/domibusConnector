package eu.domibus.connector.link.service;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.message.model.DC5Message;

/**
 * Generic interface for submitting a message
 * to a link (eg. gateway-ws-link, gateway-jms-link, backend-ws-link, ...)
 */
public interface SubmitToLinkPartner {

    public void submitToLink(DC5Message message, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) throws DomibusConnectorSubmitToLinkException;

}
