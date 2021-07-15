package eu.domibus.connector.controller.routing;

import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;

import java.util.Collection;

public interface DCRoutingRulesManager {
    void addBackendRoutingRule(DomibusConnectorMessageLane.MessageLaneId messageLaneId, RoutingRule routingRule);

    Collection<RoutingRule> getBackendRoutingRules(DomibusConnectorMessageLane.MessageLaneId messageLaneId);

    String getDefaultBackendName(DomibusConnectorMessageLane.MessageLaneId messageLaneId);

    boolean isBackendRoutingEnabled(DomibusConnectorMessageLane.MessageLaneId messageLaneId);
}
