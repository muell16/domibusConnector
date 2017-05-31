package eu.domibus.connector.gpc.validator;

public class DomibusConnectorMessageValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4949584788606144092L;

	public DomibusConnectorMessageValidationException() {
	}

	public DomibusConnectorMessageValidationException(String arg0) {
		super(arg0);
	}

	public DomibusConnectorMessageValidationException(Throwable arg0) {
		super(arg0);
	}

	public DomibusConnectorMessageValidationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DomibusConnectorMessageValidationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
