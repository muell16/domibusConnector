package eu.domibus.connector.common.service;

import eu.domibus.connector.domain.configuration.ConnectorConfigurationProperties;
import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DCBusinessDomainManagerImpl implements DCBusinessDomainManager {

    private final ConnectorConfigurationProperties businessDomainConfigurationProperties;

    public DCBusinessDomainManagerImpl(ConnectorConfigurationProperties businessDomainConfigurationProperties) {
        this.businessDomainConfigurationProperties = businessDomainConfigurationProperties;
    }

    @Override
    public List<DomibusConnectorMessageLane.MessageLaneId> getActiveBusinessDomainIds() {
        DomibusConnectorMessageLane.MessageLaneId defaultBusinessDomainId = businessDomainConfigurationProperties.getDefaultBusinessDomainId();

        List<DomibusConnectorMessageLane.MessageLaneId> collect = new ArrayList<>(businessDomainConfigurationProperties.getBusinessDomain()
                .keySet());
        ;
        return collect;
    }

    public Optional<DomibusConnectorMessageLane> getBusinessDomain(DomibusConnectorMessageLane.MessageLaneId id) {
        return businessDomainConfigurationProperties.getBusinessDomain()
                .entrySet().stream().filter(e -> e.getKey().equals(id))
                .map(this::mapBusinessConfigToBusinessDomain)
                .findAny();
    }

    private DomibusConnectorMessageLane mapBusinessConfigToBusinessDomain(Map.Entry<DomibusConnectorMessageLane.MessageLaneId, ConnectorConfigurationProperties.BusinessDomainConfig> messageLaneIdBusinessDomainConfigEntry) {
        DomibusConnectorMessageLane lane = new DomibusConnectorMessageLane();
        lane.setDescription(messageLaneIdBusinessDomainConfigEntry.getValue().getDescription());
        lane.setId(messageLaneIdBusinessDomainConfigEntry.getKey());
        Properties p = new Properties();
        p.putAll(messageLaneIdBusinessDomainConfigEntry.getValue().getProperties());
        lane.setMessageLaneProperties(p);
        return lane;
    }

}
