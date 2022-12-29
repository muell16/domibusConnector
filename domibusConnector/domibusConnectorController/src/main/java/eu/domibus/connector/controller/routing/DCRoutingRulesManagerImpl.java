package eu.domibus.connector.controller.routing;

import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.domibus.connector.persistence.service.DCRoutingRulePersistenceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Holds all Routing Rules in Memory for
 * the different UseCaseDomains
 */
@Service
public class DCRoutingRulesManagerImpl implements DCRoutingRulesManager {

    private static final Logger LOGGER = LogManager.getLogger(DCRoutingRulesManagerImpl.class);
    /**
     * holds all routing rules which have been added dynamically
     */
    private Map<DC5BusinessDomain.BusinessDomainId, RoutingConfig> routingRuleMap = new HashMap<>();

    private final ConfigurationPropertyManagerService propertyManager;
    private final DCRoutingRulePersistenceService dcRoutingRulePersistenceService;

    public DCRoutingRulesManagerImpl(ConfigurationPropertyManagerService propertyManager,
                                     DCRoutingRulePersistenceService dcRoutingRulePersistenceService) {
        this.propertyManager = propertyManager;
        this.dcRoutingRulePersistenceService = dcRoutingRulePersistenceService;
    }

    @Override
    public synchronized void addBackendRoutingRule(DC5BusinessDomain.BusinessDomainId businessDomainId, LinkPartnerRoutingRule linkPartnerRoutingRule) {
        RoutingConfig rc = routingRuleMap.get(businessDomainId);
        if (rc == null) {
            rc = new RoutingConfig();
            routingRuleMap.put(businessDomainId, rc);
        }

        rc.backendRoutingRules.put(linkPartnerRoutingRule.getRoutingRuleId(), linkPartnerRoutingRule);
        LOGGER.debug("Added routing rule [{}]", linkPartnerRoutingRule);
    }


    @Override
    public LinkPartnerRoutingRule persistBackendRoutingRule(DC5BusinessDomain.BusinessDomainId businessDomainId, LinkPartnerRoutingRule linkPartnerRoutingRule) {
        dcRoutingRulePersistenceService.createRoutingRule(businessDomainId, linkPartnerRoutingRule);
        addBackendRoutingRule(businessDomainId, linkPartnerRoutingRule);
        linkPartnerRoutingRule.setConfigurationSource(ConfigurationSource.DB);
        return linkPartnerRoutingRule;
    }

    @Override
    public void deleteBackendRoutingRuleFromPersistence(DC5BusinessDomain.BusinessDomainId businessDomainId, String routingRuleId) {
        dcRoutingRulePersistenceService.deleteRoutingRule(businessDomainId, routingRuleId);
        deleteBackendRoutingRule(businessDomainId, routingRuleId);
    }

    @Override
    public synchronized void deleteBackendRoutingRule(DC5BusinessDomain.BusinessDomainId businessDomainId, String routingRuleId) {
        RoutingConfig rc = routingRuleMap.get(businessDomainId);
        if (rc == null) {
            rc = new RoutingConfig();
            routingRuleMap.put(businessDomainId, rc);
        }

        LinkPartnerRoutingRule remove = rc.backendRoutingRules.remove(routingRuleId);
        LOGGER.debug("Removed routing rule [{}]", remove);
    }


    @Override
    public Map<String, LinkPartnerRoutingRule> getBackendRoutingRules(DC5BusinessDomain.BusinessDomainId businessDomainId) {
        RoutingConfig dcMessageRoutingConfigurationProperties = getMessageRoutingConfigurationProperties(businessDomainId);
        return dcMessageRoutingConfigurationProperties.backendRoutingRules;
    }

    @Override
    public String getDefaultBackendName(DC5BusinessDomain.BusinessDomainId businessDomainId) {
        return getMessageRoutingConfigurationProperties(businessDomainId).defaultBackendLink.getLinkName();
    }

    @Override
    public void setDefaultBackendName(DC5BusinessDomain.BusinessDomainId businessDomainId, String backendName) {
        dcRoutingRulePersistenceService.setDefaultBackendName(businessDomainId, backendName);
    }

    @Override
    public boolean isBackendRoutingEnabled(DC5BusinessDomain.BusinessDomainId businessDomainId) {
        return true; //always true
    }

    private synchronized RoutingConfig getMessageRoutingConfigurationProperties(DC5BusinessDomain.BusinessDomainId businessDomainId) {
        DCMessageRoutingConfigurationProperties routProps = propertyManager.loadConfiguration(businessDomainId, DCMessageRoutingConfigurationProperties.class);

        RoutingConfig routingConfig = new RoutingConfig();

        routingConfig.defaultBackendLink = new DomibusConnectorLinkPartner.LinkPartnerName(routProps.getDefaultBackendName());
        routingConfig.routingEnabled = true;

        routingConfig.backendRoutingRules.putAll(routProps.getBackendRules());

        RoutingConfig rc = routingRuleMap.getOrDefault(businessDomainId, new RoutingConfig());
        routingConfig.backendRoutingRules.putAll(rc.backendRoutingRules);

        //load db rules, and override existing rules..
        List<LinkPartnerRoutingRule> allLinkPartnerRoutingRules = dcRoutingRulePersistenceService.getAllRoutingRules(businessDomainId);
        Map<String, LinkPartnerRoutingRule> collect = allLinkPartnerRoutingRules.stream()
                .peek(r -> r.setConfigurationSource(ConfigurationSource.DB))
                .collect(Collectors.toMap(LinkPartnerRoutingRule::getRoutingRuleId, Function.identity()));
        routingConfig.backendRoutingRules.putAll(collect);

        return routingConfig;

    }

    private static class RoutingConfig {
        private Map<String, LinkPartnerRoutingRule> backendRoutingRules = new HashMap<>();
        private DomibusConnectorLinkPartner.LinkPartnerName defaultBackendLink;
        private boolean routingEnabled;
    }

}
