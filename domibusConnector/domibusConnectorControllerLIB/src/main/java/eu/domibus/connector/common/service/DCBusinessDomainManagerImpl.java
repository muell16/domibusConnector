package eu.domibus.connector.common.service;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.persistence.service.DCBusinessDomainPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DCBusinessDomainManagerImpl implements DCBusinessDomainManager {

//    private final ConnectorConfigurationProperties businessDomainConfigurationProperties;
    private final DCBusinessDomainPersistenceService businessDomainPersistenceService;

    public DCBusinessDomainManagerImpl(DCBusinessDomainPersistenceService businessDomainPersistenceService) {
//        this.businessDomainConfigurationProperties = businessDomainConfigurationProperties;
        this.businessDomainPersistenceService = businessDomainPersistenceService;
    }

    @Override
    public List<DomibusConnectorBusinessDomain.BusinessDomainId> getActiveBusinessDomainIds() {
//        DomibusConnectorMessageLane.MessageLaneId defaultBusinessDomainId = businessDomainConfigurationProperties.getDefaultBusinessDomainId();
//        List<DomibusConnectorMessageLane.MessageLaneId> collect = new ArrayList<>(businessDomainConfigurationProperties.getBusinessDomain()
//                .keySet());
//        ;
        List<DomibusConnectorBusinessDomain.BusinessDomainId> collect = businessDomainPersistenceService
                .findAll()
                .stream()
                .filter(DomibusConnectorBusinessDomain::isEnabled)
                .map(DomibusConnectorBusinessDomain::getId)
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public Optional<DomibusConnectorBusinessDomain> getBusinessDomain(DomibusConnectorBusinessDomain.BusinessDomainId id) {
        return businessDomainPersistenceService.findById(id);
    }

    @Override
    public void updateConfig(DomibusConnectorBusinessDomain.BusinessDomainId id, Map<String, String> properties) {
        Optional<DomibusConnectorBusinessDomain> byId = businessDomainPersistenceService.findById(id);
        if (byId.isPresent()) {
            DomibusConnectorBusinessDomain domibusConnectorBusinessDomain = byId.get();

            Map<String, String> updatedProperties = updateChangedProperties(domibusConnectorBusinessDomain.getMessageLaneProperties(), properties);
            domibusConnectorBusinessDomain.setMessageLaneProperties(updatedProperties);

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

//    private DomibusConnectorMessageLane mapBusinessConfigToBusinessDomain(Map.Entry<DomibusConnectorMessageLane.MessageLaneId, ConnectorConfigurationProperties.BusinessDomainConfig> messageLaneIdBusinessDomainConfigEntry) {
//        DomibusConnectorMessageLane lane = new DomibusConnectorMessageLane();
//        lane.setDescription(messageLaneIdBusinessDomainConfigEntry.getValue().getDescription());
//        lane.setId(messageLaneIdBusinessDomainConfigEntry.getKey());
//        Map<String, String> p = new HashMap<>(messageLaneIdBusinessDomainConfigEntry.getValue().getProperties());
//        lane.setMessageLaneProperties(p);
//        return lane;
//    }

}
