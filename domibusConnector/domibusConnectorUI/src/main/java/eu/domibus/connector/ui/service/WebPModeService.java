package eu.domibus.connector.ui.service;

import eu.domibus.configuration.Configuration;
import eu.domibus.configuration.Configuration.BusinessProcesses.Parties.PartyIdTypes.PartyIdType;
import eu.domibus.configuration.Configuration.BusinessProcesses.Roles.Role;
import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.controller.routing.DCMessageRoutingConfigurationProperties;
import eu.domibus.connector.controller.routing.DomainRoutingRule;
import eu.domibus.connector.controller.routing.RoutingRulePattern;
import eu.domibus.connector.controller.spring.ConnectorMessageProcessingProperties;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorKeystore;
import eu.domibus.connector.domain.model.DomibusConnectorKeystore.KeystoreType;
import eu.domibus.connector.evidences.spring.EvidencesToolkitConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import eu.domibus.connector.persistence.service.DomibusConnectorKeystorePersistenceService;
import eu.domibus.connector.persistence.spring.DatabaseResourceLoader;
import eu.domibus.connector.security.configuration.DCEcodexContainerProperties;
import eu.domibus.connector.ui.view.areas.configuration.ChangedPropertiesDialogFactory;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.ecodex.dc5.pmode.DC5PmodeService;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class WebPModeService {

    protected final static Logger LOGGER = LoggerFactory.getLogger(WebPModeService.class);

    private final DC5PmodeService pModeService;
    private final DCBusinessDomainManager domainManager;
    private final DomibusConnectorKeystorePersistenceService keystorePersistenceService;
    private final ConfigurationPropertyManagerService configurationPropertyManagerService;
    private final ChangedPropertiesDialogFactory dialogFactory;

    public WebPModeService(DC5PmodeService pModeService,
                           DCBusinessDomainManager domainManager,
                           DomibusConnectorKeystorePersistenceService keystorePersistenceService,
                           ConfigurationPropertyManagerService configurationPropertyManagerService, ChangedPropertiesDialogFactory dialogFactory) {
        this.pModeService = pModeService;
        this.domainManager = domainManager;
        this.keystorePersistenceService = keystorePersistenceService;
        this.configurationPropertyManagerService = configurationPropertyManagerService;
        this.dialogFactory = dialogFactory;
    }

    public static Object byteArrayToXmlObject(final byte[] xmlAsBytes, final Class<?> instantiationClazz,
                                              final Class<?>... initializationClasses) throws Exception {

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(xmlAsBytes);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document document = factory.newDocumentBuilder().parse(bis);

            JAXBContext ctx = JAXBContext.newInstance(initializationClasses);

            Unmarshaller unmarshaller = ctx.createUnmarshaller();

            JAXBElement<?> jaxbElement = unmarshaller.unmarshal(document, instantiationClazz);
            return jaxbElement.getValue();
        } catch (JAXBException | SAXException | IOException | ParserConfigurationException e) {
            throw new Exception("Exception parsing byte[] to " + instantiationClazz.getName(), e);
        }

    }

    @Transactional(readOnly = false)
    public boolean importPModes(byte[] contents, String description, DomibusConnectorKeystore store, DC5BusinessDomain.BusinessDomainId domainId) {
        if (contents == null || contents.length < 1) {
            throw new IllegalArgumentException("pModes are not allowed to be null or empty!");
        }
        LOGGER.debug("Starting import of PModes");
        Configuration pmodes = null;
        try {
            pmodes = (Configuration) byteArrayToXmlObject(contents, Configuration.class, Configuration.class);
            String referenceString = uploadPModeFile(contents);
            updatePModes(referenceString, domainId);
        } catch (Exception e) {
            LOGGER.error("Cannot load provided pmode file!", e);
            throw new RuntimeException(e);
        }

        DC5PmodeService.DomibusConnectorPModeSet pModeSet;

        try {
            final DCEcodexContainerProperties ecxSettings = getChangesToSecurityConfig(store);
            final EvidencesToolkitConfigurationProperties evSettings = getChangesToHomePartyConfig(pmodes);
            final DCMessageRoutingConfigurationProperties routingSettings = getChangesToDomainRuleConfig(pmodes, domainId);

            final List<Object> configs = Arrays.asList(ecxSettings, evSettings, routingSettings);

            dialogFactory.createChangedPropertiesDialog(configs, domainId);

            //NOTE: updatedConfiguration  contains ONLY changed Properties!
        } catch (Exception e) {
            LOGGER.error("Error while updating properties", e);
            throw new RuntimeException(e);
        }

        return true;
    }

    private ConnectorMessageProcessingProperties updatePModes(String referenceString, @NotNull DC5BusinessDomain.BusinessDomainId domainNameAkaId) {
        ConnectorMessageProcessingProperties props = configurationPropertyManagerService.loadConfiguration(domainNameAkaId, ConnectorMessageProcessingProperties.class);
        if (props.getPModeConfig() == null) {
            props.setPModeConfig(new ConnectorMessageProcessingProperties.PModeConfig());
        }
        var pModeConfig = props.getPModeConfig();
        pModeConfig.setPModeLocation(DatabaseResourceLoader.DB_URL_PREFIX + referenceString);
        pModeConfig.setChangedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        pModeConfig.setUploadDate(LocalDateTime.now());

        return props;
    }

    private String uploadPModeFile(byte[] content) {
        final DomibusConnectorKeystore domibusConnectorKeystore = new DomibusConnectorKeystore();
        domibusConnectorKeystore.setKeystoreBytes(content);
        DomibusConnectorKeystore result = keystorePersistenceService.persistNewKeystore(domibusConnectorKeystore);
        return result.getUuid();
    }

    /**
     * Update the security toolki configuration settings
     * within the current business domain
     *
     * @param store - the StoreSettings to update
     * @return
     */
    private DCEcodexContainerProperties getChangesToSecurityConfig(DomibusConnectorKeystore store) {
        DCEcodexContainerProperties dcEcodexContainerProperties = configurationPropertyManagerService.loadConfiguration(DC5BusinessDomain.getDefaultBusinessDomainId(), DCEcodexContainerProperties.class);

        StoreConfigurationProperties storeConfigurationProperties = new StoreConfigurationProperties();
        storeConfigurationProperties.setPassword(store.getPasswordPlain());
        storeConfigurationProperties.setType(store.getType().toString());
        storeConfigurationProperties.setPath(DatabaseResourceLoader.DB_URL_PREFIX + store.getUuid());
        dcEcodexContainerProperties.getSignatureValidation().setTrustStore(storeConfigurationProperties);

        return dcEcodexContainerProperties;
    }

    private EvidencesToolkitConfigurationProperties getChangesToHomePartyConfig(Configuration pmodes) {
        String homePartyName = pmodes.getParty();

        Configuration.BusinessProcesses.Parties.Party homeParty = pmodes
                .getBusinessProcesses()
                .getParties()
                .getParty()
                .stream()
                .filter(p -> p.getName().equals(homePartyName))
                .findFirst()
                .get();

        EvidencesToolkitConfigurationProperties homePartyConfigurationProperties = configurationPropertyManagerService.loadConfiguration(DC5BusinessDomain.getDefaultBusinessDomainId(), EvidencesToolkitConfigurationProperties.class);
        homePartyConfigurationProperties.getIssuerInfo().getAs4Party().setName(homeParty.getIdentifier().get(0).getPartyId());
        homePartyConfigurationProperties.getIssuerInfo().getAs4Party().setEndpointAddress(homeParty.getEndpoint());

        return homePartyConfigurationProperties;
    }

    private DCMessageRoutingConfigurationProperties getChangesToDomainRuleConfig(Configuration pmodes, DC5BusinessDomain.BusinessDomainId domainId) {
        final DCMessageRoutingConfigurationProperties msgRoutingConfig = configurationPropertyManagerService.loadConfiguration(domainId, DCMessageRoutingConfigurationProperties.class);
        final Map<String, DomainRoutingRule> currentRules = msgRoutingConfig.getDomainRules();
        for (Configuration.BusinessProcesses.Services.Service s : pmodes.getBusinessProcesses().getServices().getService()) {
            final DomainRoutingRule rule = new DomainRoutingRule();
            rule.setConfigurationSource(ConfigurationSource.DB);
            rule.setMatchClause(new RoutingRulePattern(String.format("&(equals(ServiceName, '%s'), equals(ServiceType, '%s'))", s.getValue(), s.getType())));
            currentRules.put(rule.getRoutingRuleId(), rule);
        }

        return msgRoutingConfig;
    }

    public EvidencesToolkitConfigurationProperties getHomeParty(Configuration config) {
        final Optional<Configuration.BusinessProcesses.Parties.Party> partyEndpoint = config.getBusinessProcesses().getParties().getParty().stream().filter(p -> p.getName() == config.getParty()).findFirst();
        EvidencesToolkitConfigurationProperties homePartyConfigurationProperties = configurationPropertyManagerService.loadConfiguration(DC5BusinessDomain.getDefaultBusinessDomainId(), EvidencesToolkitConfigurationProperties.class);
        if (partyEndpoint.isPresent()) {
            homePartyConfigurationProperties.getIssuerInfo().getAs4Party().setName(partyEndpoint.get().getName());
            homePartyConfigurationProperties.getIssuerInfo().getAs4Party().setEndpointAddress(partyEndpoint.get().getEndpoint());
        } else {
            LOGGER.error("Could not load name/endpoint from xml/configuration.");
            throw new RuntimeException("Could not load endpoint.");
        }
        return homePartyConfigurationProperties;
    }


    @Transactional(readOnly = false)
    public DomibusConnectorKeystore importConnectorstore(byte[] connectorstoreBytes, String password, KeystoreType connectorstoreType) {
        DomibusConnectorKeystore connectorstore = new DomibusConnectorKeystore();

        String description = "Connectorstore uploaded with PMode-Set imported at " + new Date();
        connectorstore.setDescription(description);

        connectorstore.setKeystoreBytes(connectorstoreBytes);
        connectorstore.setPasswordPlain(password);
        connectorstore.setType(connectorstoreType);

        connectorstore = keystorePersistenceService.persistNewKeystore(connectorstore);

        return connectorstore;
    }


    private DC5PmodeService.DomibusConnectorPModeSet mapPModeConfigurationToPModeSet(Configuration pmodes, byte[] contents, String description, DomibusConnectorKeystore connectorstore) {
        DC5PmodeService.DomibusConnectorPModeSet pModeSet = new DC5PmodeService.DomibusConnectorPModeSet();
        pModeSet.setDescription(description);
//        pModeSet.setpModes(contents);
        pModeSet.setConnectorstore(connectorstore);

        pModeSet.setServices(importServices(pmodes));
        pModeSet.setActions(importActions(pmodes));
        pModeSet.setParties(importParties(pmodes));
//        pModeSet.setCreateDate(new Date());
        return pModeSet;
    }


    private List<DC5PmodeService.PModeService> importServices(Configuration pmodes) {
//        return pmodes.getBusinessProcesses()
//                .getServices()
//                .getService()
//                .stream()
//                .map(s -> {
//                    DC5Service service = new DC5Service();
//                    service.setService(s.getValue());
//                    service.setServiceType(s.getType());
//                    return service;
//                })
//                .collect(Collectors.toList());
        return new ArrayList<>();
    }

    private List<DC5PmodeService.PModeAction> importActions(Configuration pmodes) {

//        return pmodes.getBusinessProcesses()
//                .getActions()
//                .getAction()
//                .stream()
//                .map(a -> {
//                    DC5Action action = new DC5Action("action");
//                    action.setAction(a.getValue());
//                    return action;
//                })
//                .collect(Collectors.toList());
        return new ArrayList<>();
    }

    private List<DC5PmodeService.PModeParty> importParties(Configuration pmodes) {
        String homeParty = pmodes.getParty();


        Map<String, Role> roles = pmodes.getBusinessProcesses()
                .getRoles()
                .getRole()
                .stream()
                .collect(Collectors.toMap(r -> r.getName(), Function.identity()));

        Map<String, Configuration.BusinessProcesses.Parties.Party> parties = pmodes
                .getBusinessProcesses()
                .getParties()
                .getParty()
                .stream()
                .collect(Collectors.toMap(p -> p.getName(), Function.identity()));

        Map<String, PartyIdType> partyIdTypes = pmodes
                .getBusinessProcesses()
                .getParties()
                .getPartyIdTypes()
                .getPartyIdType()
                .stream()
                .collect(Collectors.toMap(p -> p.getName(), Function.identity()));

//        List<DomibusConnectorParty> importedParties = pmodes.getBusinessProcesses()
//                .getProcess()
//                .stream()
//                .map(process ->
//                        Stream.of(process
//                                        .getInitiatorParties()
//                                        .getInitiatorParty()
//                                        .stream()
//                                        .map(initiatorParty -> this.createParty(partyIdTypes, parties, roles.get(process.getInitiatorRole()), initiatorParty.getName(), DomibusConnectorParty.PartyRoleType.INITIATOR)),
//                                process.
//                                        getResponderParties().
//                                        getResponderParty().
//                                        stream().
//                                        map(responderParty -> this.createParty(partyIdTypes, parties, roles.get(process.getResponderRole()), responderParty.getName(), DomibusConnectorParty.PartyRoleType.RESPONDER))
//                        ).flatMap(Function.identity())
//                ).flatMap(Function.identity())
//                .flatMap(Function.identity())
//                .distinct() //remove duplicate parties
//                .collect(Collectors.toList());
//        return importedParties;
        return new ArrayList<>();

    }


