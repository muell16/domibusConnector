package eu.domibus.connector.controller.exception;

public class DomainMatchingException extends RuntimeException {

    private final ErrorCode errorCode;

    public DomainMatchingException(ErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
    }

    public DomainMatchingException(ErrorCode errorCode, String message) {
        super(errorCode.toString() + " " + message);
        this.errorCode = errorCode;
    }

    public DomainMatchingException(ErrorCode errorCode, Throwable e) {
        super(errorCode.toString(), e);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
