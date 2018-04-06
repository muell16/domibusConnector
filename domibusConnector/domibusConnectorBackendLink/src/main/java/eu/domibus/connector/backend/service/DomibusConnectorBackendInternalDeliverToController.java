package eu.domibus.connector.backend.service;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

public interface DomibusConnectorBackendInternalDeliverToController {

    void submitToController(DomibusConnectorBackendMessage message);

    /**
     * prepares message for delivery to national system
     *  should be called by the submitting implementation BEFORE the message is submitted
     *
     * @param message - the message
     * @return the (changed) message
     *
     */
    DomibusConnectorMessage processMessageBeforeDeliverToBackend(DomibusConnectorMessage message);

    /**
     * does the work to mark the message as delivered to the national system
     *  should be called by the submitting implementation AFTER the message has been submitted without an error
     *
     * @param message - the message
     * @return - the message
     */
    DomibusConnectorMessage processMessageAfterDeliveredToBackend(DomibusConnectorMessage message);
}
