package eu.domibus.connector.controller.exception;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

public class DomibusConnectorMessageTransportException extends RuntimeException {

    private DomibusConnectorMessage connectorMessage;

    public DomibusConnectorMessageTransportException(DomibusConnectorMessage message) {
        this.connectorMessage = message;
    }

    public DomibusConnectorMessageTransportException(DomibusConnectorMessage message, Throwable cause) {
        super(cause);
        this.connectorMessage = message;
    }

    public DomibusConnectorMessageTransportException(DomibusConnectorMessage message, String reasonMessage, Throwable cause) {
        super(reasonMessage, cause);
        this.connectorMessage = message;
    }

    public DomibusConnectorMessage getConnectorMessage() {
        return connectorMessage;
    }
}
