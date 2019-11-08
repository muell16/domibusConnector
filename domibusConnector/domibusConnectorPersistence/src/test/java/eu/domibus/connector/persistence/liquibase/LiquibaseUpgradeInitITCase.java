package eu.domibus.connector.persistence.liquibase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;
import java.util.stream.Stream;

public class LiquibaseUpgradeInitITCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiquibaseUpgradeInitITCase.class);

//    @TestTemplate
//    @ExtendWith(LiquibaseTemplateInvocationContextProvider.class)
//    void testConnectivity(Properties p) {
//        System.out.println(p.getProperty("spring.datasource.url"));
//        Assertions.assertNotNull(p);
//    }

    /*
     * INSTALL VERSION 4 Tests
    */
//    @TestTemplate
//    @ExtendWith(LiquibaseTemplateInvocationContextProvider.class)
//    protected void checkInitialV4_0Scripts(Properties props) {
//        System.out.println("\n\n\n######################\nRUNNING TEST: checkInstallDB");
//        props.put("spring.liquibase.change-log","classpath:/db/changelog/install/initial-4.0.xml");
//        checkLiquibaseRuns(props);
//    }

    @TestTemplate
    @ExtendWith(LiquibaseTemplateInvocationContextProvider.class)
    protected void checkInitialV4_0Scripts(Properties props) {
        System.out.println("\n\n\n######################\nRUNNING TEST: checkInstallDB");
        props.put("spring.liquibase.change-log","classpath:/db/changelog/install-4.0.xml");
        checkLiquibaseRuns(props);
    }

    @TestTemplate
    @ExtendWith(LiquibaseTemplateInvocationContextProvider.class)
    protected void checkInitialV4_1Scripts(Properties props) {
        System.out.println("\n\n\n######################\nRUNNING TEST: checkInstallDB");
        props.put("spring.liquibase.change-log","classpath:/db/changelog/install-4.1.xml");
        checkLiquibaseRuns(props);
    }

    @TestTemplate
    @ExtendWith(LiquibaseTemplateInvocationContextProvider.class)
    protected void checkInitialV4_2Scripts(Properties props) {
        System.out.println("\n\n\n######################\nRUNNING TEST: checkInstallDB");
        props.put("spring.liquibase.change-log","classpath:/db/changelog/install-4.2.xml");
        checkLiquibaseRuns(props);
    }





    public void checkLiquibaseRuns(Properties props) {
        LOGGER.info("Running test with Properties: [{}]", props);

        SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder(DatabaseInitUpgradeITCase.TestConfiguration.class)
                .profiles("test")
                .properties(props);

        ConfigurableApplicationContext ctx = springAppBuilder.run();
        try {
            DataSource ds = ctx.getBean(DataSource.class);
            //TODO: test DB
            Connection connection = ds.getConnection();
            Assertions.assertNotNull(connection);
//            connection.createStatement().executeQuery("SELECT * FROM DOMIBUS_CONNECTOR_PARTY");
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ctx.close();
        }
    }



}
