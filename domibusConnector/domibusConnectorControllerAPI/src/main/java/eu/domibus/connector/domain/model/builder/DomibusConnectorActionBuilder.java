package eu.domibus.connector.domain.model.builder;

import eu.ecodex.dc5.message.model.DC5Action;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public final class DomibusConnectorActionBuilder {

    private String action;
//	private boolean documentRequired;
    
    public static DomibusConnectorActionBuilder createBuilder() {
        return new DomibusConnectorActionBuilder();
    }
    
    private DomibusConnectorActionBuilder() {}

    public DomibusConnectorActionBuilder setAction(String action) {
        this.action = action;
        return this;
    }

//    public DomibusConnectorActionBuilder withDocumentRequired(boolean required) {
//        this.documentRequired = required;
//        return this;
//    }
    
    public DC5Action build() {
        if (action == null) {
            throw new IllegalArgumentException("action is required!");
        }
        return DC5Action.builder()
                .action(action)
                .build();
    }

    public DomibusConnectorActionBuilder copyPropertiesFrom(DC5Action action) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null here!");
        }
        this.action = action.getAction();
        return this;
    }


}
