package eu.ecodex.connector.xml.exception;

/**
 * This exception class wraps all exceptions thrown from the Axiom API
 * as the user does not need to be bound by such checked exceptions.
 * @author dinuka
 *
 */
public class AxiomBuilderException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -7853903625725204661L;

    public AxiomBuilderException(Throwable ex) {
        super(ex);
    }

    public AxiomBuilderException(String msg) {
        super(msg);
    }
}


