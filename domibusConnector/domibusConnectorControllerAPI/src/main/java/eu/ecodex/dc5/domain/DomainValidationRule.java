package eu.ecodex.dc5.domain;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;

public interface DomainValidationRule {
    DCBusinessDomainManager.DomainValidResult validate(DomibusConnectorBusinessDomain.BusinessDomainId id);
}
