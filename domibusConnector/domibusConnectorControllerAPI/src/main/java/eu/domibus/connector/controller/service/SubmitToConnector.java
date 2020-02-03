package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 * Generic interface to submit a message to the connector
 * from any link modules (gwjmsplugin, ...)
 */
public interface SubmitToConnector {

    public void submitToConnector(DomibusConnectorMessage message, DomibusConnectorLinkPartner linkPartner) throws DomibusConnectorSubmitToLinkException;

}
