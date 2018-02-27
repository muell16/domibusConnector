package eu.domibus.connector.persistence.testutil;

import eu.domibus.connector.persistence.service.PersistenceServiceITCase;
import org.junit.BeforeClass;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;


@SpringBootApplication(scanBasePackages={"eu.domibus.connector.persistence"})
public class SetupPersistenceContext {



    static ConfigurableApplicationContext APPLICATION_CONTEXT;

    @BeforeClass
    public static ConfigurableApplicationContext startApplicationContext() {
        return startApplicationContext(SetupPersistenceContext.class);
    }


    public static ConfigurableApplicationContext startApplicationContext(Class<?>... sources) {
        ConfigurableApplicationContext applicationContext;
        String dbName = UUID.randomUUID().toString().substring(0,10);
        SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder()
                //.profiles("test", "db_mysql")
                .sources(sources)
                .web(false)
                .profiles("test", "db_h2")
                .properties("liquibase.change-log=/db/changelog/install/initial-4.0.xml", "spring.datasource.url=jdbc:h2:mem:" + dbName)
                ;
        applicationContext = springAppBuilder.run();
        APPLICATION_CONTEXT = applicationContext;
        System.out.println("APPCONTEXT IS STARTED...:" + applicationContext.isRunning());
        return applicationContext;
    }
}
