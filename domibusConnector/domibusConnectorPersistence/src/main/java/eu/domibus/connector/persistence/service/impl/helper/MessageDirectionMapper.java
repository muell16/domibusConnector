package eu.domibus.connector.persistence.service.impl.helper;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.persistence.model.enums.MessageDirection;

import javax.annotation.Nonnull;

public class MessageDirectionMapper {

    public static @Nonnull MessageDirection mapFromDomainToPersistence(@Nonnull DomibusConnectorMessageDirection direction) {
        switch (direction) {
            case BACKEND_TO_GATEWAY:
                return MessageDirection.NAT_TO_GW;
            case GATEWAY_TO_BACKEND:
                return MessageDirection.GW_TO_NAT;
            case CONNECTOR_TO_BACKEND:
                return MessageDirection.CONN_TO_NAT;
            case CONNECTOR_TO_GATEWAY:
                return MessageDirection.CONN_TO_GW;
            default:
                throw new IllegalArgumentException("Provided direction is invalid!");
        }
    }

    public static @Nonnull DomibusConnectorMessageDirection mapFromPersistenceToDomain(@Nonnull MessageDirection direction) {
        if (direction == null) {
            throw new IllegalArgumentException("Cannot map null to a direction!");
        }
        switch (direction) {
            case NAT_TO_GW:
                return DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY;
            case GW_TO_NAT:
                return DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND;
            case CONN_TO_NAT:
                return DomibusConnectorMessageDirection.CONNECTOR_TO_BACKEND;
            case CONN_TO_GW:
                return DomibusConnectorMessageDirection.CONNECTOR_TO_GATEWAY;
            default:
                throw new IllegalArgumentException("Provided direction is invalid!");
        }
    }

}
