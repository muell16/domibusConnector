package eu.domibus.connector.domain.model.builder;

import eu.domibus.connector.domain.model.DomibusConnectorAction;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorActionBuilder {

    private String action;
	private boolean documentRequired = false;
    
    public static DomibusConnectorActionBuilder createBuilder() {
        return new DomibusConnectorActionBuilder();
    }
    
    private DomibusConnectorActionBuilder() {}

    public DomibusConnectorActionBuilder setAction(String action) {
        this.action = action;
        return this;
    }

    public DomibusConnectorActionBuilder withDocumentRequired(boolean required) {
        this.documentRequired = required;
        return this;
    }
    
    public DomibusConnectorAction build() {
        if (action == null) {
            throw new IllegalArgumentException("action is required!");
        }
        return new DomibusConnectorAction(action, documentRequired);
    }


    
    
    
}
