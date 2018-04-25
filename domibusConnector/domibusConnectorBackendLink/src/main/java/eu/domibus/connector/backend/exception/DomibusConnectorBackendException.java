package eu.domibus.connector.backend.exception;

public class DomibusConnectorBackendException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DomibusConnectorBackendException() {
	}

	public DomibusConnectorBackendException(String arg0) {
		super(arg0);
	}

	public DomibusConnectorBackendException(Throwable arg0) {
		super(arg0);
	}

	public DomibusConnectorBackendException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DomibusConnectorBackendException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
