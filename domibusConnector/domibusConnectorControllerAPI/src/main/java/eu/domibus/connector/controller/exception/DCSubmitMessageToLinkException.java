package eu.domibus.connector.controller.exception;

public final class DCSubmitMessageToLinkException extends DomibusConnectorControllerException {
    public DCSubmitMessageToLinkException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DCSubmitMessageToLinkException(ErrorCode errorCode, String arg0) {
        super(errorCode, arg0);
    }

    public DCSubmitMessageToLinkException(ErrorCode errorCode, Throwable arg0) {
        super(errorCode, arg0);
    }

    public DCSubmitMessageToLinkException(ErrorCode errorCode, String arg0, Throwable arg1) {
        super(errorCode, arg0, arg1);
    }
}
