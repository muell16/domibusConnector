package eu.domibus.connector.controller.exception;

import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.ecodex.dc5.message.model.DC5Message;

public class DomibusConnectorRejectDeliveryException extends DomibusConnectorMessageTransportException {

    public DomibusConnectorRejectDeliveryException(DC5Message message, DomibusConnectorRejectionReason reason) {
        super(message, reason);
    }

    public DomibusConnectorRejectDeliveryException(DC5Message message, DomibusConnectorRejectionReason reason, Throwable cause) {
        super(message, reason, cause);
    }

    public DomibusConnectorRejectDeliveryException(DC5Message message, DomibusConnectorRejectionReason reason, String reasonMessage, Throwable cause) {
        super(message, reason, reasonMessage, cause);
    }

}
