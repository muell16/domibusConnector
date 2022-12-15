package eu.ecodex.dc5.routing;


import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.common.service.BeanToPropertyMapConverter;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.domibus.connector.controller.routing.DomainRoutingRule;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * This service uses the DC_MESSAGE_LANE and DC_MESSAGE_LANE_PROPERTY
 * tables to store the RoutingRules
 * This tables are also used by the PropertyLoading Services
 * TODO: Maybe this should be seperated into it's own table!
 *
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DCDomainRoutingRulePersistenceService {

    private static final Logger LOGGER = LogManager.getLogger(DCDomainRoutingRulePersistenceService.class);
    //TODO: avoid this by using DCMessageRoutingConfigurationProperties
    private static final String DEFAULT_BACKEND_NAME_PROPERTY_NAME = "connector.routing.default-backend-name";

    private final ConfigurationPropertyManagerService configurationPropertyManagerService;
//    private final DomibusConnectorBusinessDomainDao businessDomainDao;
    private final BeanToPropertyMapConverter beanToPropertyMapConverter;
    private final DCBusinessDomainManager domainManager;

    public static final String PREFIX = "connector.routing.domain-rules[";

    public void createRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, DomainRoutingRule rr) {
        rr.setConfigurationSource(ConfigurationSource.DB);
        DomibusConnectorBusinessDomain businessDomain = getBusinessDomain(businessDomainId);
        Map<String, String> properties = businessDomain.getProperties();
        Map<String, String> stringStringMap = beanToPropertyMapConverter.readBeanPropertiesToMap(rr, "");
        stringStringMap.forEach((key, value) -> {
            properties.put(PREFIX + rr.getRoutingRuleId() + "]" + key, value);
        });
//        businessDomainDao.save(DC5BusinessDomainJpaEntity);

        // TODO: return map before update.

        domainManager.updateConfig(businessDomainId, properties);
    }

    private DomibusConnectorBusinessDomain getBusinessDomain(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {

        Optional<DomibusConnectorBusinessDomain> byName = domainManager.getBusinessDomain(businessDomainId);

//        Optional<DC5BusinessDomainJpaEntity> byName = businessDomainDao.findByName(businessDomainId);
        if (byName.isPresent()) {
            return byName.get();
        } else {
            throw new IllegalArgumentException("The business domain with id [" + businessDomainId + "] does not exist in DB!");
        }
    }

    public void deleteRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, String ruleId) {
        DomibusConnectorBusinessDomain businessDomain = getBusinessDomain(businessDomainId);
        List<String> keys = getAllKeysForRoutingRule(ruleId, businessDomain);

        //remove all entries key belongs to routing rule with given ruleId
        keys.forEach(k -> businessDomain.getProperties().remove(k));

//        businessDomainDao.save(businessDomain);
        domainManager.updateConfig(businessDomainId, businessDomain.getProperties());
    }

    private List<String> getAllKeysForRoutingRule(String ruleId, DomibusConnectorBusinessDomain messageLane) {
        String prefix = getRoutingRuleKey(ruleId);


        List<String> keys = messageLane.getProperties().keySet().stream()
                .filter(k -> k.startsWith(prefix))
                .collect(Collectors.toList());
        return keys;
    }

    public void updateRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, DomainRoutingRule rr) {
        createRoutingRule(businessDomainId, rr);
    }

    public List<DomainRoutingRule> getAllRoutingRules(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        DomibusConnectorBusinessDomain messageLane = getBusinessDomain(businessDomainId);
        Map<String, String> properties = messageLane.getProperties();
        Set<String> routingRuleIds = properties.keySet()
                .stream()
                .filter(k -> k.length() > PREFIX.length())
                .map(k -> k.substring(PREFIX.length()))
                .filter(k -> k.contains("]"))
                .map(k -> k.substring(0, k.indexOf("]")))
                .collect(Collectors.toSet());

        Set<DomainRoutingRule> DomainRoutingRules = new HashSet<>();
        for (String ruleId : routingRuleIds) {
            String routingRuleKey = getRoutingRuleKey(ruleId);
            DomainRoutingRule DomainRoutingRule = configurationPropertyManagerService.loadConfigurationOnlyFromMap(properties, DomainRoutingRule.class, routingRuleKey);
            DomainRoutingRule.setConfigurationSource(ConfigurationSource.DB);
            DomainRoutingRules.add(DomainRoutingRule);
        }

        return new ArrayList<>(DomainRoutingRules);
    }

    public void setDefaultBackendName(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, String backendName) {
        DomibusConnectorBusinessDomain DC5BusinessDomainJpaEntity = getBusinessDomain(businessDomainId);
        Map<String, String> properties = DC5BusinessDomainJpaEntity.getProperties();
        properties.put(DEFAULT_BACKEND_NAME_PROPERTY_NAME, backendName);
        //businessDomainDao.save(DC5BusinessDomainJpaEntity);

        domainManager.updateConfig(businessDomainId, properties);
    }

    private String getRoutingRuleKey(String ruleId) {
        //better us Constant here...
        return PREFIX + ruleId + "]";
    }
}
