package eu.domibus.connector.common.service;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DCBusinessDomainManager {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class DomainValidResult {

        @Singular
        List<Object> warnings = new ArrayList<>();

        @Singular
        List<Object> errors = new ArrayList<>();;
    }

    public static final String BUSINESS_DOMAIN_PROPERTY_PREFIX = "connector.businessDomain";

    DomainValidResult validateDomain(DomibusConnectorBusinessDomain.BusinessDomainId id);

    List<DomibusConnectorBusinessDomain.BusinessDomainId> getAllBusinessDomains();

    public List<DomibusConnectorBusinessDomain.BusinessDomainId> getValidBusinessDomains();

    Optional<DomibusConnectorBusinessDomain> getBusinessDomain(DomibusConnectorBusinessDomain.BusinessDomainId id);

    void updateConfig(DomibusConnectorBusinessDomain.BusinessDomainId id, Map<String, String> properties);

    void createBusinessDomain(DomibusConnectorBusinessDomain businessDomain);

}
