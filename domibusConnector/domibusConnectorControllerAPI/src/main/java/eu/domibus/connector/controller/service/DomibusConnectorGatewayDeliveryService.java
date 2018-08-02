
package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 * is implemented by the connector controller
 * to deliver the by the gw link module received messages to controller module
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DomibusConnectorGatewayDeliveryService {

    /**
     *
     * @param message the message
     * @throws DomibusConnectorControllerException in case of errors: storing message to database, invalid message
     */
    public void deliverMessageFromGatewayToController(DomibusConnectorMessage message) throws DomibusConnectorControllerException;
    
}
