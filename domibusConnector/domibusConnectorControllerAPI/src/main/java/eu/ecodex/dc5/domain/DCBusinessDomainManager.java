package eu.ecodex.dc5.domain;

import eu.domibus.connector.domain.model.DC5BusinessDomain;
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

    Map<DC5BusinessDomain, DomainValidResult> getAllBusinessDomainsValidations();

    DomainValidResult validateDomain(DC5BusinessDomain.BusinessDomainId id);
    DomainValidResult validateDomain(DC5BusinessDomain domain);

    /**
     *
     * @return a list of all domains with the minimum length of 1
     *  The default domain is always included in this List!
     */
    List<DC5BusinessDomain.BusinessDomainId> getAllBusinessDomains();
    List<DC5BusinessDomain> getAllBusinessDomainsAllData();

    public List<DC5BusinessDomain.BusinessDomainId> getValidBusinessDomains();
    public List<DC5BusinessDomain> getValidBusinessDomainsAllData();

    Optional<DC5BusinessDomain> getBusinessDomain(DC5BusinessDomain.BusinessDomainId id);

    void updateDomain(DC5BusinessDomain domain);

    void updateConfig(DC5BusinessDomain.BusinessDomainId id, Map<String, String> properties);

    void createBusinessDomain(DC5BusinessDomain businessDomain);

}
