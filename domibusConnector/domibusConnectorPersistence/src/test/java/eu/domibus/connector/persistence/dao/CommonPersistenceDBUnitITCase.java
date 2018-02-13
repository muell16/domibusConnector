
package eu.domibus.connector.persistence.dao;

import javax.sql.DataSource;

import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public abstract class CommonPersistenceDBUnitITCase {



    @SpringBootApplication(scanBasePackages={"eu.domibus.connector.persistence"})
    static class TestConfiguration {
    }

    protected static ConfigurableApplicationContext APPLICATION_CONTEXT;

    public static ConfigurableApplicationContext setUpTestDatabaseWithSpringContext() {
        return setUpTestDatabaseWithSpringContext(TestConfiguration.class);
    }
    
    
    public static ConfigurableApplicationContext setUpTestDatabaseWithSpringContext(Class ...sources) {
        String dbName = UUID.randomUUID().toString().substring(0,10);
        //the random name of the database is generated to ensure that the database is not reused between the test classes
        SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder()
                //.profiles("test", "db_mysql")
                .sources(sources)
                .web(false)
                .profiles("test", "db_h2")
                .properties("liquibase.change-log=classpath:/db/changelog/install/initial-4.0.xml", "spring.datasource.url=jdbc:h2:mem:" + dbName)
                ;
        ConfigurableApplicationContext applicationContext = springAppBuilder.run();
        System.out.println("APPCONTEXT IS STARTED...:" + applicationContext.isRunning());       
        return applicationContext;
    }
    
    @BeforeClass
    public static void beforeClass() {
        APPLICATION_CONTEXT = setUpTestDatabaseWithSpringContext();
    }
    

    @AfterClass
    public static void afterClass() {
        APPLICATION_CONTEXT.close();
    }

    protected DataSource ds;

    protected DomibusConnectorPersistenceService persistenceService;

    protected ConfigurableApplicationContext applicationContext;
        
    @Before
    public void setUp() throws Exception {        
        this.applicationContext = APPLICATION_CONTEXT;
        //lookup type
        this.ds = APPLICATION_CONTEXT.getBean(DataSource.class);
        //lookup name
        this.persistenceService = APPLICATION_CONTEXT.getBean("persistenceService", DomibusConnectorPersistenceService.class);
    }


    
    
    
}
