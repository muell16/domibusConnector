package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorTransportStep;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.dao.CommonPersistenceTest;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkPartnerDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorTransportStep;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.TransportStepPersistenceService;
import org.junit.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@CommonPersistenceTest
class TransportStepPersistenceServiceImplITCase {

    @Autowired
    DataSource ds;

    @Autowired
    TransportStepPersistenceService transportStepPersistenceService;

    @Autowired
    DomibusConnectorMessagePersistenceService messagePersistenceService;

    @Autowired
    DomibusConnectorMessageDao msgDao;

    @Before
    public void beforeAllTests() {
        DomibusConnectorMessage message = DomainEntityCreator.createMessage();
        message.setConnectorMessageId("msg2");

        messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);

//        PDomibusConnectorMessage msg = new PDomibusConnectorMessage();
//        msg.setConnectorMessageId("msg2");
//        msgDao.save(msg);

    }


    @Test
    void createNewTransportStep() {
        PDomibusConnectorMessage msg = new PDomibusConnectorMessage();
        msg.setConnectorMessageId("msg2");
        msg.setDirectionSource(MessageTargetSource.GATEWAY);
        msg.setDirectionTarget(MessageTargetSource.BACKEND);
        msgDao.save(msg);


        DomibusConnectorTransportStep step = new DomibusConnectorTransportStep();

        step.setMessageId(new DomibusConnectorMessage.DomibusConnectorMessageId("msg2"));
        step.setTransportId(new TransportStatusService.TransportId("msg2_link2_1"));
        step.setAttempt(1);
        step.setLinkPartnerName(new DomibusConnectorLinkPartner.LinkPartnerName("link2"));

        transportStepPersistenceService.createNewTransportStep(step);
    }

    @Test
    @Disabled("not implemented yet!")
    void getTransportStepByTransportId() {
    }

    @Test
    @Disabled("not implemented yet!")
    void update() {
    }
}