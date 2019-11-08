package eu.domibus.connector.persistence.service.impl.helper;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.persistence.model.enums.PMessageDirection;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class MessageDirectionMapper {

    public static @Nonnull
    PMessageDirection mapFromDomainToPersistence(@Nonnull DomibusConnectorMessageDirection direction) {
        switch (direction) {
            case BACKEND_TO_GATEWAY:
                return PMessageDirection.NAT_TO_GW;
            case GATEWAY_TO_BACKEND:
                return PMessageDirection.GW_TO_NAT;
            case CONNECTOR_TO_BACKEND:
                return PMessageDirection.CONN_TO_NAT;
            case CONNECTOR_TO_GATEWAY:
                return PMessageDirection.CONN_TO_GW;
            default:
                throw new IllegalArgumentException("Provided direction is invalid!");
        }
    }

    public static @Nonnull DomibusConnectorMessageDirection mapFromPersistenceToDomain(@Nonnull PMessageDirection direction) {
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

    public static DomibusConnectorMessageDirection map(PMessageDirection direction) {
        return mapFromPersistenceToDomain(direction);
    }

    public static PMessageDirection map(DomibusConnectorMessageDirection direction) {
        return mapFromDomainToPersistence(direction);
    }

    public static DomibusConnectorMessageDirection mapFromPersistenceToDomain(MessageTargetSource directionSource, MessageTargetSource directionTarget) {
        return DomibusConnectorMessageDirection.fromMessageTargetSource(directionSource, directionTarget);
    }
}
