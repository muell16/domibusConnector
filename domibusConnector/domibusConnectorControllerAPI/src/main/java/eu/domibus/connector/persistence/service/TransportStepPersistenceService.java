package eu.domibus.connector.persistence.service;

import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorTransportStep;

public interface TransportStepPersistenceService {

    DomibusConnectorTransportStep createNewTransportStep(DomibusConnectorTransportStep transportStep);


    DomibusConnectorTransportStep getTransportStepByTransportId(TransportStatusService.TransportId connectorTransportId);

    DomibusConnectorTransportStep update(DomibusConnectorTransportStep transportStep);
}
