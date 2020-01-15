package eu.domibus.connector.link.service;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("linkplugins")
public class DCPluginBasedBackendDeliveryService implements DomibusConnectorBackendDeliveryService {

    @Override
    public void deliverMessageToBackend(DomibusConnectorMessage message) throws DomibusConnectorControllerException {
        //TODO: implement
        throw new RuntimeException("not implemented yet!");
    }
}
