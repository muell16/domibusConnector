package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.sql.DataSource;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import static org.assertj.core.api.Assertions.*;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.store.fs.FileUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StreamUtils;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class DomibusConnectorBigDataPersistenceServiceJdbcImplDBUnit {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorBigDataPersistenceServiceJdbcImplDBUnit.class);

    //static ConfigurableApplicationContext APPLICATION_CONTEXT;
  
    private final static byte[] TESTBYTES = "HELLO WORLD I AM A LONG BLOB OF A LOT OF DATA. AN MY END IS WITH THE DOT AT THE END OF THIS SENTENCE.".getBytes();
    
    private final static String TESTDATA1_ID = "ee56d9df-349c-4cb9-900c-324602644c98";
    
    public static String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";
    
    private static File TEST_RESULTS_FOLDER;
    
    private static ApplicationContext APPLICATION_CONTEXT;
    
    private DomibusConnectorBigDataPersistenceService bigDataPersistenceService;
    private DomibusConnectorPersistenceService persistenceService;
    
    private DataSource ds;
    private PlatformTransactionManager transactionManager;
    
    @SpringBootApplication(scanBasePackages={"eu.domibus.connector.persistence"})
    static class TestConfiguration {
    }
    
    @BeforeClass
    public static void InitClass() throws SQLException, DatabaseException, LiquibaseException {
        String dir = System.getenv().getOrDefault(TEST_FILE_RESULTS_DIR_PROPERTY_NAME, "./target/testfileresults/");
        dir = dir + "/" + DomibusConnectorBigDataPersistenceServiceJdbcImplDBUnit.class.getSimpleName();
        TEST_RESULTS_FOLDER = new File(dir);
        TEST_RESULTS_FOLDER.mkdirs();        
        
        
        ConfigurableApplicationContext ctx = initializeTestDatabaseWithinSpringContext();
        APPLICATION_CONTEXT = ctx;
        
    }
    
    public static ConfigurableApplicationContext initializeTestDatabaseWithinSpringContext() {
        File testDbFile = new File(TEST_RESULTS_FOLDER.getAbsolutePath() + "/testdb/");
        if (testDbFile.exists()) {
            //delete old testdb
            FileUtils.deleteRecursive(testDbFile.getAbsolutePath(), false);
        }
        
        SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder(TestConfiguration.class)
//                .profiles("test", "db_mysql")
                .profiles("test", "db_h2")                
                .properties("liquibase.change-log=/db/changelog/install/initial-4.0.xml",
                        "spring.datasource.url=jdbc:h2:" + testDbFile.getAbsolutePath() + "/db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=TRUE"
                        )
                ;
        ConfigurableApplicationContext applicationContext = springAppBuilder.run();
        System.out.println("APPCONTEXT IS STARTED...:" + applicationContext.isRunning());
        return applicationContext;
    }
    

        
    @Before
    public void setUp() throws DataSetException, DatabaseUnitException, SQLException, IOException {
        bigDataPersistenceService = 
                APPLICATION_CONTEXT.getBean(DomibusConnectorBigDataPersistenceService.class);
        
        ds = APPLICATION_CONTEXT.getBean(DataSource.class);
        transactionManager = APPLICATION_CONTEXT.getBean(PlatformTransactionManager.class);
        persistenceService = APPLICATION_CONTEXT.getBean(DomibusConnectorPersistenceService.class);
        
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .setColumnSensing(true)
                .build((new ClassPathResource("database/testdata/dbunit/DomibusConnectorBigData.xml").getInputStream()));
        
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);        
        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
        
        
    }


    @Test
    public void testGetReadableDataSource() throws IOException {
        //DomibusConnectorMessage message = Mockito.mock(DomibusConnectorMessage.class);
        //Mockito.when(message.getConnectorMessageId()).thenReturn("msg72");
        
        String storageReference = TESTDATA1_ID;
        
        DomibusConnectorBigDataReference dataReference = new DomibusConnectorBigDataReference();
        dataReference.setStorageIdReference(storageReference);
        
        DomibusConnectorBigDataReference readableDataSource = bigDataPersistenceService.getReadableDataSource(dataReference);
        
        InputStream inputStream = readableDataSource.getInputStream();
        byte[] data = StreamUtils.copyToByteArray(inputStream);
        
        assertThat(new String(data, "UTF-8")).isEqualTo(new String(TESTBYTES, "UTF-8"));
           
    }

    @Test
    public void testCreateDomibusConnectorBigDataReference() throws IOException, SQLException {
        DomibusConnectorMessage message = Mockito.mock(DomibusConnectorMessage.class);
        Mockito.when(message.getConnectorMessageId()).thenReturn("msg72");
        
        DomibusConnectorBigDataReference dataReference;
        dataReference = bigDataPersistenceService.createDomibusConnectorBigDataReference(message);
        assertThat(dataReference).isNotNull();

        OutputStream outputStream = dataReference.getOutputStream();

        StreamUtils.copy(TESTBYTES, outputStream);
        outputStream.close();

        
        //test if result is in database 
        Connection connection = ds.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM DOMIBUS_CONNECTOR_BIGDATA WHERE ID = ?");
        stm.setString(1, dataReference.getStorageIdReference());
        ResultSet rs = stm.executeQuery();
        rs.next();
        byte[] bytes = rs.getBytes("CONTENT");
        assertThat(new String(bytes, "UTF-8")).isEqualTo(new String(TESTBYTES, "UTF-8"));
        
    }

    @Test
    public void testDeleteDomibusConnectorBigDataReference() throws SQLException, AmbiguousTableNameException, DataSetException {
        DomibusConnectorMessage message = Mockito.mock(DomibusConnectorMessage.class);
        Mockito.when(message.getConnectorMessageId()).thenReturn("msg72");
        
        bigDataPersistenceService.deleteDomibusConnectorBigDataReference(message);
        
        
         //check result in DB
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);
        QueryDataSet dataSet = new QueryDataSet(conn);
        dataSet.addTable("domibus_connector_bigdata", "SELECT * FROM domibus_connector_bigdata");
       
        ITable domibusConnectorTable = dataSet.getTable("domibus_connector_bigdata");
        int rowCount = domibusConnectorTable.getRowCount();

        assertThat(rowCount).as("all data references to message with id 72 must be deleted").isEqualTo(1);
        
        
    }
    
}
