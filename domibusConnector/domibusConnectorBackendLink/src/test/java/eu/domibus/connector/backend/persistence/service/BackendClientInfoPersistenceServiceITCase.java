package eu.domibus.connector.backend.persistence.service;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.persistence.dao.BackendClientDao;
import eu.domibus.connector.backend.persistence.dao.BackendClientDaoDBUnit;
import eu.domibus.connector.persistence.testutil.SetupPersistenceContext;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class BackendClientInfoPersistenceServiceITCase {

    private static ConfigurableApplicationContext APPLICATION_CONTEXT;

    private DataSource dataSource;
    private ConfigurableApplicationContext applicationContext;
    private BackendClientInfoPersistenceService backendClientInfoPersistenceService;

    @SpringBootApplication(scanBasePackages={"eu.domibus.connector.persistence", "eu.domibus.connector.backend.persistence"})
    static class TestConfiguration {
    }

    @BeforeClass
    public static void beforeClass() {
        APPLICATION_CONTEXT = SetupPersistenceContext.startApplicationContext(TestConfiguration.class);
    }

    @Before
    public void setUp() throws IOException, DatabaseUnitException, SQLException {
        this.applicationContext = APPLICATION_CONTEXT;

        this.dataSource = applicationContext.getBean(DataSource.class);
        this.backendClientInfoPersistenceService = applicationContext.getBean(BackendClientInfoPersistenceService.class);

//        this.transactionTemplate = new TransactionTemplate(applicationContext.getBean(PlatformTransactionManager.class));

        //Load testdata
        IDataSet dataSet = new FlatXmlDataSetBuilder().setColumnSensing(true).build((new ClassPathResource("database/testdata/dbunit/BackendClient.xml").getInputStream()));

        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(dataSource);
        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
    }

    @Test
    public void testUpdate() throws SQLException, DataSetException {
        DomibusConnectorBackendClientInfo backendClientInfo = new DomibusConnectorBackendClientInfo();
        backendClientInfo.setBackendName("alice");
        backendClientInfo.setBackendPushAddress("my-push-address");
        backendClientInfo.setBackendKeyAlias("key-alias");

        backendClientInfoPersistenceService.save(backendClientInfo);

        //check db
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(dataSource);
        ITable queryAlice = conn.createQueryTable("QUERY_ALICE", "SELECT * FROM DOMIBUS_CONNECTOR_BACKEND_INFO WHERE BACKEND_NAME = 'alice'");

        BigDecimal id = (BigDecimal) queryAlice.getValue(0, "id");
        assertThat(id).isEqualTo(BigDecimal.valueOf(90));

    }

    @Test
    public void findByName() {
        DomibusConnectorBackendClientInfo alice = backendClientInfoPersistenceService.getBackendClientInfoByName("alice");
        assertThat(alice).isNotNull();

    }


}
