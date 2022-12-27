package eu.ecodex.dc5.domain;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import lombok.*;
import org.springframework.core.style.ToStringCreator;

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
        List<Object> errors = new ArrayList<>();

        public boolean isValid() {
            return errors.isEmpty();
        }

        public String toString() {
            return new ToStringCreator(this)
                    .append("containsErrors", !errors.isEmpty())
                    .append(errors)
                    .toString();
        }
    }

    public static final String BUSINESS_DOMAIN_PROPERTY_PREFIX = "connector.businessDomain";

    Map<DomibusConnectorBusinessDomain, DomainValidResult> getAllBusinessDomainsValidations();

    DomainValidResult validateDomain(DomibusConnectorBusinessDomain.BusinessDomainId id);
    DomainValidResult validateDomain(DomibusConnectorBusinessDomain domain);

    /**
     *
     * @return a list of all domains with the minimum length of 1
     *  The default domain is always included in this List!
     */
    List<DomibusConnectorBusinessDomain.BusinessDomainId> getAllBusinessDomains();
    List<DomibusConnectorBusinessDomain> getAllBusinessDomainsAllData();

    public List<DomibusConnectorBusinessDomain.BusinessDomainId> getValidBusinessDomains();
    public List<DomibusConnectorBusinessDomain> getValidBusinessDomainsAllData();

    Optional<DomibusConnectorBusinessDomain> getBusinessDomain(DomibusConnectorBusinessDomain.BusinessDomainId id);

    void updateDomain(DomibusConnectorBusinessDomain domain);

    void updateConfig(DomibusConnectorBusinessDomain.BusinessDomainId id, Map<String, String> properties);

    void createBusinessDomain(DomibusConnectorBusinessDomain businessDomain);

}
