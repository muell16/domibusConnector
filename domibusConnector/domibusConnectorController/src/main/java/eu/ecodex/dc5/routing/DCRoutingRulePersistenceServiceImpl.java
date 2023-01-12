package eu.ecodex.dc5.routing;


import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.domibus.connector.controller.routing.LinkPartnerRoutingRule;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.domibus.connector.persistence.service.DCRoutingRulePersistenceService;
import eu.domibus.connector.common.service.BeanToPropertyMapConverter;
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
public class DCRoutingRulePersistenceServiceImpl implements DCRoutingRulePersistenceService {

    private static final Logger LOGGER = LogManager.getLogger(DCRoutingRulePersistenceServiceImpl.class);
    //TODO: avoid this by using DCMessageRoutingConfigurationProperties
    private static final String DEFAULT_BACKEND_NAME_PROPERTY_NAME = "connector.routing.default-backend-name";

    private final ConfigurationPropertyManagerService configurationPropertyManagerService;
//    private final DomibusConnectorBusinessDomainDao businessDomainDao;
    private final BeanToPropertyMapConverter beanToPropertyMapConverter;
    private final DCBusinessDomainManager domainManager;

//    public DCRoutingRulePersistenceServiceImpl(ConfigurationPropertyManagerService configurationPropertyManagerService,
//                                               DomibusConnectorBusinessDomainDao businessDomainDao,
//                                               BeanToPropertyMapConverter beanToPropertyMapConverter) {
//        this.configurationPropertyManagerService = configurationPropertyManagerService;
//        this.businessDomainDao = businessDomainDao;
//        this.beanToPropertyMapConverter = beanToPropertyMapConverter;
//    }


    @Override
    public void createRoutingRule(DC5BusinessDomain.BusinessDomainId businessDomainId, LinkPartnerRoutingRule rr) {
        rr.setConfigurationSource(ConfigurationSource.DB);
        DC5BusinessDomain businessDomain = getBusinessDomain(businessDomainId);
        Map<String, String> properties = businessDomain.getProperties();
        Map<String, String> stringStringMap = beanToPropertyMapConverter.readBeanPropertiesToMap(rr, "");
        stringStringMap.forEach((key, value) -> {
            properties.put("connector.routing.backend-rules[" + rr.getRoutingRuleId() + "]" + key, value);
        });
//        businessDomainDao.save(DC5BusinessDomainJpaEntity);

        domainManager.updateConfig(businessDomainId, properties);
    }

    private DC5BusinessDomain getBusinessDomain(DC5BusinessDomain.BusinessDomainId businessDomainId) {

        Optional<DC5BusinessDomain> byName = domainManager.getDomain(businessDomainId);

//        Optional<DC5BusinessDomainJpaEntity> byName = businessDomainDao.findByName(businessDomainId);
        if (byName.isPresent()) {
            return byName.get();
        } else {
            throw new IllegalArgumentException("The business domain with id [" + businessDomainId + "] does not exist in DB!");
        }
    }

    @Override
    public void deleteRoutingRule(DC5BusinessDomain.BusinessDomainId businessDomainId, String ruleId) {
        DC5BusinessDomain businessDomain = getBusinessDomain(businessDomainId);
        List<String> keys = getAllKeysForRoutingRule(ruleId, businessDomain);

        //remove all entries key belongs to routing rule with given ruleId
        keys.forEach(k -> businessDomain.getProperties().remove(k));

//        businessDomainDao.save(businessDomain);
        domainManager.updateConfig(businessDomainId, businessDomain.getProperties());
    }

    private List<String> getAllKeysForRoutingRule(String ruleId, DC5BusinessDomain messageLane) {
        String prefix = getRoutingRuleKey(ruleId);


        List<String> keys = messageLane.getProperties().keySet().stream()
                .filter(k -> k.startsWith(prefix))
                .collect(Collectors.toList());
        return keys;
    }

    @Override
    public void updateRoutingRule(DC5BusinessDomain.BusinessDomainId businessDomainId, LinkPartnerRoutingRule rr) {
        createRoutingRule(businessDomainId, rr);
    }

    @Override
    public List<LinkPartnerRoutingRule> getAllRoutingRules(DC5BusinessDomain.BusinessDomainId businessDomainId) {
        DC5BusinessDomain messageLane = getBusinessDomain(businessDomainId);
        Map<String, String> properties = messageLane.getProperties();
        Set<String> routingRuleIds = properties.keySet()
                .stream()
                .filter(k -> k.length() > PREFIX.length())
                .map(k -> k.substring(PREFIX.length()))
                .filter(k -> k.contains("]"))
                .map(k -> k.substring(0, k.indexOf("]")))
                .collect(Collectors.toSet());

        Set<LinkPartnerRoutingRule> linkPartnerRoutingRules = new HashSet<>();
        for (String ruleId : routingRuleIds) {
            String routingRuleKey = getRoutingRuleKey(ruleId);
            LinkPartnerRoutingRule linkPartnerRoutingRule = configurationPropertyManagerService.loadConfigurationOnlyFromMap(properties, LinkPartnerRoutingRule.class, routingRuleKey);
            linkPartnerRoutingRule.setConfigurationSource(ConfigurationSource.DB);
            linkPartnerRoutingRules.add(linkPartnerRoutingRule);
        }

        return new ArrayList<>(linkPartnerRoutingRules);
    }

    @Override
    public void setDefaultBackendName(DC5BusinessDomain.BusinessDomainId businessDomainId, String backendName) {
        DC5BusinessDomain DC5BusinessDomainJpaEntity = getBusinessDomain(businessDomainId);
        Map<String, String> properties = DC5BusinessDomainJpaEntity.getProperties();
        properties.put(DEFAULT_BACKEND_NAME_PROPERTY_NAME, backendName);
        //businessDomainDao.save(DC5BusinessDomainJpaEntity);

        domainManager.updateConfig(businessDomainId, properties);
    }

    public static final String PREFIX = "connector.routing.backend-rules[";

    private String getRoutingRuleKey(String ruleId) {
        //better us Constant here...
        return PREFIX + ruleId + "]";
    }
}
