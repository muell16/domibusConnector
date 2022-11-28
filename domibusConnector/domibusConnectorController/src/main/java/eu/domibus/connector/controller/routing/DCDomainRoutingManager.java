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

    public synchronized void addBackendRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, DomainRoutingRule DomainRoutingRule) {
        DCDomainRoutingManager.RoutingConfig rc = routingRuleMap.get(businessDomainId);
        if (rc == null) {
            rc = new DCDomainRoutingManager.RoutingConfig();
            routingRuleMap.put(businessDomainId, rc);
        }

        rc.backendRoutingRules.put(DomainRoutingRule.getRoutingRuleId(), DomainRoutingRule);
        LOGGER.debug("Added routing rule [{}]", DomainRoutingRule);
    }


    public DomainRoutingRule persistBackendRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, DomainRoutingRule DomainRoutingRule) {
        dcRoutingRulePersistenceService.createRoutingRule(businessDomainId, DomainRoutingRule);
        addBackendRoutingRule(businessDomainId, DomainRoutingRule);
        DomainRoutingRule.setConfigurationSource(ConfigurationSource.DB);
        return DomainRoutingRule;
    }

    public void deleteBackendRoutingRuleFromPersistence(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, String routingRuleId) {
        dcRoutingRulePersistenceService.deleteRoutingRule(businessDomainId, routingRuleId);
        deleteBackendRoutingRule(businessDomainId, routingRuleId);
    }

    public synchronized void deleteBackendRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, String routingRuleId) {
        DCDomainRoutingManager.RoutingConfig rc = routingRuleMap.get(businessDomainId);
        if (rc == null) {
            rc = new DCDomainRoutingManager.RoutingConfig();
            routingRuleMap.put(businessDomainId, rc);
        }

        DomainRoutingRule remove = rc.backendRoutingRules.remove(routingRuleId);
        LOGGER.debug("Removed routing rule [{}]", remove);
    }


    public Map<String, DomainRoutingRule> getDomainRoutingRules(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        DCDomainRoutingManager.RoutingConfig dcMessageRoutingConfigurationProperties = getMessageRoutingConfigurationProperties(businessDomainId);
        return dcMessageRoutingConfigurationProperties.backendRoutingRules;
    }

    public void setDefaultBackendName(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, String backendName) {
        dcRoutingRulePersistenceService.setDefaultBackendName(businessDomainId, backendName);
    }

    public boolean isBackendRoutingEnabled(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        return true; //always true
    }

    private synchronized DCDomainRoutingManager.RoutingConfig getMessageRoutingConfigurationProperties(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        DCMessageRoutingConfigurationProperties routProps = propertyManager.loadConfiguration(businessDomainId, DCMessageRoutingConfigurationProperties.class);

        DCDomainRoutingManager.RoutingConfig routingConfig = new DCDomainRoutingManager.RoutingConfig();

        routingConfig.routingEnabled = true;

        routingConfig.backendRoutingRules.putAll(routProps.getDomainRules());

        DCDomainRoutingManager.RoutingConfig rc = routingRuleMap.getOrDefault(businessDomainId, new DCDomainRoutingManager.RoutingConfig());
        routingConfig.backendRoutingRules.putAll(rc.backendRoutingRules);

        //load db rules, and override existing rules..
        List<DomainRoutingRule> allDomainRoutingRules = dcRoutingRulePersistenceService.getAllRoutingRules(businessDomainId);
        Map<String, DomainRoutingRule> collect = allDomainRoutingRules.stream()
                .peek(r -> r.setConfigurationSource(ConfigurationSource.DB))
                .collect(Collectors.toMap(DomainRoutingRule::getRoutingRuleId, Function.identity()));
        routingConfig.backendRoutingRules.putAll(collect);

        return routingConfig;

    }

    private static class RoutingConfig {
        private Map<String, DomainRoutingRule> backendRoutingRules = new HashMap<>();
        private boolean routingEnabled;
    }
}
