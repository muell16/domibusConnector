package eu.domibus.connector.controller.routing;

import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.routing.DCDomainRoutingRulePersistenceService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DCDomainRoutingManager {


    private static final Logger LOGGER = LogManager.getLogger(DCDomainRoutingManager.class);
    /**
     * holds all routing rules which have been added dynamically
     */
    private Map<DomibusConnectorBusinessDomain.BusinessDomainId, DCDomainRoutingManager.RoutingConfig> routingRuleMap = new HashMap<>();
    private final ConfigurationPropertyManagerService propertyManager;
    private final DCDomainRoutingRulePersistenceService dcRoutingRulePersistenceService;

    public synchronized void addDomainRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, DomainRoutingRule DomainRoutingRule) {
        DCDomainRoutingManager.RoutingConfig rc = routingRuleMap.get(businessDomainId);
        if (rc == null) {
            rc = new DCDomainRoutingManager.RoutingConfig();
            routingRuleMap.put(businessDomainId, rc);
        }

        rc.domainRoutingRules.put(DomainRoutingRule.getRoutingRuleId(), DomainRoutingRule);
        LOGGER.debug("Added routing rule [{}]", DomainRoutingRule);
    }


    public DomainRoutingRule persistDomainRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, DomainRoutingRule DomainRoutingRule) {
        dcRoutingRulePersistenceService.createRoutingRule(businessDomainId, DomainRoutingRule);
        addDomainRoutingRule(businessDomainId, DomainRoutingRule);
        DomainRoutingRule.setConfigurationSource(ConfigurationSource.DB);
        return DomainRoutingRule;
    }

    public void deleteDomainRoutingRuleFromPersistence(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, String routingRuleId) {
        dcRoutingRulePersistenceService.deleteRoutingRule(businessDomainId, routingRuleId);
        deleteDomainRoutingRule(businessDomainId, routingRuleId);
    }

    public synchronized void deleteDomainRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, String routingRuleId) {
        DCDomainRoutingManager.RoutingConfig rc = routingRuleMap.get(businessDomainId);
        if (rc == null) {
            rc = new DCDomainRoutingManager.RoutingConfig();
            routingRuleMap.put(businessDomainId, rc);
        }

        DomainRoutingRule remove = rc.domainRoutingRules.remove(routingRuleId);
        LOGGER.debug("Removed routing rule [{}]", remove);
    }


    public Map<String, DomainRoutingRule> getDomainRoutingRules(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        DCDomainRoutingManager.RoutingConfig dcMessageRoutingConfigurationProperties = getMessageRoutingConfigurationProperties(businessDomainId);
        return dcMessageRoutingConfigurationProperties.domainRoutingRules;
    }

    public void setDefaultDomainName(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, String backendName) {
        dcRoutingRulePersistenceService.setDefaultBackendName(businessDomainId, backendName);
    }

    public boolean isDomainRoutingEnabled(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        return true; //always true
    }

    private synchronized DCDomainRoutingManager.RoutingConfig getMessageRoutingConfigurationProperties(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        DCMessageRoutingConfigurationProperties routProps = propertyManager.loadConfiguration(businessDomainId, DCMessageRoutingConfigurationProperties.class);

        DCDomainRoutingManager.RoutingConfig routingConfig = new DCDomainRoutingManager.RoutingConfig();

        routingConfig.routingEnabled = true;

        routingConfig.domainRoutingRules.putAll(routProps.getDomainRules());

        DCDomainRoutingManager.RoutingConfig rc = routingRuleMap.getOrDefault(businessDomainId, new DCDomainRoutingManager.RoutingConfig());
        routingConfig.domainRoutingRules.putAll(rc.domainRoutingRules);

        //load db rules, and override existing rules..
        List<DomainRoutingRule> allDomainRoutingRules = dcRoutingRulePersistenceService.getAllRoutingRules(businessDomainId);
        Map<String, DomainRoutingRule> collect = allDomainRoutingRules.stream()
                .peek(r -> r.setConfigurationSource(ConfigurationSource.DB))
                .collect(Collectors.toMap(DomainRoutingRule::getRoutingRuleId, Function.identity()));
        routingConfig.domainRoutingRules.putAll(collect);

        return routingConfig;

    }

    private static class RoutingConfig {
        private Map<String, DomainRoutingRule> domainRoutingRules = new HashMap<>();
        private boolean routingEnabled;
    }
}
