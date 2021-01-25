package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.dao.CommonPersistenceTest;
import eu.domibus.connector.persistence.testutil.SetupPersistenceContext;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Properties;
import java.util.Set;

import static eu.domibus.connector.persistence.spring.PersistenceProfiles.STORAGE_DB_PROFILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;


@CommonPersistenceTest
public class BigDataJPAWithMessagePersistenceIntegrationITCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(BigDataJPAWithMessagePersistenceIntegrationITCase.class);

//    private static ConfigurableApplicationContext APPLICATION_CONTEXT;

    @Autowired
    private DCMessagePersistenceService messagePersistenceService;

//    @Autowired
//    private DomibusConnectorPersistAllBigDataOfMessageService bigDataPersistenceService;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private TransactionTemplate transactionTemplate;

    private IDatabaseConnection conn;

    @BeforeAll
    public static void beforeClass() {
        Properties props = SetupPersistenceContext.getDefaultProperties();
        //props.put("connector.persistence.big-data-impl-class","eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceJpaImpl");
        Set<String> profiles = SetupPersistenceContext.getDefaultProfiles();
        profiles.add(STORAGE_DB_PROFILE_NAME); //activate DB storage profile...
//        APPLICATION_CONTEXT = SetupPersistenceContext.startApplicationContext(props, profiles);
    }

//    @AfterAll
//    public static void afterClass() {
//        APPLICATION_CONTEXT.close();
//    }


    @BeforeEach
    public void setUp() throws SQLException {
//        this.applicationContext = APPLICATION_CONTEXT;
//        messagePersistenceService = applicationContext.getBean(DomibusConnectorMessagePersistenceService.class);
//        bigDataPersistenceService = applicationContext.getBean(DomibusConnectorPersistAllBigDataOfMessageService.class);
//        dataSource = applicationContext.getBean(DataSource.class);
//        transactionManager = applicationContext.getBean(PlatformTransactionManager.class);
        this.conn = new DatabaseDataSourceConnection(dataSource);
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    /**
     * test write a message with attachments / content into database
     * and ensure, that everything is written
     */
    @Test
    public void testPersistMessageWithBigFiles() throws SQLException, DataSetException {
        Assertions.assertTimeout(Duration.ofSeconds(20), () -> {
            LOGGER.info("run test testPersistMessageWithBigFiles");
            DomibusConnectorMessage message = DomainEntityCreator.createEpoMessage();
            message.setConnectorMessageId("msgid1");

            transactionTemplate.execute((TransactionStatus status) -> {
                        DomibusConnectorMessage domibusConnectorMessage = messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
                        //message is in db
//                        domibusConnectorMessage = bigDataPersistenceService.persistAllBigFilesFromMessage(domibusConnectorMessage);
                        assertThat(domibusConnectorMessage.getMessageContent().getDocument().getDocument().getStorageIdReference()).isNotNull();
                        return null;
                    }
            );

            //check database....
            ITable bigdata = conn.createQueryTable("BIGDATA", "SELECT * FROM DOMIBUS_CONNECTOR_BIGDATA");
            int rowCount = bigdata.getRowCount();

            assertThat(rowCount).isEqualTo(2); //

            byte[] documentContent = (byte[]) bigdata.getValue(0, "content");
            assertThat(documentContent).isNotNull();
            assertThat(new String(documentContent)).isEqualTo("documentbytes");

            byte[] attachmentContent = (byte[]) bigdata.getValue(1, "content");
            assertThat(attachmentContent).isNotNull();
            assertThat(new String(attachmentContent)).isEqualTo("attachment");


        });
    }

}
