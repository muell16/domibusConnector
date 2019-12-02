package eu.domibus.connector.persistence.liquibase;

import org.junit.jupiter.api.Assumptions;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.util.Optional;

public class MysqlContainerDataSourceProvider implements DataSourceProvider {

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

    @Override
    public DataSource createNewDataSource() {

        Assumptions.assumeTrue(Optional.ofNullable(System.getProperty("DOCKER_HOST")).isPresent(), "No docker available!");

        MySQLContainer mysql = new MySQLContainer();

        mysql.start();

        String driverClassName = mysql.getDriverClassName();
        String jdbcUrl = mysql.getJdbcUrl();
        String username = mysql.getUsername();
        String password = mysql.getPassword();

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
