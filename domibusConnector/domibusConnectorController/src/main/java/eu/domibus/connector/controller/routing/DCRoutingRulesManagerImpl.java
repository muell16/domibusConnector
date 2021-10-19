package eu.domibus.connector.controller.routing;

import eu.domibus.connector.common.service.ConfigurationPropertyManagerService;
import eu.domibus.connector.common.service.CurrentBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Holds all Routing Rules in Memory for
 * the different UseCaseDomains
 *
 */
@Service
public class DCRoutingRulesManagerImpl implements DCRoutingRulesManager {

    private final ConfigurationPropertyManagerService propertyManager;

    public DCRoutingRulesManagerImpl(ConfigurationPropertyManagerService propertyManager) {
        this.propertyManager = propertyManager;
    }

    @Override
    public synchronized void addBackendRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, RoutingRule routingRule) {
        RoutingConfig routingConfig = getMessageRoutingConfigurationProperties(businessDomainId);
//        routingConfig.routingRules.a0dd(routingRule);
        //TODO!!
    }

    @Override
    public Map<String, RoutingRule> getBackendRoutingRules(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        RoutingConfig dcMessageRoutingConfigurationProperties = getMessageRoutingConfigurationProperties(businessDomainId);
//        return Collections.unmodifiableCollection(dcMessageRoutingConfigurationProperties.routingRules);
        return dcMessageRoutingConfigurationProperties.routingRules;
    }

    @Override
    public String getDefaultBackendName(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        return getMessageRoutingConfigurationProperties(businessDomainId).defaultLinkPartner.getLinkName();
    }

    @Override
    public boolean isBackendRoutingEnabled(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        return getMessageRoutingConfigurationProperties(businessDomainId).routingEnabled;
    }

    private synchronized RoutingConfig getMessageRoutingConfigurationProperties(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        DCMessageRoutingConfigurationProperties routProps = propertyManager.loadConfiguration(businessDomainId, DCMessageRoutingConfigurationProperties.class);

        RoutingConfig routingConfig = new RoutingConfig();

        routingConfig.defaultLinkPartner = new DomibusConnectorLinkPartner.LinkPartnerName(routProps.getDefaultBackendName());
        routingConfig.routingEnabled = true;

        routingConfig.routingRules.putAll(routProps.getBackendRules());

        return routingConfig;

    }

    private static class RoutingConfig {
        private Map<String, RoutingRule> routingRules = new HashMap<>();
        private DomibusConnectorLinkPartner.LinkPartnerName defaultLinkPartner;
        private boolean routingEnabled;
    }

}
