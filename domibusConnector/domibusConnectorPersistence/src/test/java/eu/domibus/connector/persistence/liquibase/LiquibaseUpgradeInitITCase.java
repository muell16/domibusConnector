package eu.domibus.connector.persistence.liquibase;

import eu.domibus.connector.persistence.testutil.FromVersion;
import eu.domibus.connector.persistence.testutil.LiquibaseTemplateInvocationContextProvider;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.configuration.ConfigurationProperty;
import liquibase.configuration.GlobalConfiguration;
import liquibase.configuration.LiquibaseConfiguration;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.logging.LogFactory;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.statement.SqlStatement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

@ExtendWith(LiquibaseTemplateInvocationContextProvider.class)
public class LiquibaseUpgradeInitITCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiquibaseUpgradeInitITCase.class);

    //Test context which does load only spring and liquibase to test
    //liquibase initialization
    @SpringBootApplication(scanBasePackages = "not.existance.packages")
    public static class LiquibaseUpgradeTestConfiguration {

    }

//    @TestTemplate
//    @FromVersion("")
//    protected void checkInitialV4_0Scripts(Properties props) {
//        System.out.println("\n\n\n######################\nRUNNING TEST: checkInstallDB");
//        props.put("spring.liquibase.change-log", "classpath:/db/changelog/install-4.0.xml");
//        checkLiquibaseRuns(props);
//    }

    @TestTemplate //provides the multiple databases to test...
    @FromVersion("")
    protected void checkInitialV4_1Scripts(Properties props) {
        System.out.println("\n\n\n######################\nRUNNING TEST: checkInstallDB");
        props.put("spring.liquibase.change-log", "classpath:/db/changelog/install-4.1.xml");
        checkLiquibaseRuns(props);
    }

    @TestTemplate
    @FromVersion("")
    protected void checkInitialV4_2Scripts(Properties props) {
        System.out.println("\n\n\n######################\nRUNNING TEST: checkInstallDB");
        props.put("spring.liquibase.change-log", "classpath:/db/changelog/install-4.2.xml");
        checkLiquibaseRuns(props);
    }

    @TestTemplate
    @FromVersion("3.5.x")
    protected void checkUpgradeFrom3_5_x(Properties props) {
        System.out.println("\n\n\n######################\nRUNNING TEST: checkInstallDB");
        props.put("spring.liquibase.change-log", "classpath:/db/changelog/upgrade-from-3.5.1.xml");
        checkLiquibaseRuns(props);
    }

//    @TestTemplate
//    @FromVersion("4.1.x")
//    protected void checkUpgradeFrom4_1_x(Properties props) {
//        System.out.println("\n\n\n######################\nRUNNING TEST: checkInstallDB");
//        props.put("spring.liquibase.change-log", "classpath:/db/changelog/install-4.2.xml");
//        checkLiquibaseRuns(props);
//    }


    public void checkLiquibaseRuns(Properties props) {
        Assumptions.assumeTrue(props.get("testdb.name") != null, "Test database must be available!");
        LOGGER.info("Running test with Properties: [{}]", props);

        Assertions.assertTimeout(Duration.ofSeconds(90), () -> {
            SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder(LiquibaseUpgradeInitITCase.LiquibaseUpgradeTestConfiguration.class)
                    .profiles("test")
                    .properties(props);

            ConfigurableApplicationContext ctx = springAppBuilder.run();
            try {
                DataSource ds = ctx.getBean(DataSource.class);
                //TODO: test / verify DB
                Connection connection = ds.getConnection();
                Assertions.assertNotNull(connection);
                connection.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                ctx.close();
            }
        });
    }

}
