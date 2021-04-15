package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorTransportStep;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.dao.CommonPersistenceTest;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.TransportStepPersistenceService;
import org.junit.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@CommonPersistenceTest
class AquireLockExceptionIntegrationTest {

    @Autowired
    DataSource ds;

    @Autowired
    TransportStepPersistenceService transportStepPersistenceService;

    @Autowired
    DomibusConnectorMessagePersistenceService messagePersistenceService;

    @Autowired
    DomibusConnectorMessageDao msgDao;

    @Test
    public void testBulkMessage() {

        DomibusConnectorMessage message = DomainEntityCreator.createMessage();
        message.setConnectorMessageId("msg2");
        messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);

        IntStream.range(0, 30).parallel().forEach(i -> {
            DomibusConnectorMessage message2 = DomainEntityCreator.createMessage();
            message2.setConnectorMessageId("msg2" + i);
            messagePersistenceService.persistMessageIntoDatabase(message2, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
        });

    }
}