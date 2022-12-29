package eu.domibus.connector.persistence.service;

import eu.domibus.connector.controller.routing.LinkPartnerRoutingRule;
import eu.domibus.connector.domain.model.DC5BusinessDomain;

import java.util.List;

public interface DCRoutingRulePersistenceService {

    public void createRoutingRule(DC5BusinessDomain.BusinessDomainId businessDomainId, LinkPartnerRoutingRule rr);

    public void deleteRoutingRule(DC5BusinessDomain.BusinessDomainId businessDomainId, String ruleId);

    public void updateRoutingRule(DC5BusinessDomain.BusinessDomainId businessDomainId, LinkPartnerRoutingRule ruleId);

    public List<LinkPartnerRoutingRule> getAllRoutingRules(DC5BusinessDomain.BusinessDomainId businessDomainId);

    public void setDefaultBackendName(DC5BusinessDomain.BusinessDomainId businessDomainId, String backendName);
}
