package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.persistence.model.PDomibusConnectorService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nullable;

public class ServiceMapper {

    static @Nullable
    DomibusConnectorService mapServiceToDomain(@Nullable PDomibusConnectorService persistenceService) {
        if (persistenceService != null) {
            eu.domibus.connector.domain.model.DomibusConnectorService service
                    = new eu.domibus.connector.domain.model.DomibusConnectorService(
                    persistenceService.getService(),
                    persistenceService.getServiceType()
            );
            return service;
        }
        return null;
    }

    static @Nullable
    PDomibusConnectorService mapServiceToPersistence(@Nullable DomibusConnectorService service) {
        if (service != null) {
            PDomibusConnectorService persistenceService = new PDomibusConnectorService();
            BeanUtils.copyProperties(service, persistenceService);
            return persistenceService;
        }
        return null;
    }

}
