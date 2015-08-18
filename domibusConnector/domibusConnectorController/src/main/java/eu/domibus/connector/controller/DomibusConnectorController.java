package eu.domibus.connector.controller;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;

/**
 * Controller interface which handles the whole lifecycle of the connector.
 * 
 * @author riederb
 * 
 */
public interface DomibusConnectorController {

    /**
     * Method which handles evidences all the way through the connector.
     * Triggered by a timer job executed every configured time-period it checks
     * (depending on the implementation) pending messages/confirmations on one
     * side and routes them to the other side.
     * 
     * @throws DomibusConnectorControllerException
     */
    public void execute() throws DomibusConnectorControllerException;

}
