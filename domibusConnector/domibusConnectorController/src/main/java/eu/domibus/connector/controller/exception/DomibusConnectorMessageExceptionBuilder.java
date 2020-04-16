
package eu.domibus.connector.controller.exception;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import javax.annotation.Nonnull;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorMessageExceptionBuilder {

    private DomibusConnectorMessage message;
    private DomibusConnectorMessage.DomibusConnectorMessageId messageId;
    private Class<?> source;
    private Throwable cause;
    private String text;

    private DomibusConnectorMessageExceptionBuilder() {}
    
    public static DomibusConnectorMessageExceptionBuilder createBuilder() {
        return new DomibusConnectorMessageExceptionBuilder();
    }
    
    public DomibusConnectorMessageExceptionBuilder setMessage(DomibusConnectorMessage message) {
        this.message = message;
        return this;
    }

    public DomibusConnectorMessageExceptionBuilder setMessage(DomibusConnectorMessage.DomibusConnectorMessageId message) {
        this.messageId = message;
        return this;
    }

    /**
     * can be called to set the source of the exception or call setSourceObject
     * @param source sets the source component of the exception
     * @return the builder
     */
    public DomibusConnectorMessageExceptionBuilder setSource(Class<?> source) {
        this.source = source;
        return this;
    }

    /**
     * can be called to set the source of the exception or call setSource
     * @param object - sets the source object of the exception (calls object.getClass())
     * @return the builder
     */
    public DomibusConnectorMessageExceptionBuilder setSourceObject(@Nonnull Object object) {
        this.source = object.getClass();
        return this;
    }
    
    public DomibusConnectorMessageExceptionBuilder setCause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    public DomibusConnectorMessageExceptionBuilder setText(String text) {
        this.text = text;
        return this;
    }
    
    public DomibusConnectorMessageException build() {
        if (message == null) {
            throw new IllegalArgumentException("Cannot create Exception without message set!");
        }
        
        DomibusConnectorMessageException exception;
        if (text != null && cause != null) {
            exception = new DomibusConnectorMessageException(message, source, cause, text);
        } else if (text != null) {
            exception = new DomibusConnectorMessageException(message, source, text);
        } else if (cause != null) {
            exception = new DomibusConnectorMessageException(message, source, cause);
        } else {
            exception = new DomibusConnectorMessageException(message, source);
        }                
        return exception;
    }
    
    public void buildAndThrow() throws DomibusConnectorMessageException {
        DomibusConnectorMessageException build = build();
        throw build;
    }

}
