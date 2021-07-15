package eu.domibus.connector.controller.routing;

import eu.domibus.connector.config.c2ctests.ConnectorTestConfigurationProperties;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Holds all Routing Rules in Memory for
 * the different UseCaseDomains
 *
 */
@Service
public class DCRoutingRulesManagerImpl implements DCRoutingRulesManager {

    private Map<DomibusConnectorMessageLane.MessageLaneId, RoutingConfig> backendRoutingConfig = new HashMap<>();

    private final DCMessageRoutingConfigurationProperties routingConfigurationProperties;

    public DCRoutingRulesManagerImpl(DCMessageRoutingConfigurationProperties routingConfigurationProperties) {
        this.routingConfigurationProperties = routingConfigurationProperties;
    }

    @Override
    public synchronized void addBackendRoutingRule(DomibusConnectorMessageLane.MessageLaneId messageLaneId, RoutingRule routingRule) {

        RoutingConfig routingConfig = getMessageRoutingConfigurationProperties(messageLaneId);
        routingConfig.routingRules.add(routingRule);

    }

    @Override
    public Collection<RoutingRule> getBackendRoutingRules(DomibusConnectorMessageLane.MessageLaneId messageLaneId) {
        RoutingConfig dcMessageRoutingConfigurationProperties = getMessageRoutingConfigurationProperties(messageLaneId);
        return Collections.unmodifiableCollection(dcMessageRoutingConfigurationProperties.routingRules);
    }

    @Override
    public String getDefaultBackendName(DomibusConnectorMessageLane.MessageLaneId messageLaneId) {
        return getMessageRoutingConfigurationProperties(messageLaneId).defaultLinkPartner.getLinkName();
    }

    @Override
    public boolean isBackendRoutingEnabled(DomibusConnectorMessageLane.MessageLaneId messageLaneId) {
        return getMessageRoutingConfigurationProperties(messageLaneId).routingEnabled;
    }

    private synchronized RoutingConfig getMessageRoutingConfigurationProperties(DomibusConnectorMessageLane.MessageLaneId messageLaneId) {
        RoutingConfig routingConfig = backendRoutingConfig.get(messageLaneId);
        if (routingConfig == null) {
            routingConfig = new RoutingConfig();

            routingConfig.defaultLinkPartner = new DomibusConnectorLinkPartner.LinkPartnerName(routingConfigurationProperties.getDefaultBackendName());
            routingConfig.routingEnabled = routingConfigurationProperties.isEnabled();

            routingConfig.routingRules.addAll(routingConfigurationProperties.getBackendRules());
            backendRoutingConfig.put(messageLaneId, routingConfig);

        }
        return routingConfig;
    }

    private class RoutingConfig {
        private SortedSet<RoutingRule> routingRules = new TreeSet<>(Comparator.comparingInt(RoutingRule::getPriority));
        private DomibusConnectorLinkPartner.LinkPartnerName defaultLinkPartner;
        private boolean routingEnabled;
    }

}
