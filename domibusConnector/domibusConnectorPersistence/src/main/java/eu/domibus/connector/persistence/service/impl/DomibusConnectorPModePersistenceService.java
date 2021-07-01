package eu.domibus.connector.persistence.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import eu.domibus.connector.domain.model.DomibusConnectorPModeSet;
import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageLaneDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorPModeSetDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorAction;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageLane;
import eu.domibus.connector.persistence.model.PDomibusConnectorPModeSet;
import eu.domibus.connector.persistence.model.PDomibusConnectorParty;
import eu.domibus.connector.persistence.model.PDomibusConnectorService;
import eu.domibus.connector.persistence.service.DomibusConnectorPModeService;
import eu.domibus.connector.persistence.service.exceptions.IncorrectResultSizeException;

@Service
public class DomibusConnectorPModePersistenceService implements DomibusConnectorPModeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorPModePersistenceService.class);

    @Autowired
    DomibusConnectorPModeSetDao domibusConnectorPModeSetDao;

    @Autowired
    DomibusConnectorMessageLaneDao messageLaneDao;


    @Override
    @Cacheable
    public Optional<DomibusConnectorAction> getConfiguredSingle(DomibusConnectorMessageLane.MessageLaneId lane, DomibusConnectorAction searchAction) {
        return getConfiguredSingleDB(lane, ActionMapper.mapActionToPersistence(searchAction))
                .map(ActionMapper::mapActionToDomain);
    }

    public Optional<PDomibusConnectorAction> getConfiguredSingleDB(DomibusConnectorMessageLane.MessageLaneId lane, PDomibusConnectorAction searchAction) {
        Optional<PDomibusConnectorPModeSet> currentPModeSetOptional = getCurrentDBPModeSet(lane);
        if (!currentPModeSetOptional.isPresent()) {
            return Optional.empty();
        }
        List<PDomibusConnectorAction> foundActions = currentPModeSetOptional
                .get()
                .getActions()
                .stream()
                .filter(action -> {
                    boolean result = true;
                    if (result && searchAction.getAction() != null) {
                        result = result && searchAction.getAction().equals(action.getAction());
                    }
                    return result;
                })
                .collect(Collectors.toList());
        if (foundActions.size() > 1) {
            throw new IncorrectResultSizeException(String.format("Found %d Actions which match Action [%s] in MessageLane [%s]", foundActions.size(), searchAction, lane));
        }
        if (foundActions.isEmpty()) {
            LOGGER.debug("Found no Actions which match Action [{}] in MessageLane [{}]", searchAction, lane);
            return Optional.empty();
        }
        return Optional.of(foundActions.get(0));
    }

    @Override
    @Cacheable
    public Optional<DomibusConnectorService> getConfiguredSingle(DomibusConnectorMessageLane.MessageLaneId lane, DomibusConnectorService searchService) {
        return getConfiguredSingleDB(lane, ServiceMapper.mapServiceToPersistence(searchService))
                .map(ServiceMapper::mapServiceToDomain);
    }


    public Optional<PDomibusConnectorService> getConfiguredSingleDB(DomibusConnectorMessageLane.MessageLaneId lane, PDomibusConnectorService searchService) {
        Optional<PDomibusConnectorPModeSet> currentPModeSetOptional = getCurrentDBPModeSet(lane);
        if (!currentPModeSetOptional.isPresent()) {
            return Optional.empty();
        }
        List<PDomibusConnectorService> foundServices = currentPModeSetOptional
                .get()
                .getServices()
                .stream()
                .filter(service -> {
                    boolean result = true;
                    if (result && searchService.getService() != null) {
                        result = result && searchService.getService().equals(service.getService());
                    }
                    if (result && searchService.getServiceType() != null) {
                        result = result && searchService.getServiceType().equals(service.getServiceType());
                    }
                    return result;
                })
                .collect(Collectors.toList());
        if (foundServices.size() > 1) {
            throw new IncorrectResultSizeException(String.format("Found %d Services which match Service [%s] in MessageLane [%s]", foundServices.size(), searchService, lane));
        }
        if (foundServices.isEmpty()) {
            LOGGER.debug("Found no Services which match Service [{}] in MessageLane [{}]", searchService, lane);
            return Optional.empty();
        }
        return Optional.of(foundServices.get(0));
    }

    @Override
    @Cacheable
    public Optional<DomibusConnectorParty> getConfiguredSingle(DomibusConnectorMessageLane.MessageLaneId lane, DomibusConnectorParty searchParty) throws IncorrectResultSizeException {
        return getConfiguredSingleDB(lane, PartyMapper.mapPartyToPersistence(searchParty))
                .map(PartyMapper::mapPartyToDomain);
    }

    public Optional<PDomibusConnectorParty> getConfiguredSingleDB(DomibusConnectorMessageLane.MessageLaneId lane, PDomibusConnectorParty searchParty) throws IncorrectResultSizeException {
        Optional<PDomibusConnectorPModeSet> currentPModeSetOptional = getCurrentDBPModeSet(lane);
        if (!currentPModeSetOptional.isPresent()) {
            return Optional.empty();
        }
        List<PDomibusConnectorParty> foundParties = currentPModeSetOptional
                .get()
                .getParties()
                .stream()
                .filter(party -> {
                    boolean result = true;
                    if (result && searchParty.getPartyId() != null) {
                        result = result && searchParty.getPartyId().equals(party.getPartyId());
                    }
                    if (result && searchParty.getRole() != null) {
                        result = result && searchParty.getRole().equals(party.getRole());
                    }
                    if (result && searchParty.getRoleType() != null) {
                        result = result && searchParty.getRoleType().equals(party.getRoleType());
                    }
                    if (result && searchParty.getPartyIdType() != null) {
                        result = result && searchParty.getPartyIdType().equals(party.getPartyIdType());
                    }
                    return result;

                }).collect(Collectors.toList());
        if (foundParties.size() > 1) {
            throw new IncorrectResultSizeException(String.format("Found %d Parties which match Party [%s] in MessageLane [%s]", foundParties.size(), searchParty, lane));
        }
        if (foundParties.isEmpty()) {
            LOGGER.debug("Found no Parties which match Party [{}] in MessageLane [{}]", searchParty, lane);
            return Optional.empty();
        }
        return Optional.of(foundParties.get(0));
    }

    @Override
    @CacheEvict
    @Transactional
    public void updatePModeConfigurationSet(DomibusConnectorMessageLane.MessageLaneId lane, DomibusConnectorPModeSet connectorPModeSet) {
        if (lane == null) {
            throw new IllegalArgumentException("MessageLaneId is not allowed to be null!");
        }
        if (connectorPModeSet == null) {
            throw new IllegalArgumentException("connectorPMode Set is not allowed to be null!");
        }

        connectorPModeSet.getParties().forEach(p -> p.setDbKey(null));
        connectorPModeSet.getActions().forEach(a -> a.setDbKey(null));
        connectorPModeSet.getServices().forEach(s -> s.setDbKey(null));

        Optional<PDomibusConnectorMessageLane> messageLaneOptional = messageLaneDao.findByName(lane);
        PDomibusConnectorMessageLane pDomibusConnectorMessageLane = messageLaneOptional.orElseThrow(() -> new RuntimeException(String.format("No message lane found with name [%s]", lane)));

        PDomibusConnectorPModeSet dbPmodeSet = new PDomibusConnectorPModeSet();
        dbPmodeSet.setDescription(connectorPModeSet.getDescription());
        dbPmodeSet.setCreated(Timestamp.from(Instant.now()));
        dbPmodeSet.setMessageLane(pDomibusConnectorMessageLane);
        dbPmodeSet.setActions(mapActionListToDb(connectorPModeSet.getActions()));
        dbPmodeSet.setServices(mapServiceListToDb(connectorPModeSet.getServices()));
        dbPmodeSet.setParties(mapPartiesListToDb(connectorPModeSet.getParties()));
        dbPmodeSet.setActive(true);

        List<PDomibusConnectorPModeSet> currentActivePModeSet = this.domibusConnectorPModeSetDao.getCurrentActivePModeSet(lane);
        currentActivePModeSet.forEach(s -> s.setActive(false));
        this.domibusConnectorPModeSetDao.saveAll(currentActivePModeSet);
        this.domibusConnectorPModeSetDao.save(dbPmodeSet);

    }

    private Set<PDomibusConnectorParty> mapPartiesListToDb(List<DomibusConnectorParty> parties) {
        return parties.stream()
                .map(PartyMapper::mapPartyToPersistence)
                .collect(Collectors.toSet());
    }

    private Set<PDomibusConnectorService> mapServiceListToDb(List<DomibusConnectorService> services) {
        return services.stream()
                .map(ServiceMapper::mapServiceToPersistence)
                .collect(Collectors.toSet());
    }

    private Set<PDomibusConnectorAction> mapActionListToDb(List<DomibusConnectorAction> actions) {
        return actions.stream()
                .map(ActionMapper::mapActionToPersistence)
                .collect(Collectors.toSet());
    }


    @Override
    @Cacheable
    @Transactional(readOnly = true)
    public Optional<DomibusConnectorPModeSet> getCurrentPModeSet(DomibusConnectorMessageLane.MessageLaneId lane) {
        return getCurrentDBPModeSet(lane).map(this::mapToDomain);
    }

    public Optional<PDomibusConnectorPModeSet> getCurrentDBPModeSet(DomibusConnectorMessageLane.MessageLaneId lane) {
        List<PDomibusConnectorPModeSet> currentActivePModeSet = domibusConnectorPModeSetDao.getCurrentActivePModeSet(lane);
        if (currentActivePModeSet.isEmpty()) {
            LOGGER.debug("getCurrentDBPModeSet# no active pMode Set found for message lane [{}]", lane);
        }
        return currentActivePModeSet
                .stream()
                .findFirst();
    }



    public DomibusConnectorPModeSet mapToDomain(PDomibusConnectorPModeSet dbPmodes) {
        DomibusConnectorPModeSet pModeSet = new DomibusConnectorPModeSet();
        pModeSet.setCreateDate(dbPmodes.getCreated());
        pModeSet.setDescription(dbPmodes.getDescription());
        pModeSet.setMessageLaneId(dbPmodes.getMessageLane().getName());

        pModeSet.setParties(
                dbPmodes.getParties()
                        .stream()
                        .map(PartyMapper::mapPartyToDomain)
                        .collect(Collectors.toList())
        );
        pModeSet.setActions(
                dbPmodes.getActions()
                .stream()
                .map(ActionMapper::mapActionToDomain)
                .collect(Collectors.toList())
        );
        pModeSet.setServices(
                dbPmodes.getServices()
                .stream()
                .map(ServiceMapper::mapServiceToDomain)
                .collect(Collectors.toList())
        );

        return pModeSet;
    }

}
