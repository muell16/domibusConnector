package eu.domibus.connector.controller.exception;

import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.transport.model.DC5TransportRequest;

public class DomibusConnectorMessageTransportException extends RuntimeException {

    private DC5Message connectorMessage;
    private DomibusConnectorRejectionReason reason;
    private boolean retryable = false;
    private ErrorCode errorCode;

    public DomibusConnectorMessageTransportException(DC5TransportRequest.TransportRequestId transportRequestId, String errorMessage) {
        super(errorMessage);
    }

    public DomibusConnectorMessageTransportException(DC5Message message, DomibusConnectorRejectionReason reason) {
        this.connectorMessage = message;
        this.reason = reason;
    }

    public DomibusConnectorMessageTransportException(DC5Message message, DomibusConnectorRejectionReason reason, Throwable cause) {
        super(cause);
        this.connectorMessage = message;
        this.reason = reason;
    }

    public DomibusConnectorMessageTransportException(DC5Message message, DomibusConnectorRejectionReason reason, String reasonMessage, Throwable cause) {
        super(reasonMessage, cause);
        this.connectorMessage = message;
        this.reason = reason;
    }

    public DomibusConnectorMessageTransportException(DC5Message message, String errorMessage) {
        super(errorMessage);
        this.connectorMessage = message;
    }

    public DC5Message getConnectorMessage() {
        return connectorMessage;
    }

    public DomibusConnectorRejectionReason getReason() {
        return reason;
    }

    public void setConnectorMessage(DC5Message connectorMessage) {
        this.connectorMessage = connectorMessage;
    }

    public void setReason(DomibusConnectorRejectionReason reason) {
        this.reason = reason;
    }

    public boolean isRetryable() {
        return retryable;
    }

    public void setRetryable(boolean retryable) {
        this.retryable = retryable;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
