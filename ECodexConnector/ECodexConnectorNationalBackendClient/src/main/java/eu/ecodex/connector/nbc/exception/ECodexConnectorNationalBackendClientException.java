package eu.ecodex.connector.nbc.exception;

public class ECodexConnectorNationalBackendClientException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 8603977543024262636L;

    public ECodexConnectorNationalBackendClientException() {
    }

    public ECodexConnectorNationalBackendClientException(String message) {
        super(message);
    }

    public ECodexConnectorNationalBackendClientException(Throwable cause) {
        super(cause);
    }

    public ECodexConnectorNationalBackendClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
