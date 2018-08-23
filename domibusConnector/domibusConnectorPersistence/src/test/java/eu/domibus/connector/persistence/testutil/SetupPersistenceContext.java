package eu.domibus.connector.persistence.testutil;

import org.junit.BeforeClass;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Properties;
import java.util.UUID;


@SpringBootApplication(scanBasePackages={"eu.domibus.connector.persistence"})
public class SetupPersistenceContext {



    static ConfigurableApplicationContext APPLICATION_CONTEXT;

    @BeforeClass
    public static ConfigurableApplicationContext startApplicationContext() {
        return startApplicationContext();
    }

    public static ConfigurableApplicationContext startApplicationContext(Class<?>... sources) {
        return startApplicationContext(new Properties(), sources);
    }

    public static ConfigurableApplicationContext startApplicationContext(Properties props) {
        return startApplicationContext(props, SetupPersistenceContext.class);
    }

    public static ConfigurableApplicationContext startApplicationContext(Properties props, Class<?>... sources) {
        ConfigurableApplicationContext applicationContext;
        String dbName = UUID.randomUUID().toString().substring(0,10); //create random db name to avoid conflicts between tests
        SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder()
                .sources(sources)
                .web(WebApplicationType.NONE)
                .profiles("test", "db_h2")
                //start with JPA big file storage
                .properties("connector.persistence.big-data-impl-class=eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceJpaImpl")
                .properties("spring.liquibase.change-log=db/changelog/test/testdata.xml",
                        "spring.datasource.url=jdbc:h2:mem:" + dbName)
                .properties(props)
                ;
        applicationContext = springAppBuilder.run();
        APPLICATION_CONTEXT = applicationContext;
        System.out.println("APPCONTEXT IS STARTED...:" + applicationContext.isRunning());
        return applicationContext;
    }
}
