package eu.domibus.connector.persistence.service.impl;

import eu.ecodex.dc5.message.model.DC5Service;
import eu.domibus.connector.persistence.model.PDomibusConnectorService;

import javax.annotation.Nullable;

public class ServiceMapper {

    static @Nullable
	public DC5Service mapServiceToDomain(@Nullable PDomibusConnectorService persistenceService) {
        if (persistenceService != null) {
            DC5Service service
                    = DC5Service.builder().service(
                    persistenceService.getService()).serviceType(
                    persistenceService.getServiceType()).build();

            return service;
        }
        return null;
    }

    static @Nullable
	public
    PDomibusConnectorService mapServiceToPersistence(@Nullable DC5Service service) {
        if (service != null) {
            PDomibusConnectorService persistenceService = new PDomibusConnectorService();
            persistenceService.setServiceType(service.getServiceType());
            persistenceService.setService(service.getService());
//            persistenceService.setId(service.getDbKey());
            return persistenceService;
        }
        return null;
    }

}
