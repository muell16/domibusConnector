package eu.ecodex.dc5.pmode;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.persistence.service.exceptions.IncorrectResultSizeException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DC5PModeServiceImpl implements DC5PmodeService {

    @Override
    public List<PModeAction> findByExample(DomibusConnectorBusinessDomain.BusinessDomainId lane, PModeAction action) {
        return null;
    }

    @Override
    public Optional<PModeAction> getConfiguredSingle(DomibusConnectorBusinessDomain.BusinessDomainId lane, PModeAction action) {
        return Optional.empty();
    }

    @Override
    public List<PModeService> findByExample(DomibusConnectorBusinessDomain.BusinessDomainId lane, PModeService DC5Service) {
        return null;
    }

    @Override
    public Optional<PModeService> getConfiguredSingle(DomibusConnectorBusinessDomain.BusinessDomainId lane, PModeService DC5Service) {
        return Optional.empty();
    }

    @Override
    public List<PModeParty> findByExample(DomibusConnectorBusinessDomain.BusinessDomainId lane, PModeParty domibusConnectorParty) throws IncorrectResultSizeException {
        return null;
    }

    @Override
    public Optional<PModeParty> getConfiguredSingle(DomibusConnectorBusinessDomain.BusinessDomainId lane, PModeParty domibusConnectorParty) throws IncorrectResultSizeException {
        return Optional.empty();
    }

    @Override
    public void updatePModeConfigurationSet(DomibusConnectorPModeSet connectorPModeSet) {

    }

    @Override
    public Optional<DomibusConnectorPModeSet> getCurrentPModeSet(DomibusConnectorBusinessDomain.BusinessDomainId lane) {
        return Optional.empty();
    }

    @Override
    public void updateActivePModeSetDescription(DomibusConnectorPModeSet connectorPModeSet) {

    }

    @Override
    public List<DomibusConnectorPModeSet> getInactivePModeSets(DomibusConnectorBusinessDomain.BusinessDomainId lane) {
        return null;
    }
}
