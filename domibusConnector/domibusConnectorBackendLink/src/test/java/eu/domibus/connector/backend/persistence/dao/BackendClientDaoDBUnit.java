
package eu.domibus.connector.backend.persistence.dao;

import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import eu.domibus.connector.persistence.dao.CommonPersistenceDBUnitITCase;
import java.util.List;

import eu.domibus.connector.persistence.testutil.SetupPersistenceContext;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class BackendClientDaoDBUnit extends CommonPersistenceDBUnitITCase {

    @SpringBootApplication(scanBasePackages={"eu.domibus.connector.persistence", "eu.domibus.connector.backend.persistence"})    
//    @Import(BackendPersistenceConfig.class)
    static class TestConfiguration {
    }
    
    @BeforeClass
    public static void beforeClass() {
        APPLICATION_CONTEXT = SetupPersistenceContext.startApplicationContext(BackendClientDaoDBUnit.TestConfiguration.class);
    }
    

    @AfterClass
    public static void afterClass() {
        APPLICATION_CONTEXT.close();
    }
    
    
    private BackendClientDao backendClientDao;


    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.backendClientDao = applicationContext.getBean(BackendClientDao.class);
        
//        this.transactionTemplate = new TransactionTemplate(applicationContext.getBean(PlatformTransactionManager.class));
        
        //Load testdata
        IDataSet dataSet = new FlatXmlDataSetBuilder().setColumnSensing(true).build((new ClassPathResource("database/testdata/dbunit/BackendClient.xml").getInputStream()));
        
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);        
        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
        
    }
    
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