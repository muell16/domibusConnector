package eu.domibus.connector.persistence.liquibase;

import org.assertj.core.api.Assertions;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.Assumptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.UUID;

public class H2TestDatabaseFactory implements TestDatabaseFactory {

    public static final String INITIAL_TEST_SCRIPTS_LOCATION = "dbscripts/test/h2/";

    String version;
    String dbType;

    public static H2TestDatabaseFactory h2Oracle() {
        H2TestDatabaseFactory h2DataSourceProvider = new H2TestDatabaseFactory();
        h2DataSourceProvider.dbType = "oracle";
        return h2DataSourceProvider;
    }

    public static H2TestDatabaseFactory h2Mysql() {
        H2TestDatabaseFactory h2DataSourceProvider = new H2TestDatabaseFactory();
        h2DataSourceProvider.dbType = "mysql";
        return h2DataSourceProvider;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    @Override
    public String getDatabaseType() {
        return dbType;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getName() {
        return String.format("H2 %s data: [%s]", dbType, version == null ? "empty" : version);
    }

    class H2TestDatabase implements TestDatabase {

        JdbcDataSource ds;

        @Override
        public DataSource getDataSource() {
            return ds;
        }

        @Override
        public void close() throws Exception {
            RunScript.execute(ds.getURL(), ds.getUser(), ds.getPassword(), "classpath:/dbscripts/test/h2/shutdown.sql", null, false);
        }
    }

    @Override
    public boolean isAvailable(String version) {
        return true;
    }

    @Override
    public TestDatabase createNewDatabase(String version) {
        H2TestDatabase testDatabase = new H2TestDatabase();

        JdbcDataSource ds = new JdbcDataSource();
        testDatabase.ds = ds;

        String jdbcUrl = "jdbc:h2:mem:" + UUID.randomUUID().toString().substring(0,6) + ";MODE=" + dbType;

        ds.setURL(jdbcUrl);
        ds.setUser("sa");

        if (version != null) {
            String initialScript = INITIAL_TEST_SCRIPTS_LOCATION + dbType + "_" + version + ".sql";
            ClassPathResource classPathResource = new ClassPathResource(initialScript);
            Assertions.assertThat(classPathResource.exists()).as("A initial db script must exist!").isTrue();
            try {
                ScriptUtils.executeSqlScript(ds.getConnection(), classPathResource);
            } catch (SQLException e) {
                throw new RuntimeException("Test preparation failed", e);
            }
        }

        return testDatabase;
    }
}
