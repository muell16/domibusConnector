package eu.domibus.connector.backend.service;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

public interface DomibusConnectorBackendInternalDeliverToController {

    void submitToController(DomibusConnectorBackendMessage message);

    /**
     * prepares message for delivery to national system
     *
     *  *) create delivery evidence
     *
     * @param message - the corresponding message
     *
     */
    DomibusConnectorMessage processMessageBeforeDeliverToBackend(DomibusConnectorMessage message);
}
