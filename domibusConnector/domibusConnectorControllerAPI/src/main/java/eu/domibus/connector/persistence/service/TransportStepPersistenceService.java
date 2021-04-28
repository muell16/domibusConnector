package eu.domibus.connector.persistence.service;

import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorTransportStep;

import java.util.List;

public interface TransportStepPersistenceService {

    DomibusConnectorTransportStep createNewTransportStep(DomibusConnectorTransportStep transportStep);

    DomibusConnectorTransportStep getTransportStepByTransportId(TransportStateService.TransportId connectorTransportId);

    DomibusConnectorTransportStep update(DomibusConnectorTransportStep transportStep);

    List<DomibusConnectorTransportStep> findPendingStepBy(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName);
}
