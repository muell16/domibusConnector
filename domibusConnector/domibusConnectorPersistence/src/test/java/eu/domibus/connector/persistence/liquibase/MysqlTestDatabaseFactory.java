package eu.domibus.connector.persistence.liquibase;

import org.junit.jupiter.api.Assumptions;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

public class MysqlTestDatabaseFactory implements TestDatabaseFactory {

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
        return "Native Mysql";
    }

    class MysqlTestDatabase implements TestDatabase {

        String driverClassName;
        String jdbcUrl;
        String username;
        String password;

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
