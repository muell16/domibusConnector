package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.persistence.service.DomibusConnectorPModeService;
import eu.domibus.connector.persistence.service.exceptions.IncorrectResultSizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DomibusConnectorPModePersistenceService implements DomibusConnectorPModeService {

//    @Autowired


    @Override
    public Optional<DomibusConnectorAction> getConfigured(DomibusConnectorMessageLane.MessageLaneId lane, DomibusConnectorAction action) {
        return Optional.empty();
    }

    @Override
    public Optional<DomibusConnectorService> getConfigured(DomibusConnectorMessageLane.MessageLaneId lane, DomibusConnectorService domibusConnectorService) {
        return Optional.empty();
    }

    @Override
    public Optional<DomibusConnectorParty> getConfiguredSingle(DomibusConnectorMessageLane.MessageLaneId lane, DomibusConnectorParty domibusConnectorParty) throws IncorrectResultSizeException {
        return Optional.empty();
    }

    @Override
    public void updatePModeConfigurationSet(DomibusConnectorMessageLane.MessageLaneId lane, DomibusConnectorPModeSet connectorPModeSet) {

    }

    @Override
    public DomibusConnectorPModeSet getCurrentPModeSet(DomibusConnectorMessageLane.MessageLaneId lane) {
        return null;
    }
}
