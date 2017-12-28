/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.service;

import javax.sql.DataSource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public abstract class CommonPersistenceDBUnitITCase {



    @SpringBootApplication(scanBasePackages={"eu.domibus.connector"})
    static class TestConfiguration {
    }

    static ConfigurableApplicationContext APPLICATION_CONTEXT;

    @BeforeClass
    public static void InitClass() {
        SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder(TestConfiguration.class)
                //.profiles("test", "db_mysql")
                .profiles("test", "db_h2")
                .properties("liquibase.change-log=/db/changelog/install/initial-4.0.xml")
                ;
        APPLICATION_CONTEXT = springAppBuilder.run();
        System.out.println("APPCONTEXT IS STARTED...:" + APPLICATION_CONTEXT.isRunning());

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
