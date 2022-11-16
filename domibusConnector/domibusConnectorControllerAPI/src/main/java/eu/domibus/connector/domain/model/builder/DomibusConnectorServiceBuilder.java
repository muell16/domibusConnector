
package eu.domibus.connector.domain.model.builder;

import eu.ecodex.dc5.message.model.DC5Service;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public final class DomibusConnectorServiceBuilder {
    
    private String service;
	private String serviceType;
    
    public static DomibusConnectorServiceBuilder createBuilder() {
        return new DomibusConnectorServiceBuilder();
    }
    
    private DomibusConnectorServiceBuilder() {}

    public DomibusConnectorServiceBuilder setService(String service) {
        this.service = service;
        return this;
    }

    public DomibusConnectorServiceBuilder withServiceType(String serviceType) {
        this.serviceType = serviceType;
        return this;
    }
    
    public DC5Service build() {
        if (service == null) {
            throw new IllegalArgumentException("Service must be set!");
        }
        return new DC5Service(service, serviceType);
    }

    public DomibusConnectorServiceBuilder copyPropertiesFrom(DC5Service service) {
        this.service = service.getService();
        this.serviceType = service.getServiceType();
        return this;
    }

}
