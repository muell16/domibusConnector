package eu.ecodex.dc5.flow.api;

public class StepFailedException extends RuntimeException {
    public StepFailedException() {
    }

    public StepFailedException(String message) {
        super(message);
    }

    public StepFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public StepFailedException(Throwable cause) {
        super(cause);
    }

}

