package eu.ecodex.dc5.pmode;

import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.persistence.dao.DomibusConnectorKeystoreDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorPModeSetDao;
import eu.domibus.connector.persistence.model.*;
import eu.domibus.connector.persistence.service.exceptions.IncorrectResultSizeException;
import eu.domibus.connector.persistence.service.impl.ServiceMapper;
import eu.ecodex.dc5.domain.repo.DomibusConnectorBusinessDomainDao;
import eu.ecodex.dc5.message.model.DC5Action;
import eu.ecodex.dc5.message.model.DomibusConnectorParty;
import eu.ecodex.dc5.message.model.DC5Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Service
//implements DomibusConnectorPModeService
public class DomibusConnectorPModePersistenceService  {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorPModePersistenceService.class);

    private final DomibusConnectorPModeSetDao domibusConnectorPModeSetDao;
    private final DomibusConnectorBusinessDomainDao messageLaneDao;
    private final DomibusConnectorKeystoreDao keystoreDao;

    public DomibusConnectorPModePersistenceService(DomibusConnectorPModeSetDao domibusConnectorPModeSetDao,
                                                   DomibusConnectorBusinessDomainDao messageLaneDao,
                                                   DomibusConnectorKeystoreDao keystoreDao) {
        this.domibusConnectorPModeSetDao = domibusConnectorPModeSetDao;
        this.messageLaneDao = messageLaneDao;
        this.keystoreDao = keystoreDao;
    }

    
    public List<DC5Action> findByExample(DomibusConnectorBusinessDomain.BusinessDomainId lane, DC5Action searchAction) {
        Optional<PDomibusConnectorPModeSet> currentPModeSetOptional = getCurrentDBPModeSet(lane);
        if (!currentPModeSetOptional.isPresent()) {
            return new ArrayList<>();
        }
//        List<DC5Action> foundActions = findByExample(ActionMapper.mapActionToPersistence(searchAction), currentPModeSetOptional)
//                .map(ActionMapper::mapActionToDomain)
//                .collect(Collectors.toList());
        return null;
    }

    
    public Optional<DC5Action> getConfiguredSingle(DomibusConnectorBusinessDomain.BusinessDomainId lane, DC5Action searchAction) {
//        return getConfiguredSingleDB(lane, ActionMapper.mapActionToPersistence(searchAction))
//                .map(ActionMapper::mapActionToDomain);
        return null;
    }

    public Optional<PDomibusConnectorAction> getConfiguredSingleDB(DomibusConnectorBusinessDomain.BusinessDomainId lane, PDomibusConnectorAction searchAction) {
        Optional<PDomibusConnectorPModeSet> currentPModeSetOptional = getCurrentDBPModeSet(lane);
        if (!currentPModeSetOptional.isPresent()) {
            return Optional.empty();
        }
        List<PDomibusConnectorAction> foundActions = findByExample(searchAction, currentPModeSetOptional)
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

    private Stream<PDomibusConnectorAction> findByExample(PDomibusConnectorAction searchAction, Optional<PDomibusConnectorPModeSet> currentPModeSetOptional) {
        Stream<PDomibusConnectorAction> stream = currentPModeSetOptional
                .get()
                .getActions()
                .stream()
                .filter(action -> {
                    boolean result = true;
                    if (result && searchAction.getAction() != null) {
                        result = result && searchAction.getAction().equals(action.getAction());
                    }
                    return result;
                });
        return stream;
    }

    
    public Optional<DC5Service> getConfiguredSingle(DomibusConnectorBusinessDomain.BusinessDomainId lane, DC5Service searchService) {
        return getConfiguredSingleDB(lane, ServiceMapper.mapServiceToPersistence(searchService))
                .map(ServiceMapper::mapServiceToDomain);
    }

    
    public List<DC5Service> findByExample(DomibusConnectorBusinessDomain.BusinessDomainId lane, DC5Service searchService) {
        Optional<PDomibusConnectorPModeSet> currentPModeSetOptional = getCurrentDBPModeSet(lane);
        if (!currentPModeSetOptional.isPresent()) {
            return new ArrayList<>();
        }
        List<DC5Service> foundServices = findByExampleStream(ServiceMapper.mapServiceToPersistence(searchService), currentPModeSetOptional)
                .map(ServiceMapper::mapServiceToDomain)
                .collect(Collectors.toList());
        return foundServices;
    }


    public Optional<PDomibusConnectorService> getConfiguredSingleDB(DomibusConnectorBusinessDomain.BusinessDomainId lane, PDomibusConnectorService searchService) {
        Optional<PDomibusConnectorPModeSet> currentPModeSetOptional = getCurrentDBPModeSet(lane);
        if (!currentPModeSetOptional.isPresent()) {
            return Optional.empty();
        }
        List<PDomibusConnectorService> foundServices = findByExampleStream(searchService, currentPModeSetOptional)
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

    private Stream<PDomibusConnectorService> findByExampleStream(PDomibusConnectorService searchService, Optional<PDomibusConnectorPModeSet> currentPModeSetOptional) {
        Stream<PDomibusConnectorService> stream = currentPModeSetOptional
                .get()
                .getServices()
                .stream()
                .filter(service -> {
                    boolean result = true;
                    if (result && searchService.getService() != null) {
                        result = result && searchService.getService().equals(service.getService());
                    }
                    if (result && searchService.getServiceType() != null) { //check for null a uri can be a empty string
                        result = result && searchService.getServiceType().equals(service.getServiceType());
                    }
                    return result;
                });
        return stream;
    }

    
    public List<DomibusConnectorParty> findByExample(DomibusConnectorBusinessDomain.BusinessDomainId lane, DomibusConnectorParty exampleParty) throws IncorrectResultSizeException {
//        PDomibusConnectorParty searchParty = PartyMapper.mapPartyToPersistence(exampleParty);
//        Optional<PDomibusConnectorPModeSet> currentPModeSetOptional = getCurrentDBPModeSet(lane);
//        if (!currentPModeSetOptional.isPresent()) {
//            return new ArrayList<>();
//        }
//        Stream<PDomibusConnectorParty> stream = findByExampleStream(searchParty, currentPModeSetOptional);
//        List<DomibusConnectorParty> foundParties = stream.map(PartyMapper::mapPartyToDomain)
//                .collect(Collectors.toList());

        return new ArrayList<>();
    }

    
    public Optional<DomibusConnectorParty> getConfiguredSingle(DomibusConnectorBusinessDomain.BusinessDomainId lane, DomibusConnectorParty searchParty) throws IncorrectResultSizeException {
//        return getConfiguredSingleDB(lane, PartyMapper.mapPartyToPersistence(searchParty))
//                .map(PartyMapper::mapPartyToDomain);
        return Optional.empty();
    }

    public Optional<PDomibusConnectorParty> getConfiguredSingleDB(DomibusConnectorBusinessDomain.BusinessDomainId lane, PDomibusConnectorParty searchParty) throws IncorrectResultSizeException {
        Optional<PDomibusConnectorPModeSet> currentPModeSetOptional = getCurrentDBPModeSet(lane);
        if (!currentPModeSetOptional.isPresent()) {
            return Optional.empty();
        }
        List<PDomibusConnectorParty> foundParties =
                findByExampleStream(searchParty, currentPModeSetOptional).collect(Collectors.toList());

        if (foundParties.size() > 1) {
            throw new IncorrectResultSizeException(String.format("Found %d Parties which match Party [%s] in MessageLane [%s]", foundParties.size(), searchParty, lane));
        }
        if (foundParties.isEmpty()) {
            LOGGER.debug("Found no Parties which match Party [{}] in MessageLane [{}]", searchParty, lane);
            return Optional.empty();
        }
        return Optional.of(foundParties.get(0));
    }

    private Stream<PDomibusConnectorParty> findByExampleStream(PDomibusConnectorParty searchParty, Optional<PDomibusConnectorPModeSet> currentPModeSetOptional) {
        Stream<PDomibusConnectorParty> stream = currentPModeSetOptional
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
                });
        return stream;
    }

    
