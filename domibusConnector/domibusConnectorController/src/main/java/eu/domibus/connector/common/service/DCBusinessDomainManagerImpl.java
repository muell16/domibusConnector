package eu.domibus.connector.common.service;

import eu.domibus.connector.controller.spring.ConnectorConfigurationProperties;
import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageLaneDao;
import org.springframework.stereotype.Service;

import java.util.List;
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

        List<DomibusConnectorMessageLane.MessageLaneId> collect = businessDomainConfigurationProperties.getBusinessDomain().entrySet().stream()
                .filter(entry -> entry.getValue().isEnabled())
                .map(entry -> new DomibusConnectorMessageLane.MessageLaneId(entry.getKey()))
                .collect(Collectors.toList());
        collect.add(defaultBusinessDomainId);

        return collect;
    }

}
