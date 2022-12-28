package eu.domibus.connector.controller.exception;

public class DomibusConnectorControllerException extends RuntimeException {


    private final ErrorCode errorCode;

    public DomibusConnectorControllerException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public DomibusConnectorControllerException(ErrorCode errorCode, String arg0) {
        super(arg0);
        this.errorCode = errorCode;
    }

    public DomibusConnectorControllerException(ErrorCode errorCode, Throwable arg0) {
        super(arg0);
        this.errorCode = errorCode;
    }

    public DomibusConnectorControllerException(ErrorCode errorCode, String arg0, Throwable arg1) {
        super(arg0, arg1);
        this.errorCode = errorCode;
    }

}
