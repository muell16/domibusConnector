package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 * Generic interface for submitting a message
 * to a link (eg. gateway-ws-link, gateway-jms-link, backend-ws-link, ...)
 */
public interface SubmitToLink {

    public void submitToLink(DomibusConnectorMessage message) throws DomibusConnectorSubmitToLinkException;

}
