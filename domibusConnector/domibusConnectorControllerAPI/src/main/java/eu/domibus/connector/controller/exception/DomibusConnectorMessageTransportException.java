package eu.domibus.connector.controller.exception;

import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

public class DomibusConnectorMessageTransportException extends RuntimeException {

    private DomibusConnectorMessage connectorMessage;
    private DomibusConnectorRejectionReason reason;

    public DomibusConnectorMessageTransportException(DomibusConnectorMessage message, DomibusConnectorRejectionReason reason) {
        this.connectorMessage = message;
        this.reason = reason;
    }

    public DomibusConnectorMessageTransportException(DomibusConnectorMessage message, DomibusConnectorRejectionReason reason, Throwable cause) {
        super(cause);
        this.connectorMessage = message;
        this.reason = reason;
    }

    public DomibusConnectorMessageTransportException(DomibusConnectorMessage message, DomibusConnectorRejectionReason reason, String reasonMessage, Throwable cause) {
        super(reasonMessage, cause);
        this.connectorMessage = message;
        this.reason = reason;
    }

    public DomibusConnectorMessage getConnectorMessage() {
        return connectorMessage;
    }

    public DomibusConnectorRejectionReason getReason() {
        return reason;
    }
}
