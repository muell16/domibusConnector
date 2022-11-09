package eu.ecodex.dc5.core.model;

import javax.persistence.Embeddable;

@Embeddable
public class DC5Service {
    private String service;
    private String serviceType;

    public DC5Service() {
    }

    public DC5Service(String service, String serviceType) {
        this.service = service;
        this.serviceType = serviceType;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
