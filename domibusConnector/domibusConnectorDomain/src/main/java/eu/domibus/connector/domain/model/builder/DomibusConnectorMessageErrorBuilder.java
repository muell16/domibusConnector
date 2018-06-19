
package eu.domibus.connector.domain.model.builder;

import eu.domibus.connector.domain.model.DomibusConnectorMessageError;

/**
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public final class DomibusConnectorMessageErrorBuilder {

    private String text;
    private String details;
    private String source;

    private DomibusConnectorMessageErrorBuilder() {
    }

    public static DomibusConnectorMessageErrorBuilder createBuilder() {
        return new DomibusConnectorMessageErrorBuilder();
    }

    /**
     * @param text short, main error text
     * @return the builder
     */
    public DomibusConnectorMessageErrorBuilder setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * @param details error details, eg the exception stack trace
     * @return the builder
     */
    public DomibusConnectorMessageErrorBuilder setDetails(String details) {
        this.details = details;
        return this;
    }

    /**
     * @param source Name of the component where the error occured
     * @return the builder
     */
    public DomibusConnectorMessageErrorBuilder setSource(String source) {
        this.source = source;
        return this;
    }

    public DomibusConnectorMessageError build() {
        if (text == null) {
            throw new RuntimeException("Text cannot be null!");
        }
        DomibusConnectorMessageError msgError = new DomibusConnectorMessageError(text, details, source);
        return msgError;
    }


}
