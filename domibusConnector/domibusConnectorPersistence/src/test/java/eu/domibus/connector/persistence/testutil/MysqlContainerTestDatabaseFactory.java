package eu.domibus.connector.persistence.testutil;

import org.junit.jupiter.api.Assumptions;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.util.Properties;

public class MysqlContainerTestDatabaseFactory extends AbstractContainerTestDatabaseFactory implements TestDatabaseFactory {


    @Override
    public String getDatabaseType() {
        return "mysql";
    }

    @Override
    public String getName() {
        return "Mysql within Docker";
    }

    protected JdbcDatabaseContainer getDatabaseContainer(String version) {
        MySQLContainer mysql = new MySQLContainer();
        return mysql;
    }


}
