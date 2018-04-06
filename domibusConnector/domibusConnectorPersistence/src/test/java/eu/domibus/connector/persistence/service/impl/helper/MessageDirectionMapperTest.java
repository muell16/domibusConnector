package eu.domibus.connector.persistence.service.impl.helper;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.model.enums.MessageDirection;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageDirectionMapperTest {


    @Test
    public void mapFromDomainToPersistence() throws Exception {
        for (DomibusConnectorMessageDirection direction : DomibusConnectorMessageDirection.values()) {
            MessageDirectionMapper.mapFromDomainToPersistence(direction);
        }
    }

    @Test
    public void mapFromPersistenceToDomain() throws Exception {
        for (MessageDirection direction : MessageDirection.values()) {
            DomibusConnectorMessageDirection.valueOf(direction.name());
        }
    }

}