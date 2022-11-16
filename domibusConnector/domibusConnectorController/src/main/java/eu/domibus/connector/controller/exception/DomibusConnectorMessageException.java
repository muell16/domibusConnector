package eu.domibus.connector.controller.exception;

import eu.ecodex.dc5.message.model.DC5Message;

public class DomibusConnectorMessageException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 2899706995862182574L;
   
    private DC5Message DC5Message;
    private Class<?> source;

    public DomibusConnectorMessageException() {
    }

    public DomibusConnectorMessageException(DC5Message message, Class<?> source) {
        super();
        this.DC5Message = message;
        this.source = source;
    }

    public DomibusConnectorMessageException(DC5Message message, Class<?> source, Throwable cause) {
        super(cause);
        this.DC5Message = message;
        this.source = source;
        this.setStackTrace(cause.getStackTrace());
    }

    public DomibusConnectorMessageException(DC5Message message, Class<?> source, String text) {
        super(text);
        this.DC5Message = message;
        this.source = source;
    }

    public DomibusConnectorMessageException(DC5Message message, Class<?> source, Throwable cause, String text) {
        super(text, cause);
        this.DC5Message = message;
        this.source = source;
        this.setStackTrace(cause.getStackTrace());
    }

    public DC5Message getDomibusConnectorMessage() {
        return DC5Message;
    }

    public Class<?> getSource() {
        return source;
    }
    
}
