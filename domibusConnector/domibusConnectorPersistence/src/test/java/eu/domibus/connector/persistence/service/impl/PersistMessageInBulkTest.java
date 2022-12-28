package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.persistence.dao.CommonPersistenceTest;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.service.TransportStepPersistenceService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

/**
 * Test the persistence of multiple messages
 * in parallel
 */
@CommonPersistenceTest
@Disabled
class PersistMessageInBulkTest {

    @Autowired
    DataSource ds;

    @Autowired
    TransportStepPersistenceService transportStepPersistenceService;


    @Autowired
    DomibusConnectorMessageDao msgDao;

    @Test
    @Disabled("is broken and does test a deprecated function")
    public void testBulkMessage() {

//        DC5Message message = DomainEntityCreator.createMessage();
//        message.setConnectorMessageId(new DomibusConnectorMessageId("msg2"));
//        messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
//
//        IntStream.range(0, 30).parallel().forEach(i -> {
//            DC5Message message2 = DomainEntityCreator.createMessage();
//            message2.setConnectorMessageId(new DomibusConnectorMessageId("msg2" + i));
//            messagePersistenceService.persistMessageIntoDatabase(message2, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
//        });

    }
}