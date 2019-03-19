package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.DomibusConnectorRejectDeliveryException;

/**
 * This service provides the ability to reject the delivery to the
 * backend.
 * This service should be called on any point of delivering a message
 * to the backend, if the system is unable to deliver the message.
 * <p>
 * The backendDeliveryRejectionService has to ensure that for the
 * message a NON_DELIVERY is generated and submitted to the sending party
 * if the message is a business_message
 * if the mssage is just an ConfirmationMessage/EvidenceMessage no evidence/confirmation
 * is generated.
 *
 */
public interface DomibusConnectorDeliveryRejectionService {

    public void rejectDelivery(DomibusConnectorRejectDeliveryException cause);

}
