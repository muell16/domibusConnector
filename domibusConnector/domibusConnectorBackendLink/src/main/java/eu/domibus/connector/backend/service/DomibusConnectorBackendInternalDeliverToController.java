package eu.domibus.connector.backend.service;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

public interface DomibusConnectorBackendInternalDeliverToController {

    void submitToController(DomibusConnectorBackendMessage message);

    /**
     * sets message as delivered to national system
     *  *) set state in persistence
     *  *) create delivery evidence
     *
     * @param message - the corresponding message
     *
     */
    void markMessageAsDeliveredToNationalSystem(DomibusConnectorMessage message);
}