//    @Cacheable
//    public List<DomibusConnectorPModeSet> getInactivePModeSets(DomibusConnectorBusinessDomain.BusinessDomainId lane){
//    	if (lane == null) {
//            throw new IllegalArgumentException("MessageLaneId is not allowed to be null!");
//        }
//
//    	List<DomibusConnectorPModeSet> result = new ArrayList<DomibusConnectorPModeSet>();
//
//    	List<PDomibusConnectorPModeSet> inactivePModeSets = domibusConnectorPModeSetDao.getInactivePModeSets(lane);
//
//    	inactivePModeSets.forEach(s -> {
//
//    		result.add(mapToDomain(s));
//    		});
//
//    	return result;
//    }

    
//    @CacheEvict
//    @Transactional
//    public void updatePModeConfigurationSet(DomibusConnectorPModeSet connectorPModeSet) {
//        if (connectorPModeSet == null) {
//            throw new IllegalArgumentException("connectorPMode Set is not allowed to be null!");
//        }
//        DomibusConnectorBusinessDomain.BusinessDomainId lane = connectorPModeSet.getMessageLaneId();
//        if (lane == null) {
//            throw new IllegalArgumentException("MessageLaneId is not allowed to be null!");
//        }
//        if (connectorPModeSet.getConnectorstore() == null) {
//            throw new IllegalArgumentException("connectorStoreUUID is not allowed to be null!");
//        }
//
////        connectorPModeSet.getParties().forEach(p -> p.setDbKey(null));
////        connectorPModeSet.getActions().forEach(a -> a.setDbKey(null));
////        connectorPModeSet.getServices().forEach(s -> s.setDbKey(null));
//
//        Optional<DC5BusinessDomainJpaEntity> messageLaneOptional = messageLaneDao.findByName(lane);
//        DC5BusinessDomainJpaEntity DC5BusinessDomainJpaEntity = messageLaneOptional.orElseThrow(() -> new RuntimeException(String.format("No message lane found with name [%s]", lane)));
//
//        PDomibusConnectorPModeSet dbPmodeSet = new PDomibusConnectorPModeSet();
//        dbPmodeSet.setDescription(connectorPModeSet.getDescription());
//        dbPmodeSet.setCreated(Timestamp.from(Instant.now()));
//
//		dbPmodeSet.setPmodes(connectorPModeSet.getpModes());
//
////        dbPmodeSet.setMessageLane(DC5BusinessDomainJpaEntity);
//        dbPmodeSet.setActions(mapActionListToDb(connectorPModeSet.getActions()));
//        dbPmodeSet.setServices(mapServiceListToDb(connectorPModeSet.getServices()));
//        dbPmodeSet.setParties(mapPartiesListToDb(connectorPModeSet.getParties()));
//        dbPmodeSet.setActive(true);
//
//        if (connectorPModeSet.getConnectorstore() == null) {
//            throw new IllegalArgumentException("You must provide a already persisted keystore!");
//        }
//        Optional<PDomibusConnectorKeystore> connectorstore = keystoreDao.findByUuid(connectorPModeSet.getConnectorstore().getUuid());
//        if (!connectorstore.isPresent()) {
//            String error = String.format("There is no JavaKeyStore with id [%s]", connectorPModeSet.getConnectorstore());
//            throw new IllegalArgumentException(error);
//        }
//
//        dbPmodeSet.setConnectorstore(connectorstore.get());
//
//        List<PDomibusConnectorPModeSet> currentActivePModeSet = this.domibusConnectorPModeSetDao.getCurrentActivePModeSet(lane);
//        currentActivePModeSet.forEach(s -> s.setActive(false));
//        this.domibusConnectorPModeSetDao.saveAll(currentActivePModeSet);
//        this.domibusConnectorPModeSetDao.save(dbPmodeSet);
//
//    }

    
//    @Transactional
//    public void updateActivePModeSetDescription(DomibusConnectorPModeSet connectorPModeSet) {
//        DomibusConnectorBusinessDomain.BusinessDomainId lane = connectorPModeSet.getMessageLaneId();
//        if (lane == null) {
//            throw new IllegalArgumentException("MessageLaneId is not allowed to be null!");
//        }
//
//    	List<PDomibusConnectorPModeSet> currentActivePModeSet = this.domibusConnectorPModeSetDao.getCurrentActivePModeSet(lane);
//
//    	currentActivePModeSet.forEach(s -> {
//    		if(s.isActive()) {
//    			s.setDescription(connectorPModeSet.getDescription());
//    			this.domibusConnectorPModeSetDao.save(s);
//    		}
//    	});
//    }

    private Set<PDomibusConnectorParty> mapPartiesListToDb(List<DomibusConnectorParty> parties) {
//        return parties.stream()
//                .map(PartyMapper::mapPartyToPersistence)
//                .collect(Collectors.toSet());
        return new HashSet<>();
    }

    private Set<PDomibusConnectorService> mapServiceListToDb(List<DC5Service> services) {
//        return services.stream()
//                .map(ServiceMapper::mapServiceToPersistence)
//                .collect(Collectors.toSet());
        return new HashSet<>();
    }

    private Set<PDomibusConnectorAction> mapActionListToDb(List<DC5Action> actions) {
//        return actions.stream()
//                .map(ActionMapper::mapActionToPersistence)
//                .collect(Collectors.toSet());
        return null;
    }


    
