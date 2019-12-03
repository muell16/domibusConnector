package eu.domibus.connector.persistence.testutil;

import org.junit.jupiter.api.Assumptions;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.Properties;

public class MysqlContainerTestDatabaseFactory implements TestDatabaseFactory {

    @Override
    public String getDatabaseType() {
        return "mysql";
    }

    @Override
    public String getName() {
        return "Mysql within Docker";
    }

    class MysqlContainerTestDatabase implements TestDatabase {

        JdbcDatabaseContainer jdbcDatabaseContainer;
        String version = null;

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
        public Properties getProperties() {
            Properties p = new Properties();
            p.setProperty("spring.datasource.driver-class-name", jdbcDatabaseContainer.getDriverClassName());
            p.setProperty("spring.datasource.url", jdbcDatabaseContainer.getJdbcUrl());
            p.setProperty("spring.datasource.username", jdbcDatabaseContainer.getUsername());
            p.setProperty("spring.datasource.password", jdbcDatabaseContainer.getPassword());
            return p;
        }

        @Override
        public String getName() {
            return String.format("Mysql data: [%s]", version == null ? "empty" : version);
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
        testDatabase.version = version;

        String driverClassName = mysql.getDriverClassName();

        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            Assumptions.assumeTrue(false, String.format("Cannot load mysql driver %s", driverClassName));
        }

        return testDatabase;
    }
}
