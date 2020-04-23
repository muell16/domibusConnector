package eu.domibus.connector.persistence.testutil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.OracleContainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OracleContainerTestDatabaseFactory extends AbstractContainerTestDatabaseFactory implements TestDatabaseFactory {

    private static final Logger LOGGER = LogManager.getLogger(OracleContainerTestDatabaseFactory.class);

    List<String> availableVersions = Stream.of("4.1.x", "3.5.x").collect(Collectors.toList());

    public static final String SID = "testsid";
    public static final String DB_DOMAIN = "example.com";
    public static final String DB_PASSWORD = "test";

    @Override
    public String getDatabaseType() {
        return "oracle";
    }

    @Override
    public String getName() {
        return "Oracle within Docker";
    }

    protected JdbcDatabaseContainer getDatabaseContainer(String version) {

        OracleContainer oracle = new OracleContainer("store/oracle/database-enterprices:12.2.0.1");
        oracle.withEnv("DB_SID", SID)
                .withEnv("DB_PASSWD", DB_PASSWORD)
                .withEnv("DB_DOMAIN", DB_DOMAIN)
                .withEnv("DB_BUNDLE", "basic");

        return oracle;
    }


    public TestDatabase createNewDatabase(String version) {
        TestDatabase newDatabase = super.createNewDatabase(version);

        if (version != null) {
            String scriptFile = "/dbscripts/test/oracle/oracle" + version + ".sql";
            LOGGER.info("Loading initial script from [{}]", scriptFile);
            try {
//            Connection connection = newDatabase.getDataSource().getConnection("test", "test");
                Connection connection = newDatabase.getDataSource().getConnection();

                ScriptUtils.executeSqlScript(connection, new ClassPathResource(scriptFile));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return newDatabase;

    }

    @Override
    public boolean isAvailable(String version) {
        return availableVersions.contains(version) || version == null;
    }


}