//    @Cacheable
//    @Transactional(readOnly = true)
//    public Optional<DomibusConnectorPModeSet> getCurrentPModeSet(DomibusConnectorBusinessDomain.BusinessDomainId lane) {
//        return getCurrentDBPModeSet(lane).map(this::mapToDomain);
//    }

    public Optional<PDomibusConnectorPModeSet> getCurrentDBPModeSet(DomibusConnectorBusinessDomain.BusinessDomainId lane) {
        List<PDomibusConnectorPModeSet> currentActivePModeSet = new ArrayList<>(); // = domibusConnectorPModeSetDao.getCurrentActivePModeSet(lane);
        if (currentActivePModeSet.isEmpty()) {
            LOGGER.debug("getCurrentDBPModeSet# no active pMode Set found for message lane [{}]", lane);
        }
        return currentActivePModeSet
                .stream()
                .findFirst();
    }



//    public DomibusConnectorPModeSet mapToDomain(PDomibusConnectorPModeSet dbPmodes) {
//        DomibusConnectorPModeSet pModeSet = new DomibusConnectorPModeSet();
//        pModeSet.setCreateDate(dbPmodes.getCreated());
//        pModeSet.setDescription(dbPmodes.getDescription());
//        pModeSet.setMessageLaneId(dbPmodes.getMessageLane().getName());
//        pModeSet.setpModes(dbPmodes.getPmodes());

//        pModeSet.setConnectorstore(KeystoreMapper.mapKeystoreToDomain(dbPmodes.getConnectorstore()));

//        pModeSet.setParties(
//                dbPmodes.getParties()
//                        .stream()
//                        .map(PartyMapper::mapPartyToDomain)
//                        .collect(Collectors.toList())
//        );
//        pModeSet.setActions(
//                dbPmodes.getActions()
//                .stream()
////                .map(ActionMapper::mapActionToDomain)
//                .collect(Collectors.toList())
//        );
//        pModeSet.setServices(
//                dbPmodes.getServices()
//                .stream()
//                .map(ServiceMapper::mapServiceToDomain)
//                .collect(Collectors.toList())
//        );
//
//        return pModeSet;
//    }

}
