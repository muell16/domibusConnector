package eu.domibus.connector.persistence.testutil;

import org.junit.jupiter.api.Assumptions;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.util.Properties;

public class MysqlTestDatabaseFactory implements TestDatabaseFactory {

    @Override
    public String getDatabaseType() {
        return "mysql";
    }

    @Override
    public String getName() {
        return "Native Mysql";
    }

    class MysqlTestDatabase implements TestDatabase {

        String driverClassName;
        String jdbcUrl;
        String username;
        String password;
        String version = null;

        @Override
        public DataSource getDataSource() {
            return DataSourceBuilder
                    .create()
                    .driverClassName(driverClassName)
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .build();
        }

        @Override
        public Properties getProperties() {
            Properties p = new Properties();
            p.setProperty("spring.datasource.driver-class-name", driverClassName);
            p.setProperty("spring.datasource.url", jdbcUrl);
            p.setProperty("spring.datasource.username", username);
            p.setProperty("spring.datasource.password", password);
            return p;
        }

        @Override
        public String getName() {
            return String.format("H2 %s data: [%s]", version == null ? "empty" : version);
        }

        @Override
        public void close() throws Exception {
            //cannot really shutdown remote db...
        }
    }

    @Override
    public MysqlTestDatabase createNewDatabase(String version) {
        isAvailable(version);

        MysqlTestDatabase mysqlTestDatabase = new MysqlTestDatabase();

        mysqlTestDatabase.driverClassName = System.getProperty("test.db.mysql.driverclassname");
        mysqlTestDatabase.jdbcUrl = System.getProperty("test.db.mysql.url");
        mysqlTestDatabase.username = System.getProperty("test.db.mysql.username");
        mysqlTestDatabase.password = System.getProperty("test.db.mysql.password");
        mysqlTestDatabase.version = version;

        try {
            Class.forName(mysqlTestDatabase.driverClassName);
        } catch (ClassNotFoundException e) {
            Assumptions.assumeTrue(false, String.format("Cannot load mysql driver %s", mysqlTestDatabase.driverClassName));
        }

        return mysqlTestDatabase;
    }

    @Override
    public boolean isAvailable(String version) {
        if (version != null) {
//            throw new RuntimeException("Cannot provide db with data in version " + version);
            Assumptions.assumeTrue(true, "Cannot provide db with data in version " + version);
        }
        Assumptions.assumeTrue("true".equalsIgnoreCase(System.getProperty("test.db.mysql.enabled")),
                "\nNative Mysql not available! Enable by setting following properties" +
                        "\ntest.db.mysql.enabled=true" +
                        "\ntest.db.mysql.driverclassname=<driverClassName>" +
                        "\ntest.db.mysql.url=<mysql driver url>" +
                        "\ntest.db.mysql.username=<username>" +
                        "\ntest.db.mysql.password=<password>"
        );
        return true;
    }
}