//    private Stream<DomibusConnectorParty> createParty(Map<String, PartyIdType> partyIdTypes, Map<String, Configuration.BusinessProcesses.Parties.Party> parties,
//                                                      Role role, String partyName, DomibusConnectorParty.PartyRoleType roleType) {
//
//        return parties.get(partyName)
//                .getIdentifier()
//                .stream()
//                .map(identifier -> {
//                    DomibusConnectorParty p = new DomibusConnectorParty();
//                    p.setPartyName(partyName);
//                    p.setRole(role.getValue());
//                    p.setPartyId(identifier.getPartyId());
//                    p.setRoleType(roleType);
//                    String partyIdTypeValue = partyIdTypes.get(identifier.getPartyIdType()).getValue();
//                    p.setPartyIdType(partyIdTypeValue);
//                    return p;
//                });
//    }


    public List<DC5PmodeService.PModeParty> getPartyList() {
        return getCurrentPModeSetOrNewSet().getParties();
    }

    public List<DC5PmodeService.PModeAction> getActionList() {
        return getCurrentPModeSetOrNewSet().getActions();
    }

    public List<String> getActionListString() {
        return this.getActionList()
                .stream()
                .map(DC5PmodeService.PModeAction::getAction)
                .collect(Collectors.toList());
    }

    public List<DC5PmodeService.PModeService> getServiceList() {
        return getCurrentPModeSetOrNewSet().getServices();
    }

    public List<String> getServiceListString() {
        return this.getServiceList()
                .stream()
                .map(DC5PmodeService.PModeService::getService)
                .collect(Collectors.toList());
    }

