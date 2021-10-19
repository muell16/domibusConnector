package eu.domibus.connector.controller.routing;

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

    private Map<DomibusConnectorBusinessDomain.BusinessDomainId, RoutingConfig> backendRoutingConfig = new HashMap<>();

    private final DCMessageRoutingConfigurationProperties routingConfigurationProperties;

    public DCRoutingRulesManagerImpl(DCMessageRoutingConfigurationProperties routingConfigurationProperties) {
        this.routingConfigurationProperties = routingConfigurationProperties;
    }

    @Override
    public synchronized void addBackendRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, RoutingRule routingRule) {
        RoutingConfig routingConfig = getMessageRoutingConfigurationProperties(businessDomainId);
        routingConfig.routingRules.add(routingRule);
    }

    @Override
    public Collection<RoutingRule> getBackendRoutingRules(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId) {
        RoutingConfig dcMessageRoutingConfigurationProperties = getMessageRoutingConfigurationProperties(businessDomainId);
        return Collections.unmodifiableCollection(dcMessageRoutingConfigurationProperties.routingRules);
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
        try {
            CurrentBusinessDomain.setCurrentBusinessDomain(businessDomainId);

            RoutingConfig routingConfig = backendRoutingConfig.get(businessDomainId);
            if (routingConfig == null) {
                routingConfig = new RoutingConfig();

                routingConfig.defaultLinkPartner = new DomibusConnectorLinkPartner.LinkPartnerName(routingConfigurationProperties.getDefaultBackendName());
                routingConfig.routingEnabled = routingConfigurationProperties.isEnabled();

                routingConfig.routingRules.addAll(routingConfigurationProperties.getBackendRules());
                backendRoutingConfig.put(businessDomainId, routingConfig);

            }
            return routingConfig;
        } finally {
            CurrentBusinessDomain.setCurrentBusinessDomain(null);
        }
    }

    private static class RoutingConfig {
        private SortedSet<RoutingRule> routingRules = new TreeSet<>(Comparator.comparingInt(RoutingRule::getPriority));
        private DomibusConnectorLinkPartner.LinkPartnerName defaultLinkPartner;
        private boolean routingEnabled;
    }

}
