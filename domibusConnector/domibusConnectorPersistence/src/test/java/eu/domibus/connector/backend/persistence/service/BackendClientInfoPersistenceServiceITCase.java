package eu.domibus.connector.backend.persistence.service;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.spring.DBRiderTestExecutionListener;
import com.github.database.rider.spring.api.DBRider;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.persistence.dao.BackendClientDaoDBUnit;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.persistence.testutil.RecreateDbByLiquibaseTestExecutionListener;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackendClientInfoPersistenceServiceITCase.TestConfiguration.class)
@TestPropertySource(properties = {
        "connector.persistence.big-data-impl-class=eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceJpaImpl",
        "spring.liquibase.change-log=db/changelog/test/testdata.xml",
        "spring.datasource.url=jdbc:h2:mem:t2", //use different randomly named dbs to seperate tests..
        "spring.active.profiles=connector,db-storage"
})
@ActiveProfiles({"test", "db_h2", "storage-db"})
@TestExecutionListeners(
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
        listeners = {RecreateDbByLiquibaseTestExecutionListener.class, //drop and create db by liquibase after each TestClass
                DBRiderTestExecutionListener.class, //activate @DBRider
        })
@DataSet(value = "/database/testdata/dbunit/BackendClient.xml", strategy = SeedStrategy.CLEAN_INSERT)
public class BackendClientInfoPersistenceServiceITCase {

//    private static ConfigurableApplicationContext APPLICATION_CONTEXT;

    @Autowired
    private DataSource dataSource;
//    @Autowired
//    private ConfigurableApplicationContext applicationContext;
    @Autowired
    private BackendClientInfoPersistenceService backendClientInfoPersistenceService;

    @Autowired
    ApplicationContext applicationContext;

    @SpringBootApplication(scanBasePackages={"eu.domibus.connector.persistence", "eu.domibus.connector.backend.persistence"})
    static class TestConfiguration {
    }

//    @BeforeAll
//    public static void beforeClass() {
//        APPLICATION_CONTEXT = SetupPersistenceContext.startApplicationContext(TestConfiguration.class);
//    }

//    @BeforeEach
//    public void setUp() throws IOException, DatabaseUnitException, SQLException, LiquibaseException {
//
//
//        this.dataSource = applicationContext.getBean(DataSource.class);
//        this.backendClientInfoPersistenceService = applicationContext.getBean(BackendClientInfoPersistenceService.class);
//
//
//        SpringLiquibase springLiquibase = applicationContext.getBean(SpringLiquibase.class);
//        springLiquibase.setDropFirst(true);
//        springLiquibase.afterPropertiesSet(); //The database get recreated here
//
//        //Load testdata
//        IDataSet dataSet = new FlatXmlDataSetBuilder().setColumnSensing(true).build((new ClassPathResource("database/testdata/dbunit/BackendClient.xml").getInputStream()));
//
//        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(dataSource);
//        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
//    }

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
    public void testLoadUpdate() throws SQLException, DataSetException {
        DomibusConnectorBackendClientInfo backendClientInfo = backendClientInfoPersistenceService.getBackendClientInfoByName("alice");

        backendClientInfo.setDefaultBackend(true);
        backendClientInfo.setBackendPushAddress("my-push-address");
        backendClientInfo.setBackendKeyAlias("key-alias");

        backendClientInfoPersistenceService.save(backendClientInfo);

        //check db
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(dataSource);
        ITable queryAlice = conn.createQueryTable("QUERY_ALICE", "SELECT * FROM DOMIBUS_CONNECTOR_BACKEND_INFO WHERE BACKEND_NAME = 'alice'");

        BigDecimal id = (BigDecimal) queryAlice.getValue(0, "id");
        assertThat(id).isEqualTo(BigDecimal.valueOf(90));
        String name = (String) queryAlice.getValue(0, "backend_name");
        assertThat(name).isEqualTo("alice");

        //load again from db
        backendClientInfo = backendClientInfoPersistenceService.getBackendClientInfoByName("alice");
        assertThat(backendClientInfo).isNotNull();

    }


    @Test
    public void findEnabledByName() {
        DomibusConnectorBackendClientInfo alice = backendClientInfoPersistenceService.getEnabledBackendClientInfoByName("alice");
        assertThat(alice).isNotNull();
    }

    @Test
    public void findEnabledByName_isNotEnabled_shouldReturnNull() {
        DomibusConnectorBackendClientInfo notEnabledBackend = backendClientInfoPersistenceService.getEnabledBackendClientInfoByName("not_enabled");
        assertThat(notEnabledBackend).isNull();
    }

    @Test
    public void findEnabledByService() {
        DomibusConnectorService service = new DomibusConnectorService("EPO", "service_type");

        DomibusConnectorBackendClientInfo notEnabledBackend = backendClientInfoPersistenceService.getEnabledBackendClientInfoByService(service);
        assertThat(notEnabledBackend).isNotNull();
    }


}
