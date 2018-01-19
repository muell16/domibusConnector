/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.persistence.service.CommonPersistenceDBUnitITCase;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.store.fs.FileUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class DomibusConnectorBigDataPersistenceServiceJdbcImplDBUnit {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorBigDataPersistenceServiceJdbcImplDBUnit.class);

    //static ConfigurableApplicationContext APPLICATION_CONTEXT;
  
    public static String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";
    
    private static File TEST_RESULTS_FOLDER;
    
    private static ApplicationContext APPLICATION_CONTEXT;
    
    private DomibusConnectorBigDataPersistenceService bigDataPersistenceService;
    
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
                //.profiles("test", "db_mysql")
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
                APPLICATION_CONTEXT.getBean(DomibusConnectorBigDataPersistenceServiceJdbcImpl.class);
        
        DataSource ds = APPLICATION_CONTEXT.getBean(DataSource.class);
        
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .setColumnSensing(true)
                .build((new ClassPathResource("database/testdata/dbunit/DomibusConnectorMessage.xml").getInputStream()));
        
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);        
        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
        
        
    }


    @Test
    public void testGetReadableDataSource() {
        
    }

    @Test
    public void testCreateDomibusConnectorBigDataReference() {
        
        bigDataPersistenceService.createDomibusConnectorBigDataReference(message);
    }

    @Test
    public void testDeleteDomibusConnectorBigDataReference() {
    }
    
}
