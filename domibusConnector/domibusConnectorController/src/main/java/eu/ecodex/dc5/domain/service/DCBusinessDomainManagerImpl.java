package eu.ecodex.dc5.domain.service;

import eu.domibus.connector.common.configuration.ConnectorConfigurationProperties;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.domibus.connector.persistence.service.DCBusinessDomainPersistenceService;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.ecodex.dc5.domain.validation.DCDomainValidationService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DCBusinessDomainManagerImpl implements DCBusinessDomainManager {

    private static final Logger LOGGER = LogManager.getLogger(DCBusinessDomainManagerImpl.class);

    private final ConnectorConfigurationProperties businessDomainConfigurationProperties;
    private final DCBusinessDomainPersistenceService businessDomainPersistenceService;
    private final DCDomainValidationService domainValidationService;


    @Override
    public DomainValidResult validateDomain(DC5BusinessDomain.BusinessDomainId id) {
        final Optional<DC5BusinessDomain> businessDomain = this.getDomain(id);
        if (!businessDomain.isPresent()) {
            throw new IllegalArgumentException("For the provided business domain id [" + id + "] no domain exists");
        }
        return validateDomain(businessDomain.get());
    }

    @Override
    public DomainValidResult validateDomain(DC5BusinessDomain domain) {
        DomainValidResult result = domainValidationService.validateDomain(domain);
        LOGGER.debug("#validateDomain: Validate Domain [{}] returned result: [{}]", domain, result);
        return result;
    }


    @Override
    public List<DC5BusinessDomain.BusinessDomainId> getDomainIds() {
        Set<DC5BusinessDomain.BusinessDomainId> collect = new HashSet<>();
        if (businessDomainConfigurationProperties.isLoadBusinessDomainsFromDb()) {
            businessDomainPersistenceService
                    .findAll()
                    .stream()
                    .map(DC5BusinessDomain::getId)
                    .forEach(b -> {
                        LOGGER.debug("#getAllBusinessDomains: adding domain [{}] from DB", b);
                        collect.add(b);
                    });
        }

        businessDomainConfigurationProperties.getBusinessDomain()
                .entrySet().stream().map(this::mapDomainConfigToDomain)
                .map(DC5BusinessDomain::getId)
                .forEach(b -> {
                    if (!collect.add(b)) {
                        LOGGER.warn("Database has already provided a business domain with id [{}]. The domain will not be added from environment. DB takes precedence!", b);
                    } else {
                        LOGGER.debug("#getAllBusinessDomains: adding domain [{}] from ENV", b);
                    }
                });

        //TODO: make sure default BusinessDomain is always returned!

        return new ArrayList<>(collect);
    }

    @Override
    public List<DC5BusinessDomain.BusinessDomainId> getValidDomainIds() {
        return getValidDomains().stream()
                .map(DC5BusinessDomain::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<DC5BusinessDomain> getDomains() {
        return getDomainIds().stream()
                .map(this::getDomain)
                .map(o -> o.orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Map<DC5BusinessDomain, DomainValidResult> getDomainValidations() {
        return getDomains().stream()
                .collect(Collectors.toMap(Function.identity(), this::validateDomain));
    }

    @Override
    public List<DC5BusinessDomain> getValidDomains() {
        List<DC5BusinessDomain> collect = getDomainValidations().entrySet().stream()
                .filter(e -> e.getValue().isValid())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (collect.isEmpty()) {
            LOGGER.warn("#getValidBusinessDomainsAllData: returned no valid business domains! Check your configuration!");
        }
        return collect;
    }

    @Override
    public Optional<DC5BusinessDomain> getDomain(DC5BusinessDomain.BusinessDomainId id) {
        Optional<DC5BusinessDomain> db = Optional.empty();
        if (businessDomainConfigurationProperties.isLoadBusinessDomainsFromDb()) {
            db = businessDomainPersistenceService.findById(id);
        }
        if (!db.isPresent()) {
            db = businessDomainConfigurationProperties.getBusinessDomain()
                    .entrySet().stream().map(this::mapDomainConfigToDomain)
                    .filter(b -> b.getId().equals(id))
                    .findAny();
        }
        return db;
    }

    @Override
    public void updateDomain(DC5BusinessDomain domain) {
        businessDomainPersistenceService.findById(domain.getId())
                .filter(d -> d.getConfigurationSource().equals(ConfigurationSource.DB))
                .ifPresent(d -> {
                    final boolean alwaysAllowEnablingDomainsOrEditingDomainsThatAreNotTheDefaultDomain =
                            domain.isEnabled() || !DC5BusinessDomain.getDefaultBusinessDomain().getId().equals(domain.getId());
                    if (alwaysAllowEnablingDomainsOrEditingDomainsThatAreNotTheDefaultDomain) {
                        businessDomainPersistenceService.update(domain);
                    }
                });
    }

    @Override
    public void updateConfig(DC5BusinessDomain.BusinessDomainId id, Map<String, String> properties) {
        Optional<DC5BusinessDomain> byId = businessDomainPersistenceService.findById(id);
        if (byId.isPresent()) {
            DC5BusinessDomain DC5BusinessDomain = byId.get();
            if (DC5BusinessDomain.getConfigurationSource() != ConfigurationSource.DB) {
                LOGGER.warn("Cannot update other than DB source!");
                return;
            }

            Map<String, String> updatedProperties = updateChangedProperties(DC5BusinessDomain.getProperties(), properties);
            DC5BusinessDomain.setProperties(updatedProperties);

            businessDomainPersistenceService.update(DC5BusinessDomain);
        } else {
            throw new RuntimeException("no business domain found for update config!");
        }

    }

    @Override
    public void createDomain(DC5BusinessDomain businessDomain) {
        businessDomainPersistenceService.create(businessDomain);
    }

    Map<String, String> updateChangedProperties(Map<String, String> currentProperties, Map<String, String> properties) {
        currentProperties.putAll(properties);
        Map<String, String> collect = currentProperties.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return collect;
    }

    private DC5BusinessDomain mapDomainConfigToDomain(Map.Entry<DC5BusinessDomain.BusinessDomainId, ConnectorConfigurationProperties.BusinessDomainConfig> messageLaneIdBusinessDomainConfigEntry) {
        DC5BusinessDomain lane = new DC5BusinessDomain();
        lane.setDescription(messageLaneIdBusinessDomainConfigEntry.getValue().getDescription());
        lane.setId(messageLaneIdBusinessDomainConfigEntry.getKey());
        lane.setEnabled(messageLaneIdBusinessDomainConfigEntry.getValue().isEnabled());
        lane.setConfigurationSource(ConfigurationSource.ENV);
        Map<String, String> p = new HashMap<>(messageLaneIdBusinessDomainConfigEntry.getValue().getProperties());
        lane.setProperties(p);
        return lane;
    }


}
