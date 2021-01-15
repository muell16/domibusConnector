
package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.DomibusConnectorBackendException;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 *
 * Should be implemented by the backend link module
 * Is called by the controller if it wants to submit a message to a backend
 *  the specific backend is determined by the backend module
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DomibusConnectorBackendDeliveryService {

    /**
     * hands over a message to the backend link
     *  the backend link has to determine which backend client should receive the message
     *  and has to transport the message to this backend client
     *
     * @param message the message
     * @throws DomibusConnectorControllerException in case of exceptions
     */
    public void deliverMessageToBackend(DomibusConnectorMessage message) throws DomibusConnectorBackendException;

}
