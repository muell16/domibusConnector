package eu.domibus.connector.common.service;

import eu.domibus.connector.common.configuration.ConnectorConfigurationProperties;
import eu.domibus.connector.domain.enums.ConfigurationSource;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.persistence.service.DCBusinessDomainPersistenceService;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DCBusinessDomainManagerImpl implements DCBusinessDomainManager {

    private static final Logger LOGGER = LogManager.getLogger(DCBusinessDomainManagerImpl.class);

    private final ConnectorConfigurationProperties businessDomainConfigurationProperties;
    private final DCBusinessDomainPersistenceService businessDomainPersistenceService;
    private final DCDomainValidationService domainValidationService;


    @Override
    public DomainValidResult validateDomain(DomibusConnectorBusinessDomain.BusinessDomainId id) {
        final Optional<DomibusConnectorBusinessDomain> businessDomain = this.getBusinessDomain(id);
        if (!businessDomain.isPresent()) {
            throw new IllegalArgumentException("For the provided business domain id [" + id + "] no domain exists");
        }
        return validateDomain(businessDomain.get());
    }

    @Override
    public DomainValidResult validateDomain(DomibusConnectorBusinessDomain domain) {
        DomainValidResult result = domainValidationService.validateDomain(domain);
        LOGGER.debug("#validateDomain: Validate Domain [{}] returned result: [{}]", domain, result);
        return result;
    }


    @Override
    public List<DomibusConnectorBusinessDomain.BusinessDomainId> getAllBusinessDomains() {
        Set<DomibusConnectorBusinessDomain.BusinessDomainId> collect = new HashSet<>();
        if (businessDomainConfigurationProperties.isLoadBusinessDomainsFromDb()) {
            businessDomainPersistenceService
                    .findAll()
                    .stream()
                    .map(DomibusConnectorBusinessDomain::getId)
                    .forEach(b -> {
                        LOGGER.debug("#getAllBusinessDomains: adding domain [{}] from DB", b);
                        collect.add(b);
                    });
        }

        businessDomainConfigurationProperties.getBusinessDomain()
                .entrySet().stream().map(this::mapBusinessConfigToBusinessDomain)
                .map(DomibusConnectorBusinessDomain::getId)
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
    public List<DomibusConnectorBusinessDomain.BusinessDomainId> getValidBusinessDomains() {
        return getValidBusinessDomainsAllData().stream()
                .map(DomibusConnectorBusinessDomain::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<DomibusConnectorBusinessDomain> getAllBusinessDomainsAllData() {
        return getAllBusinessDomains().stream()
                .map(this::getBusinessDomain)
                .map(o -> o.orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<DomibusConnectorBusinessDomain> getValidBusinessDomainsAllData() {
        List<DomibusConnectorBusinessDomain> collect = getAllBusinessDomainsAllData().stream()
                .filter(domain -> this.validateDomain(domain).isValid())
                .collect(Collectors.toList());

        if (collect.isEmpty()) {
            LOGGER.warn("#getValidBusinessDomainsAllData: returned no valid business domains! Check your configuration!");
        }
        return collect;

//        Set<DomibusConnectorBusinessDomain> collect = new HashSet<>();
//        if (businessDomainConfigurationProperties.isLoadBusinessDomainsFromDb()) {
//            businessDomainPersistenceService
//                    .findAll()
//                    .stream()
//                    .filter(DomibusConnectorBusinessDomain::isEnabled)
//                    .filter(d -> this.validateDomain(d.getId()).isValid())
//                    .forEach(b -> collect.add(b));
//        }
//
//        businessDomainConfigurationProperties.getBusinessDomain()
//                .entrySet().stream().map(this::mapBusinessConfigToBusinessDomain)
//                .filter(d -> !this.validateDomain(d.getId()).isValid())
//                .forEach(b -> {
//                    if (!collect.add(b)) {
//                        LOGGER.warn("Database has already provided a business domain with id [{}]. The domain will not be added from environment. DB takes precedence!", b);
//                    }
//                });
//

//
//        return new ArrayList<>(collect);
    }

    @Override
    public Optional<DomibusConnectorBusinessDomain> getBusinessDomain(DomibusConnectorBusinessDomain.BusinessDomainId id) {
        Optional<DomibusConnectorBusinessDomain> db = Optional.empty();
        if (businessDomainConfigurationProperties.isLoadBusinessDomainsFromDb()) {
            db = businessDomainPersistenceService.findById(id);
        }
        if (!db.isPresent()) {
            db = businessDomainConfigurationProperties.getBusinessDomain()
                    .entrySet().stream().map(this::mapBusinessConfigToBusinessDomain)
                    .filter(b -> b.getId().equals(id))
                    .findAny();
        }
        return db;
    }

    @Override
    public void updateDomain(DomibusConnectorBusinessDomain domain) {
        businessDomainPersistenceService.findById(domain.getId())
                .filter(d -> d.getConfigurationSource().equals(ConfigurationSource.DB))
                .ifPresent(d -> {
                    final boolean alwaysAllowEnablingDomainsOrEditingDomainsThatAreNotTheDefaultDomain =
                            domain.isEnabled() || !DomibusConnectorBusinessDomain.getDefaultBusinessDomain().getId().equals(domain.getId());
                    if (alwaysAllowEnablingDomainsOrEditingDomainsThatAreNotTheDefaultDomain) {
                        businessDomainPersistenceService.update(domain);
                    }
                });
    }

    @Override
    public void updateConfig(DomibusConnectorBusinessDomain.BusinessDomainId id, Map<String, String> properties) {
        Optional<DomibusConnectorBusinessDomain> byId = businessDomainPersistenceService.findById(id);
        if (byId.isPresent()) {
            DomibusConnectorBusinessDomain domibusConnectorBusinessDomain = byId.get();
            if (domibusConnectorBusinessDomain.getConfigurationSource() != ConfigurationSource.DB) {
                LOGGER.warn("Cannot update other than DB source!");
                return;
            }

            Map<String, String> updatedProperties = updateChangedProperties(domibusConnectorBusinessDomain.getProperties(), properties);
            domibusConnectorBusinessDomain.setProperties(updatedProperties);

            businessDomainPersistenceService.update(domibusConnectorBusinessDomain);
        } else {
            throw new RuntimeException("no business domain found for update config!");
        }

    }

    @Override
    public void createBusinessDomain(DomibusConnectorBusinessDomain businessDomain) {
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

    private DomibusConnectorBusinessDomain mapBusinessConfigToBusinessDomain(Map.Entry<DomibusConnectorBusinessDomain.BusinessDomainId, ConnectorConfigurationProperties.BusinessDomainConfig> messageLaneIdBusinessDomainConfigEntry) {
        DomibusConnectorBusinessDomain lane = new DomibusConnectorBusinessDomain();
        lane.setDescription(messageLaneIdBusinessDomainConfigEntry.getValue().getDescription());
        lane.setId(messageLaneIdBusinessDomainConfigEntry.getKey());
        lane.setEnabled(messageLaneIdBusinessDomainConfigEntry.getValue().isEnabled());
        lane.setConfigurationSource(ConfigurationSource.ENV);
        Map<String, String> p = new HashMap<>(messageLaneIdBusinessDomainConfigEntry.getValue().getProperties());
        lane.setProperties(p);
        return lane;
    }

}
