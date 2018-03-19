package eu.domibus.connector.persistence.service.impl.helper;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.model.enums.MessageDirection;
import sun.plugin2.message.Message;

import javax.annotation.Nonnull;

public class MessageDirectionMapper {

    public static @Nonnull MessageDirection mapFromDomainToPersistence(@Nonnull DomibusConnectorMessageDirection direction) {
        return MessageDirection.valueOf(direction.name());
    }

    public static  @Nonnull DomibusConnectorMessageDirection mapFromPersistenceToDomain(@Nonnull MessageDirection direction) {
        return DomibusConnectorMessageDirection.valueOf(direction.name());
    }

}
