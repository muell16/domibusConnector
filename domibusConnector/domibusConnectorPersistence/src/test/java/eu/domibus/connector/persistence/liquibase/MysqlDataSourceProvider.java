package eu.domibus.connector.persistence.liquibase;

import org.junit.jupiter.api.Assumptions;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

public class MysqlDataSourceProvider implements DataSourceProvider {

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

    @Override
    public DataSource createNewDataSource(String version) {
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

        String driverClassName = System.getProperty("test.db.mysql.driverclassname");
        String jdbcUrl = System.getProperty("test.db.mysql.url");
        String username = System.getProperty("test.db.mysql.username");
        String password = System.getProperty("test.db.mysql.password");

        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            Assumptions.assumeTrue(false, String.format("Cannot load mysql driver %s", driverClassName));
        }

        DataSource build = DataSourceBuilder
                .create()
                .driverClassName(driverClassName)
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .build();

        return build;
    }
}
