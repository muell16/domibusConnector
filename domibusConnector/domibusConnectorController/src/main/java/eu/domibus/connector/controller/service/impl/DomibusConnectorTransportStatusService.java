package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.service.TransportStatusService;
import org.springframework.stereotype.Service;

@Service
public class DomibusConnectorTransportStatusService implements TransportStatusService {

    @Override
    public void setTransportStatusForTransportToGateway(DomibusConnectorTransportState transportState) {
        //TODO: trigger content deletion, failure handling, ...
    }

    @Override
    public void setTransportStatusForTransportToBackendClient(DomibusConnectorTransportState transportState) {
        //TODO: trigger content deletion, failure handling, ...
    }

}
