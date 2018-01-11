/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.domain.model.builder;

import eu.domibus.connector.domain.model.DomibusConnectorService;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorServiceBuilder {
    
    private String service;
	private String serviceType;
    
    public static DomibusConnectorServiceBuilder createBuilder() {
        return new DomibusConnectorServiceBuilder();
    }
    
    private DomibusConnectorServiceBuilder() {}

    public void setService(String service) {
        this.service = service;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    
    public DomibusConnectorService build() {
        if (service == null) {
            throw new IllegalArgumentException("Service must be set!");
        }
        if (serviceType == null) {
            throw new IllegalArgumentException("serviceType must be set!");
        }
        return new DomibusConnectorService(service, serviceType);
    }

}
