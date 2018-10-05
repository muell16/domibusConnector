package eu.domibus.connector.persistence.testutil;

import org.junit.BeforeClass;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.*;


@SpringBootApplication(scanBasePackages={"eu.domibus.connector.persistence"})
public class SetupPersistenceContext {


    static ConfigurableApplicationContext APPLICATION_CONTEXT;

    public static Properties getDefaultProperties() {
        Properties props = new Properties();
        String dbName = UUID.randomUUID().toString().substring(0,10); //create random db name to avoid conflicts between tests
        props.put("dbname", dbName);
        props.put("connector.persistence.big-data-impl-class","eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceJpaImpl");
        props.put("spring.liquibase.change-log","db/changelog/test/testdata.xml");
        props.put("spring.datasource.url","jdbc:h2:mem:" + dbName);
        props.put("spring.active.profiles", "connector,db-storage");
        return props;
    }

    public static Set<String> getDefaultProfiles() {
        Set<String> defaultProfiles =  new HashSet<String>();
        defaultProfiles.addAll(Arrays.asList(new String[]{"test", "db_h2"}));
        return defaultProfiles;
    }


//    @BeforeClass
    public static ConfigurableApplicationContext startApplicationContext() {
        return startApplicationContext(getDefaultProperties());
    }

    public static ConfigurableApplicationContext startApplicationContext(Class<?>... sources) {
        return startApplicationContext(getDefaultProperties(), sources);
    }

    public static ConfigurableApplicationContext startApplicationContext(Properties props,  Set<String> profiles) {
        return startApplicationContext(props, profiles, SetupPersistenceContext.class);
    }

    public static ConfigurableApplicationContext startApplicationContext(Properties props) {
        return startApplicationContext(props, SetupPersistenceContext.class);
    }

    public static ConfigurableApplicationContext startApplicationContext(Properties props, Class<?>... sources) {
        Set<String> profiles = getDefaultProfiles();
        return startApplicationContext(props, profiles, sources);
    }

    public static ConfigurableApplicationContext startApplicationContext(Properties props, Set<String> profiles, Class<?>... sources) {
        ConfigurableApplicationContext applicationContext;

        SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder()
                .sources(sources)
                .web(WebApplicationType.NONE)
                .profiles(profiles.toArray(new String[profiles.size()]))
                .properties(props);
        applicationContext = springAppBuilder.run();
        APPLICATION_CONTEXT = applicationContext;
        System.out.println("APPCONTEXT IS STARTED...:" + applicationContext.isRunning());
        return applicationContext;
    }
}