//    @Transactional(readOnly = false)
//    public void deleteParty(DomibusConnectorPModeService.PModeParty p) {
//        LOGGER.trace("#deleteParty: called, use partyDao to delete");
//        DomibusConnectorPModeService.DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
//        pModes.getParties().remove(p);
//        pModes.setDescription(String.format("delete party %s clicked in UI", p));
//        updatePModeSet(pModes);
//    }
////
//    @Transactional(readOnly = false)
//    public DomibusConnectorPModeService.PModeParty updateParty(DomibusConnectorParty oldParty, DomibusConnectorParty updatedParty) {
//        LOGGER.trace("#updateParty: called, update party [{}] to party [{}]", oldParty, updatedParty);
//        DomibusConnectorPModeService.DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
//        pModes.getParties().remove(oldParty);
//        pModes.getParties().add(updatedParty);
//        pModes.setDescription(String.format("updated party %s in UI", updatedParty));
//        //find party in new p-modes by equals
//        return updatePModeSet(pModes)
//                .getParties()
//                .stream()
//                .filter(p -> p.equals(updatedParty))
//                .findAny()
//                .get();
//    }
//
//    @Transactional
//    public DomibusConnectorParty createParty(DomibusConnectorParty party) {
//        LOGGER.trace("#createParty: called with party [{}]", party);
//        DomibusConnectorPModeService.DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
//        pModes.getParties().add(party);
//        pModes.setDescription(String.format("added party %s in UI", party));
//        //find party in new p-modes by equals
//        return updatePModeSet(pModes)
//                .getParties()
//                .stream()
//                .filter(p -> p.equals(party))
//                .findAny()
//                .get();
//    }
//
//    @Transactional
//    public void deleteAction(DC5Action action) {
//        LOGGER.trace("deleteAction: delete Action [{}]", action);
//        DomibusConnectorPModeService.DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
//        pModes.getActions().remove(action);
//        pModes.setDescription(String.format("delete action %s clicked in UI", action));
//        updatePModeSet(pModes);
//    }
//
//    @Transactional
//    public DC5Action createAction(DC5Action action) {
//        LOGGER.trace("#createAction: called with action [{}]", action);
//        DomibusConnectorPModeService.DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
//        pModes.getActions().add(action);
//        pModes.setDescription(String.format("added action %s in UI", action));
//        //find party in new p-modes by equals
//        return updatePModeSet(pModes)
//                .getActions()
//                .stream()
//                .filter(p -> p.equals(action))
//                .findAny()
//                .get();
//    }
//
//    @Transactional(readOnly = false)
//    public DC5Action updateAction(DC5Action oldAction, DC5Action updatedAction) {
//        LOGGER.trace("updateAction: updateAction with oldAction [{}] and new action [{}]", oldAction, updatedAction);
//        DomibusConnectorPModeService.DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
//        pModes.getActions().remove(oldAction);
//        pModes.getActions().add(updatedAction);
//        pModes.setDescription(String.format("updated action %s in UI", updatedAction));
//        //find party in new p-modes by equals
//        return updatePModeSet(pModes)
//                .getActions()
//                .stream()
//                .filter(p -> p.equals(updatedAction))
//                .findAny()
//                .get();
//    }
//
//    @Transactional(readOnly = false)
//    public DC5Service createService(DC5Service service) {
//        LOGGER.trace("createService: with service [{}]", service);
//        DomibusConnectorPModeService.DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
//        pModes.getServices().add(service);
//        pModes.setDescription(String.format("added service %s in UI", service));
//        //find party in new p-modes by equals
//        return updatePModeSet(pModes)
//                .getServices()
//                .stream()
//                .filter(p -> p.equals(service))
//                .findAny()
//                .get();
//    }
//
//    @Transactional
//    public DC5Service updateService(DC5Service oldService, DC5Service updatedService) {
//        LOGGER.trace("updateService: with new service [{}]", updatedService);
//        DomibusConnectorPModeService.DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
//        pModes.getServices().remove(oldService);
//        pModes.getServices().add(updatedService);
//        pModes.setDescription(String.format("updated service %s in UI", updatedService));
//        //find party in new p-modes by equals
//        return updatePModeSet(pModes)
//                .getServices()
//                .stream()
//                .filter(p -> p.equals(updatedService))
//                .findAny()
//                .get();
//    }
//
//    @Transactional(readOnly = false)
//    public void deleteService(DC5Service service) {
//        LOGGER.trace("deleteService: with service [{}]", service);
//        DomibusConnectorPModeService.DomibusConnectorPModeSet pModes = this.getCurrentPModeSetOrNewSet();
//        pModes.getServices().remove(service);
//        pModes.setDescription(String.format("delete service %s clicked in UI", service));
//        updatePModeSet(pModes);
//    }

