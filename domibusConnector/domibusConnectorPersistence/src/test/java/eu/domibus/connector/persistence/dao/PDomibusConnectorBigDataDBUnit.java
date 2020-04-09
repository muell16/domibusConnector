package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.persistence.model.PDomibusConnectorBigData;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Disabled("test conflicts with other tests")
@Ignore
public class PDomibusConnectorBigDataDBUnit extends CommonPersistenceDBUnitITCase {

    private DomibusConnectorBigDataDao bigDataDao;

    private DomibusConnectorMessageDao messageDao;

    private EntityManager entityManager;

    private PlatformTransactionManager transactionManager;

    private TransactionTemplate transactionTemplate;

    private DatabaseDataSourceConnection conn;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.bigDataDao = applicationContext.getBean(DomibusConnectorBigDataDao.class);
        this.messageDao = applicationContext.getBean(DomibusConnectorMessageDao.class);
        this.entityManager = applicationContext.getBean(EntityManager.class);
        this.transactionManager = applicationContext.getBean(PlatformTransactionManager.class);

        this.transactionTemplate = new TransactionTemplate(transactionManager);

        //Load testdata
        IDataSet dataSet = new FlatXmlDataSetBuilder().setColumnSensing(true).build((new ClassPathResource("database/testdata/dbunit/DomibusConnectorBigDataContent.xml").getInputStream()));

        this.conn = new DatabaseDataSourceConnection(ds);
        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
    }


    @Test(timeout=20000)
    @Ignore
    public void testSave() throws SQLException, DataSetException {
        long msgId = 72L;
        PDomibusConnectorMessage msg = messageDao.findById(msgId).get();

        PDomibusConnectorBigData bigData = new PDomibusConnectorBigData();
        bigData.setMessage(msgId);

        transactionTemplate.execute( status -> {
            Session hibernateSession = entityManager.unwrap(Session.class);
            Blob blob = Hibernate.getLobCreator(hibernateSession).createBlob("HELLO WORLD I AM A VERY LONG CONTENT".getBytes());
            bigData.setContent(blob);
            bigData.setMimeType("application/octet-stream");
            bigData.setName("name");

            bigDataDao.save(bigData);
            return null;
        });

        //check database
        ITable dataTable = this.conn.createQueryTable("DATARES", "SELECT * FROM DOMIBUS_CONNECTOR_BIGDATA WHERE MESSAGE_ID = " + msgId);
        int rowCount = dataTable.getRowCount();

        assertThat(rowCount).isEqualTo(1);
    }
}
