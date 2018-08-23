package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.testutil.SetupPersistenceContext;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;


public class BigDataJPAWithMessagePersistenceIntegrationITCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(BigDataJPAWithMessagePersistenceIntegrationITCase.class);

    private static ConfigurableApplicationContext APPLICATION_CONTEXT;

    private DomibusConnectorMessagePersistenceService messagePersistenceService;

    private DomibusConnectorPersistAllBigDataOfMessageService bigDataPersistenceService;

    private ApplicationContext applicationContext;
    private DataSource dataSource;
    private PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;
    private IDatabaseConnection conn;

    @BeforeClass
    public static void beforeClass() {
        Properties props = SetupPersistenceContext.getDefaultProperties();
        props.put("connector.persistence.big-data-impl-class","eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceJpaImpl");
        APPLICATION_CONTEXT = SetupPersistenceContext.startApplicationContext(props);
    }

    @AfterClass
    public static void afterClass() {
        APPLICATION_CONTEXT.close();
    }


    @Before
    public void setUp() throws SQLException {
        this.applicationContext = APPLICATION_CONTEXT;
        messagePersistenceService = applicationContext.getBean(DomibusConnectorMessagePersistenceService.class);
        bigDataPersistenceService = applicationContext.getBean(DomibusConnectorPersistAllBigDataOfMessageService.class);
        dataSource = applicationContext.getBean(DataSource.class);
        transactionManager = applicationContext.getBean(PlatformTransactionManager.class);
        this.conn = new DatabaseDataSourceConnection(dataSource);
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    /**
     * test write a message with attachments / content into database
     * and ensure, that everything is written
     */
    @Test(timeout=20000)
    public void testPersistMessageWithBigFiles() throws SQLException, DataSetException {
        LOGGER.info("run test testPersistMessageWithBigFiles");
        DomibusConnectorMessage message = DomainEntityCreator.createEpoMessage();
        message.setConnectorMessageId("msgid1");

        transactionTemplate.execute((TransactionStatus status) -> {
                DomibusConnectorMessage domibusConnectorMessage = messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.NAT_TO_GW);
                //message is in db
                domibusConnectorMessage = bigDataPersistenceService.persistAllBigFilesFromMessage(domibusConnectorMessage);
                assertThat(domibusConnectorMessage.getMessageContent().getDocument().getDocument().getStorageIdReference()).isNotNull();
                return null;
            }
        );

        //check database....
        ITable bigdata = conn.createQueryTable("BIGDATA", "SELECT * FROM DOMIBUS_CONNECTOR_BIGDATA");
        int rowCount = bigdata.getRowCount();

        assertThat(rowCount).isEqualTo(2);
        byte[] attachmentContent = (byte[]) bigdata.getValue(0, "content");
        assertThat(attachmentContent).isNotNull();
        assertThat(new String(attachmentContent)).isEqualTo("attachment");

        byte[] documentContent = (byte[]) bigdata.getValue(1, "content");
        assertThat(documentContent).isNotNull();
        assertThat(new String(documentContent)).isEqualTo("documentbytes");

    }

}