//    private DC5PmodeService.DomibusConnectorPModeSet updatePModeSet(DC5PmodeService.DomibusConnectorPModeSet pModes) {
//        DomibusConnectorBusinessDomain.BusinessDomainId laneId = pModes.getBusinessDomainId();
//        if (laneId == null) {
//            laneId = DomibusConnectorBusinessDomain.getDefaultBusinessDomainId();
//            LOGGER.info("Setting default lane [{}] pModeSet", laneId);
//            pModes.setBusinessDomainId(laneId);
//        }
//        this.pModeService.updatePModeConfigurationSet(pModes);
//        return this.getCurrentPModeSet(laneId).orElseThrow(() -> new IllegalStateException("After update there must be a p-ModeSet with this id"));
//    }

    @Transactional(readOnly = false)
    public void updateActivePModeSetDescription(DC5PmodeService.DomibusConnectorPModeSet pModes) {
//        DomibusConnectorBusinessDomain.BusinessDomainId laneId = pModes.getBusinessDomainId();
//        if (laneId == null) {
//            laneId = DomibusConnectorBusinessDomain.getDefaultBusinessDomainId();
//            LOGGER.info("Setting default lane [{}] pModeSet", laneId);
//            pModes.setBusinessDomainId(laneId);
//        }
//        this.pModeService.updateActivePModeSetDescription(pModes);
    }

    @Transactional(readOnly = false)
    public void updateConnectorstorePassword(DC5PmodeService.DomibusConnectorPModeSet pModes, String newConnectorstorePwd) {

        this.keystorePersistenceService.updateKeystorePassword(pModes.getConnectorstore(), newConnectorstorePwd);
    }

    public Optional<DC5PmodeService.DomibusConnectorPModeSet> getCurrentPModeSet(DC5BusinessDomain.BusinessDomainId laneId) {
        return this.pModeService.getCurrentPModeSet(laneId);
    }


    @Deprecated
    public List<DC5PmodeService.DomibusConnectorPModeSet> getInactivePModeSets() {
//        final DomibusConnectorBusinessDomain.BusinessDomainId laneId = DomibusConnectorBusinessDomain.getDefaultBusinessDomainId();
//
//        return this.pModeService.getInactivePModeSets(laneId);
        return new ArrayList<>();
    }

    private DC5PmodeService.DomibusConnectorPModeSet getCurrentPModeSetOrNewSet() {
        final DC5BusinessDomain.BusinessDomainId laneId = DC5BusinessDomain.getDefaultBusinessDomainId();
        Optional<DC5PmodeService.DomibusConnectorPModeSet> currentPModeSetOptional = this.pModeService.getCurrentPModeSet(laneId);
        return currentPModeSetOptional.orElseGet(() -> {
            DC5PmodeService.DomibusConnectorPModeSet set = new DC5PmodeService.DomibusConnectorPModeSet();
            set.setBusinessDomainId(laneId);
            return set;
        });
    }

//    public DomibusConnectorKeystore getConnectorstore(String connectorstoreUUID) {
//        return keystorePersistenceService.getKeystoreByUUID(connectorstoreUUID);
//    }

}
