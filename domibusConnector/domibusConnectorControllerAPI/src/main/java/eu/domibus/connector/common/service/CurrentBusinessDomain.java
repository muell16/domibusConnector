package eu.domibus.connector.common.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;

public class CurrentBusinessDomain {

    private static final ThreadLocal<DomibusConnectorMessageLane.MessageLaneId> currentMessageLaneId = new ThreadLocal<>();

    public static DomibusConnectorMessageLane.MessageLaneId getCurrentBusinessDomain() {
        return currentMessageLaneId.get();
    }

    public static void setCurrentBusinessDomain(DomibusConnectorMessageLane.MessageLaneId messageLaneId) {
        currentMessageLaneId.set(messageLaneId);
    }

}
