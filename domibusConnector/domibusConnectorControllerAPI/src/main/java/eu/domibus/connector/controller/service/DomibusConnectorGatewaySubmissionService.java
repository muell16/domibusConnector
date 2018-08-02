
package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;



/**
 * should be implemented by the Gateway Link modules to submit messages to the gateway
 * the service is called by the controller if it wants to submit a message to the gateway
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DomibusConnectorGatewaySubmissionService {

    /**
     *
     * @param message the message
     * @throws DomibusConnectorGatewaySubmissionException - in case of errors: eg: no connection to gw, gw not reachable,
     */
    public void submitToGateway(DomibusConnectorMessage message) throws DomibusConnectorGatewaySubmissionException;
    
}
