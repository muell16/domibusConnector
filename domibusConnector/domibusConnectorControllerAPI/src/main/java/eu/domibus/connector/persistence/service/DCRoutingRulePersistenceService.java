package eu.domibus.connector.persistence.service;

import eu.domibus.connector.controller.routing.LinkPartnerRoutingRule;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;

import java.util.List;

public interface DCRoutingRulePersistenceService {

    public void createRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, LinkPartnerRoutingRule rr);

    public void deleteRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, String ruleId);

    public void updateRoutingRule(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, LinkPartnerRoutingRule ruleId);

    public List<LinkPartnerRoutingRule> getAllRoutingRules(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId);

    public void setDefaultBackendName(DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId, String backendName);
}
