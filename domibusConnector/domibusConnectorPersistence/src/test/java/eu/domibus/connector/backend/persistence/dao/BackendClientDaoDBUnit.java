
package eu.domibus.connector.backend.persistence.dao;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.DBRiderTestExecutionListener;
import com.github.database.rider.spring.api.DBRider;
import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import eu.domibus.connector.persistence.dao.CommonPersistenceDBUnitITCase;
import eu.domibus.connector.persistence.testutil.RecreateDbByLiquibaseTestExecutionListener;
import eu.domibus.connector.persistence.testutil.SetupPersistenceContext;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 *
 */
@SpringBootTest(classes = BackendClientDaoDBUnit.TestConfiguration.class)
@TestPropertySource(properties = {
        "connector.persistence.big-data-impl-class=eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceJpaImpl",
        "spring.liquibase.change-log=db/changelog/test/testdata.xml",
        "spring.datasource.url=jdbc:h2:mem:${random.int(100)}", //use different randomly named dbs to seperate tests..
        "spring.active.profiles=connector,db-storage"
})
@ActiveProfiles({"test", "db_h2", "storage-db"})
@TestExecutionListeners(
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
        listeners = {RecreateDbByLiquibaseTestExecutionListener.class, //drop and create db by liquibase after each TestClass
                DBRiderTestExecutionListener.class, //activate @DBRider
        })
@DataSet(value = "/database/testdata/dbunit/BackendClient.xml", cleanAfter = true, cleanBefore = true)
public class BackendClientDaoDBUnit {

    @SpringBootApplication(scanBasePackages={"eu.domibus.connector.persistence", "eu.domibus.connector.backend.persistence"})
    static class TestConfiguration {
    }
    
//    @BeforeAll
//    public static void beforeClass() {
//        APPLICATION_CONTEXT = SetupPersistenceContext.startApplicationContext(SetupPersistenceContext.getDefaultProperties(),
//                SetupPersistenceContext.getDefaultProfiles(),
//                BackendClientDaoDBUnit.TestConfiguration.class);
//    }
    

//    @AfterAll
//    public static void afterClass() {
//        APPLICATION_CONTEXT.close();
//    }
    
    @Autowired
    private BackendClientDao backendClientDao;

    
//    @BeforeEach
//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
//        this.backendClientDao = applicationContext.getBean(BackendClientDao.class);
//
////        this.transactionTemplate = new TransactionTemplate(applicationContext.getBean(PlatformTransactionManager.class));
//
//        //Load testdata
//        IDataSet dataSet = new FlatXmlDataSetBuilder().setColumnSensing(true).build((new ClassPathResource("/database/testdata/dbunit/BackendClient.xml").getInputStream()));
//
//        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);
//        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
//
//    }
    
    @Test
    public void testFindOneBackendByBackendName() {
        BackendClientInfo backendClientBob = backendClientDao.findOneBackendByBackendNameAndEnabledIsTrue("bob");
        assertThat(backendClientBob).isNotNull();
    }
    
    @Test
    public void testFindByServices_service() {
        List<BackendClientInfo> backendClients = backendClientDao.findByServices_serviceAndEnabledIsTrue("EPO");
        
        assertThat(backendClients).as("should containe exact one element!").hasSize(1);
        BackendClientInfo clientBob = backendClients.get(0);
        assertThat(clientBob.getBackendName()).as("must be client bob").isEqualTo("bob");
    }
    
    
    @Test
    public void testFindByServices_service_shouldContain2Elements() {
        List<BackendClientInfo> backendClients = backendClientDao.findByServices_serviceAndEnabledIsTrue("LOCAL-CONNECTOR-TEST");
        assertThat(backendClients).as("should containe exact two elements!").hasSize(2);
    }

    @Test
    public void testFindOneEnabledBackendByBackendName() {
        BackendClientInfo notEnabled = backendClientDao.findOneBackendByBackendNameAndEnabledIsTrue("not_enabled");
        assertThat(notEnabled).isNull();
    }

}