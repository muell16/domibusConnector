package eu.domibus.connector.backend.service;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 * this interface is used in the backendLink only, and provides
 * an abstraction layer for the different transport mechanics
 *  push/pull over webservices, push/push over webservice,
 *  async push/push over jms
 *
 */
public interface DomibusConnectorBackendInternalDeliverToController {

    /**
     * submites message to controller
     * @param message
     */
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
