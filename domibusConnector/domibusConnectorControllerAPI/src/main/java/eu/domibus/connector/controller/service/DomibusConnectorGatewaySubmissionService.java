
package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DomibusConnectorGatewaySubmissionService {

    public void submitToGateway(DomibusConnectorMessage message) throws DomibusConnectorGatewaySubmissionException;
    
}
