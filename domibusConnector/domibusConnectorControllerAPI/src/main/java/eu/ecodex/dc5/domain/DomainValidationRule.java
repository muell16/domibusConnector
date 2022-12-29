package eu.ecodex.dc5.domain;

import eu.domibus.connector.domain.model.DC5BusinessDomain;

public interface DomainValidationRule {
    DCBusinessDomainManager.DomainValidResult validate(DC5BusinessDomain domain);
}
