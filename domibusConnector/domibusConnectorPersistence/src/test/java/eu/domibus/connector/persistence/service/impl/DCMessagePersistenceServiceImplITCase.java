package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.dao.CommonPersistenceTest;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@CommonPersistenceTest
public class DCMessagePersistenceServiceImplITCase {


    @Autowired
    DCMessagePersistenceService persistenceService;


    @Test
    public void testPersistLoadBusinessMessage() {
        DomibusConnectorMessage epoMessage = DomainEntityCreator.createEpoMessage();
        epoMessage.setConnectorMessageId(new DomibusConnectorMessage.DomibusConnectorMessageId("id1"));

        persistenceService.persistMessageIntoDatabase(epoMessage, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);


        DomibusConnectorMessage loadedBusinessMsg = persistenceService.findMessageByConnectorMessageId("id1");
        assertThat(loadedBusinessMsg).isNotNull();

    }

    @Test
    public void testPersistLoadEvidenceMessage() {
        DomibusConnectorMessage evidenceMsg = DomainEntityCreator.createEvidenceNonDeliveryMessage();
        evidenceMsg.getMessageDetails().setCausedBy("id1");

        evidenceMsg.setConnectorMessageId(new DomibusConnectorMessage.DomibusConnectorMessageId("ev1"));

        persistenceService.persistMessageIntoDatabase(evidenceMsg, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);

        DomibusConnectorMessage loadedEvidenceMsg = persistenceService.findMessageByConnectorMessageId("ev1");
        assertThat(loadedEvidenceMsg).isNotNull();

    }



}