package eu.ecodex.connector.controller;

import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;

/**
 * Controller interface which handles the whole lifecycle of the connector.
 * 
 * @author riederb
 * 
 */
public interface ECodexConnectorController {

    /**
     * Method which handles all the way from national backend system to gateway.
     * Triggered by a timer job it executes every period (configurable) to check
     * pending messages on national side and handles all messages way through to
     * the gateway.
     * 
     * @throws ECodexConnectorControllerException
     */
    public void handleNationalMessages() throws ECodexConnectorControllerException;

    /**
     * Method which handles all the way from gateway to the national backend
     * system. Triggered by a timer job it executes every period (configurable)
     * to check pending messages on the gateway and handles all messages way
     * through to the national backend side.
     * 
     * @throws ECodexConnectorControllerException
     */
    public void handleGatewayMessages() throws ECodexConnectorControllerException;

}
