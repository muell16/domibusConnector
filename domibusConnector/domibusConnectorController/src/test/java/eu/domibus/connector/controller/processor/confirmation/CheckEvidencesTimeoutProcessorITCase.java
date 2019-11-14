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

//@ITCaseTestAnnotation
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes={ITCaseTestContext.class})
@TestPropertySource("classpath:application-test.properties")
@Commit
@ActiveProfiles({"ITCaseTestContext", "storage-db"})
@Sql(scripts = {"/testdata.sql"}) //, "/testdata-confirmationtimeoutprocessor.sql"}) //adds testdata to database like domibus-blue party + message check timeout
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CheckEvidencesTimeoutProcessorITCase {

    @Autowired
    CheckEvidencesTimeoutProcessor checkEvidencesTimeoutProcessor;

    @Autowired
    DomibusConnectorMessagePersistenceService messagePersistenceService;

    @Autowired
    DomibusConnectorMessageDao messageDao;

//    @Autowired
//    TransactionManager txManager;

    @Autowired
    TransactionTemplate txTemplate;

//
//    @Before
//    public void beforeTest() {
//        txTemplate.execute((TransactionCallback) status -> {
//            DomibusConnectorMessage domibusConnectorMessage = null;
//            try {
//
//                return null;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        });
//    }

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Test
//    @Sql(statements = {""})
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
