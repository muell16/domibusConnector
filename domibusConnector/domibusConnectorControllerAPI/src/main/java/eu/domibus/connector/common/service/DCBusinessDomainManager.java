package eu.domibus.connector.common.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;

import java.util.List;

public interface DCBusinessDomainManager {

    public List<DomibusConnectorMessageLane.MessageLaneId> getActiveBusinessDomainIds();

}
