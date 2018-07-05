
package eu.domibus.connector.controller.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 *  is implemented by the controller to submit messages to the controller
 *  which have been received by the backend link module
 *
 */
public interface DomibusConnectorBackendSubmissionService {

    /**
     * submit a persisted message for further processing to controller
     * @param message - the message
     */
    public void submitToController(DomibusConnectorMessage message);

}
