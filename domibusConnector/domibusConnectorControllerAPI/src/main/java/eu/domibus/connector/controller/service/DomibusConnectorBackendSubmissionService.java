
package eu.domibus.connector.controller.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 * should be implemented by the Service which is delivering the messages
 * to the backend
 *
 */
public interface DomibusConnectorBackendSubmissionService {

    /**
     * submit a persisted message for further processing to controller
     * @param message - the message
     */
    public void submitToController(DomibusConnectorMessage message);

}
