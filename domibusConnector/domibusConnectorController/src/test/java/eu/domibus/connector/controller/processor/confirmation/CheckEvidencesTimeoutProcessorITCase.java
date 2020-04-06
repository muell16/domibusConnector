package eu.domibus.connector.controller.processor.confirmation;

import eu.domibus.connector.controller.test.util.ITCaseTestContext;
import eu.domibus.connector.controller.test.util.LoadStoreMessageFromPath;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static eu.domibus.connector.persistence.spring.PersistenceProfiles.STORAGE_DB_PROFILE_NAME;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes={ITCaseTestContext.class})
@TestPropertySource("classpath:config/application-test.properties")
@Commit
@ActiveProfiles({"ITCaseTestContext", STORAGE_DB_PROFILE_NAME, "test"})
@Sql(scripts = {"/testdata.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CheckEvidencesTimeoutProcessorITCase {

    @Autowired
    CheckEvidencesTimeoutProcessor checkEvidencesTimeoutProcessor;

    @Autowired
    DomibusConnectorMessagePersistenceService messagePersistenceService;

    @Autowired
    DomibusConnectorMessageDao messageDao;

    @Autowired
    TransactionTemplate txTemplate;


    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Test
    public void testCheckEvidenceTimeout() throws IOException, ParseException {


        //create testdata:
        DomibusConnectorMessage domibusConnectorMessage = null;
        domibusConnectorMessage = LoadStoreMessageFromPath.loadMessageFrom(new ClassPathResource("/testmessages/msg2/"));
        domibusConnectorMessage.setConnectorMessageId("connectorid1");
        domibusConnectorMessage.getMessageDetails().setBackendMessageId("backendid1");
        messagePersistenceService.persistMessageIntoDatabase(domibusConnectorMessage, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
//        messagePersistenceService.setDeliveredToGateway(domibusConnectorMessage);
        messageDao.setMessageDeliveredToGateway("connectorid1", df.parse("2018-01-01 00:00"));


        //run evidence timeout checker
        checkEvidencesTimeoutProcessor.checkEvidencesTimeout();


        //TODO: check result...

    }


}
