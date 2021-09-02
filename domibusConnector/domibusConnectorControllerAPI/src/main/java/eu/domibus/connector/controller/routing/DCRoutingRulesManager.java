package eu.domibus.connector.controller.routing;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;

import java.util.Collection;

public interface DCRoutingRulesManager {
    void addBackendRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, RoutingRule routingRule);

    Collection<RoutingRule> getBackendRoutingRules(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId);

    String getDefaultBackendName(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId);

    boolean isBackendRoutingEnabled(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId);
}