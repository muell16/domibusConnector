package eu.domibus.connector.backend.ws.helper;

@Deprecated // "use class from package eu.domibus.connector.link.common instead!"
public class WsPolicyLoaderException extends RuntimeException {
    public WsPolicyLoaderException() {
    }

    public WsPolicyLoaderException(String message) {
        super(message);
    }

    public WsPolicyLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public WsPolicyLoaderException(Throwable cause) {
        super(cause);
    }

    public WsPolicyLoaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}