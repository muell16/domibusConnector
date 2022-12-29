package eu.domibus.connector.controller.routing;

import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
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
    private Map<DC5BusinessDomain.BusinessDomainId, DomainRoutingProperties> routingRuleMap = new HashMap<>();
    private final ConfigurationPropertyManagerService propertyManager;
    private final DCDomainRoutingRulePersistenceService dcRoutingRulePersistenceService;

    public synchronized void addDomainRoutingRule(DC5BusinessDomain.BusinessDomainId businessDomainId, DomainRoutingRule DomainRoutingRule) {
        DomainRoutingProperties rc = routingRuleMap.get(businessDomainId);
        if (rc == null) {
            rc = new DomainRoutingProperties();
            routingRuleMap.put(businessDomainId, rc);
        }

        rc.domainRoutingRules.put(DomainRoutingRule.getRoutingRuleId(), DomainRoutingRule);
        LOGGER.debug("Added routing rule [{}]", DomainRoutingRule);
    }


//    public DomainRoutingRule persistDomainRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, DomainRoutingRule domainRoutingRule) {
//        // TODO: pass configuration class instead of domain ID
//        dcRoutingRulePersistenceService.createRoutingRule(businessDomainId, domainRoutingRule);
//        addDomainRoutingRule(businessDomainId, domainRoutingRule);
//        domainRoutingRule.setConfigurationSource(ConfigurationSource.DB);
//        return DomainRoutingRule;
//    }

    public void deleteDomainRoutingRuleFromPersistence(DC5BusinessDomain.BusinessDomainId businessDomainId, String routingRuleId) {
        dcRoutingRulePersistenceService.deleteRoutingRule(businessDomainId, routingRuleId);
        deleteDomainRoutingRule(businessDomainId, routingRuleId);
    }

    public synchronized void deleteDomainRoutingRule(DC5BusinessDomain.BusinessDomainId businessDomainId, String routingRuleId) {
        DomainRoutingProperties rc = routingRuleMap.get(businessDomainId);
        if (rc == null) {
            rc = new DomainRoutingProperties();
            routingRuleMap.put(businessDomainId, rc);
        }

        DomainRoutingRule remove = rc.domainRoutingRules.remove(routingRuleId);
        LOGGER.debug("Removed routing rule [{}]", remove);
    }


    public Map<String, DomainRoutingRule> getDomainRoutingRules(DC5BusinessDomain.BusinessDomainId businessDomainId) {
        DomainRoutingProperties dcMessageDomainRoutingConfigurationProperties = getMessageRoutingConfigurationProperties(businessDomainId);
        return dcMessageDomainRoutingConfigurationProperties.domainRoutingRules;
    }

    public void setDefaultDomainName(DC5BusinessDomain.BusinessDomainId businessDomainId, String backendName) {
        dcRoutingRulePersistenceService.setDefaultBackendName(businessDomainId, backendName);
    }

    public boolean isDomainRoutingEnabled(DC5BusinessDomain.BusinessDomainId businessDomainId) {
        return true; //always true
    }

    private synchronized DomainRoutingProperties getMessageRoutingConfigurationProperties(DC5BusinessDomain.BusinessDomainId businessDomainId) {
        DCMessageRoutingConfigurationProperties routProps = propertyManager.loadConfiguration(businessDomainId, DCMessageRoutingConfigurationProperties.class);

        DomainRoutingProperties domainRoutingProperties = new DomainRoutingProperties();

        domainRoutingProperties.routingEnabled = true;

        domainRoutingProperties.domainRoutingRules.putAll(routProps.getDomainRules());

        DomainRoutingProperties rc = routingRuleMap.getOrDefault(businessDomainId, new DomainRoutingProperties());
        domainRoutingProperties.domainRoutingRules.putAll(rc.domainRoutingRules);

        //load db rules, and override existing rules..
        List<DomainRoutingRule> allDomainRoutingRules = dcRoutingRulePersistenceService.getAllRoutingRules(businessDomainId);
        Map<String, DomainRoutingRule> collect = allDomainRoutingRules.stream()
                .peek(r -> r.setConfigurationSource(ConfigurationSource.DB))
                .collect(Collectors.toMap(DomainRoutingRule::getRoutingRuleId, Function.identity()));
        domainRoutingProperties.domainRoutingRules.putAll(collect);

        return domainRoutingProperties;

    }

    private static class DomainRoutingProperties {
        private Map<String, DomainRoutingRule> domainRoutingRules = new HashMap<>();
        private boolean routingEnabled;
    }
}
