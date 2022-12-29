package eu.domibus.connector.controller.routing;

import eu.domibus.connector.domain.model.DC5BusinessDomain;

import java.util.Map;

public interface DCRoutingRulesManager {
    void addBackendRoutingRule(DC5BusinessDomain.BusinessDomainId businessDomainId, LinkPartnerRoutingRule linkPartnerRoutingRule);

    LinkPartnerRoutingRule persistBackendRoutingRule(DC5BusinessDomain.BusinessDomainId businessDomainId, LinkPartnerRoutingRule linkPartnerRoutingRule);

    void deleteBackendRoutingRuleFromPersistence(DC5BusinessDomain.BusinessDomainId businessDomainId, String routingRuleId);

    void deleteBackendRoutingRule(DC5BusinessDomain.BusinessDomainId businessDomainId, String routingRuleId);

    Map<String, LinkPartnerRoutingRule> getBackendRoutingRules(DC5BusinessDomain.BusinessDomainId businessDomainId);

    String getDefaultBackendName(DC5BusinessDomain.BusinessDomainId businessDomainId);

    void setDefaultBackendName(DC5BusinessDomain.BusinessDomainId businessDomainId, String backendName);

    boolean isBackendRoutingEnabled(DC5BusinessDomain.BusinessDomainId businessDomainId);
}
