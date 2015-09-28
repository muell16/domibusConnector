package eu.domibus.connector.common.exception;

public class ImplementationMissingException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -2824611216400037174L;
    private static final String MESSAGE = "There must be a national implementation of this method!";

    public ImplementationMissingException() {
        super(MESSAGE);
    }

    public ImplementationMissingException(String arg0, Throwable arg1) {
        super(MESSAGE + arg0, arg1);
    }

    public ImplementationMissingException(String arg0) {
        super(MESSAGE + arg0);
    }

    public ImplementationMissingException(Throwable arg0) {
        super(MESSAGE, arg0);
    }

    public ImplementationMissingException(String interfaceName, String methodName) {
        super(MESSAGE + " Interface: " + interfaceName + ", Method: " + methodName);
    }

}
