package eu.domibus.connector.nbc.exception;

public class DomibusConnectorNationalBackendClientException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 8603977543024262636L;

    public DomibusConnectorNationalBackendClientException() {
    }

    public DomibusConnectorNationalBackendClientException(String message) {
        super(message);
    }

    public DomibusConnectorNationalBackendClientException(Throwable cause) {
        super(cause);
    }

    public DomibusConnectorNationalBackendClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
