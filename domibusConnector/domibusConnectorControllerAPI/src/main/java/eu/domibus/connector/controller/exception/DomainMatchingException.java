package eu.domibus.connector.controller.exception;

import eu.ecodex.dc5.message.model.DomibusConnectorMessageId;

import java.util.Optional;

public class DomainMatchingException extends RuntimeException {

    private final ErrorCode errorCode;
    private final DomibusConnectorMessageId messageId;

    public DomainMatchingException(ErrorCode errorCode, String message) {
        super(errorCode.toString() + " " + message);
        this.errorCode = errorCode;
        this.messageId = null;
    }

    public DomainMatchingException(DomibusConnectorMessageId messageId, ErrorCode errorCode, String message) {
        super(errorCode.toString() + " " + message);
        this.errorCode = errorCode;
        this.messageId = messageId;
    }

    public Optional<DomibusConnectorMessageId> getMessageId() {
        return Optional.ofNullable(this.messageId);
    }

}
