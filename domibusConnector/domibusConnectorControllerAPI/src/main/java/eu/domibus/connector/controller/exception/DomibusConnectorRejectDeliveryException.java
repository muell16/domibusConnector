package eu.domibus.connector.controller.exception;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

public class DomibusConnectorRejectDeliveryException extends DomibusConnectorMessageTransportException {

    public DomibusConnectorRejectDeliveryException(DomibusConnectorMessage message) {
        super(message);
    }

    public DomibusConnectorRejectDeliveryException(DomibusConnectorMessage message, Throwable cause) {
        super(message, cause);
    }

    public DomibusConnectorRejectDeliveryException(DomibusConnectorMessage message, String reasonMessage, Throwable cause) {
        super(message, reasonMessage, cause);
    }

}
