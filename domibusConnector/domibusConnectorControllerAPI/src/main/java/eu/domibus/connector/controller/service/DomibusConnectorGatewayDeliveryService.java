
package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DomibusConnectorGatewayDeliveryService {

    public void deliverMessageFromGateway(DomibusConnectorMessage message) throws DomibusConnectorControllerException;
    
}
