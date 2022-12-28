package eu.domibus.connector.controller.exception;

import eu.ecodex.dc5.message.model.DC5MessageId;

import java.util.Optional;

public class DomainMatchingException extends RuntimeException {

    private final ErrorCode errorCode;
    private final DC5MessageId messageId;

    public DomainMatchingException(ErrorCode errorCode, String message) {
        super(errorCode.toString() + " " + message);
        this.errorCode = errorCode;
        this.messageId = null;
    }

    public DomainMatchingException(DC5MessageId messageId, ErrorCode errorCode, String message) {
        super(errorCode.toString() + " " + message);
        this.errorCode = errorCode;
        this.messageId = messageId;
    }

    public Optional<DC5MessageId> getMessageId() {
        return Optional.ofNullable(this.messageId);
    }

}
