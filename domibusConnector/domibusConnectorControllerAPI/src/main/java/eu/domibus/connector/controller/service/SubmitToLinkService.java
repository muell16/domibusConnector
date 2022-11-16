package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.ecodex.dc5.message.model.DC5Message;

/**
 * Will be called by the connector
 * controller to submit a message to a link
 *  the specific implementation here has to lookup the
 *  correct link partner
 */
public interface SubmitToLinkService {

    public void submitToLink(DC5Message message) throws DomibusConnectorSubmitToLinkException;

}
