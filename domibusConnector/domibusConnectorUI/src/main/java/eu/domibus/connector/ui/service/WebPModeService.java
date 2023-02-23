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




    public List<DC5PmodeService.PModeParty> getPartyList(DC5BusinessDomain.BusinessDomainId laneId) {
        return getCurrentPModeSetOrNewSet(laneId).getParties();
    }

    public List<DC5PmodeService.PModeAction> getActionList(DC5BusinessDomain.BusinessDomainId laneId) {
        return getCurrentPModeSetOrNewSet(laneId).getActions();
    }


    public List<DC5PmodeService.PModeService> getServiceList(DC5BusinessDomain.BusinessDomainId laneId) {
        return getCurrentPModeSetOrNewSet(laneId).getServices();
    }


    public Optional<DC5PmodeService.DomibusConnectorPModeSet> getCurrentPModeSet(DC5BusinessDomain.BusinessDomainId laneId) {
        return this.pModeService.getCurrentPModeSet(laneId);
    }


    private DC5PmodeService.DomibusConnectorPModeSet getCurrentPModeSetOrNewSet(DC5BusinessDomain.BusinessDomainId domainId) {
        Optional<DC5PmodeService.DomibusConnectorPModeSet> currentPModeSetOptional = this.pModeService.getCurrentPModeSet(domainId);
        return currentPModeSetOptional.orElseGet(() -> {
            DC5PmodeService.DomibusConnectorPModeSet set = new DC5PmodeService.DomibusConnectorPModeSet();
            set.setBusinessDomainId(domainId);
            return set;
        });
    }

}
