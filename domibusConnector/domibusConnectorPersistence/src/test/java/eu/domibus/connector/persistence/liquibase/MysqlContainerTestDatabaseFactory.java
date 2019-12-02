package eu.domibus.connector.persistence.liquibase;

import org.junit.jupiter.api.Assumptions;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.util.Optional;

public class MysqlContainerTestDatabaseFactory implements TestDatabaseFactory {

    @Override
    public String getDatabaseType() {
        return "mysql";
    }

    @Override
    public String getVersion() {
        return "0";
    }

    @Override
    public String getName() {
        return "Mysql within Docker";
    }

    class MysqlContainerTestDatabase implements TestDatabase {

        JdbcDatabaseContainer jdbcDatabaseContainer;


        @Override
        public DataSource getDataSource() {
            return DataSourceBuilder
                    .create()
                    .driverClassName(jdbcDatabaseContainer.getDriverClassName())
                    .url(jdbcDatabaseContainer.getJdbcUrl())
                    .username(jdbcDatabaseContainer.getUsername())
                    .password(jdbcDatabaseContainer.getPassword())
                    .build();
        }

        @Override
        public void close() throws Exception {
            jdbcDatabaseContainer.stop();
        }
    }

    @Override
    public boolean isAvailable(String version) {
        Assumptions.assumeTrue(Optional.ofNullable(System.getProperty("DOCKER_HOST")).isPresent(), "No docker available!");
        if (version != null) {
//            throw new RuntimeException("Cannot provide db with data in version " + version);
            Assumptions.assumeTrue(true, "Cannot provide db with data in version " + version);
        }
        return true;
    }

    @Override
    public TestDatabase createNewDatabase(String version) {
        MysqlContainerTestDatabase testDatabase = new MysqlContainerTestDatabase();
        MySQLContainer mysql = new MySQLContainer();
        mysql.start();
        testDatabase.jdbcDatabaseContainer = mysql;

        String driverClassName = mysql.getDriverClassName();

        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            Assumptions.assumeTrue(false, String.format("Cannot load mysql driver %s", driverClassName));
        }

        return testDatabase;
    }
}
