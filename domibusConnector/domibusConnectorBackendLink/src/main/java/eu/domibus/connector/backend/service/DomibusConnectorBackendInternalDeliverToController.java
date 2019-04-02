package eu.domibus.connector.backend.service;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.controller.exception.DomibusConnectorRejectDeliveryException;
import eu.domibus.connector.controller.service.DomibusConnectorDeliveryRejectionService;
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
     * submites a message to the controller
     * @param message
     */
    void submitToController(DomibusConnectorBackendMessage message);


    /**
     * If the message cannot be delivered to the backend the message is going to be
     * rejected by the backend transport mechanism. To inform the backendLinkController about that this method
     * should be called.
     * <p>
     * The backendLinkController now has to deal with the fact, that the message
     * cannot be delivered by the transport implementation. The BackendInternalDeliverToController implemenation
     * can decide to do an retry or immediately reject the message.
     * By calling {@link DomibusConnectorDeliveryRejectionService#rejectDelivery(DomibusConnectorRejectDeliveryException)}
     *
     *
     */
    void rejectDelivery(DomibusConnectorBackendMessage message, Throwable reason);

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
