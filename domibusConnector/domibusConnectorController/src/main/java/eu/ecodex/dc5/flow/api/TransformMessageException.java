package eu.ecodex.dc5.flow.api;

public class TransformMessageException extends RuntimeException {
    public TransformMessageException() {
    }

    public TransformMessageException(String message) {
        super(message);
    }

    public TransformMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransformMessageException(Throwable cause) {
        super(cause);
    }

}
