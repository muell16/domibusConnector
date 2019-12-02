package eu.domibus.connector.persistence.liquibase;

import org.assertj.core.api.Assertions;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.UUID;

public class H2DataSourceProvider implements DataSourceProvider {

    public static final String INITIAL_TEST_SCRIPTS_LOCATION = "dbscripts/test/h2/";

    String version;
//    String initialScript;
    String dbType;

    public static H2DataSourceProvider h2Oracle() {
        H2DataSourceProvider h2DataSourceProvider = new H2DataSourceProvider();
        h2DataSourceProvider.dbType = "oracle";
//        if (version != null) {
//            h2DataSourceProvider.setInitialScript(INITIAL_TEST_SCRIPTS_LOCATION + h2DataSourceProvider.dbType + "_" + version + ".sql");
//        }
        return h2DataSourceProvider;
    }

    public static H2DataSourceProvider h2Mysql() {
        H2DataSourceProvider h2DataSourceProvider = new H2DataSourceProvider();
        h2DataSourceProvider.dbType = "mysql";
//        if (version != null) {
//            h2DataSourceProvider.setInitialScript(INITIAL_TEST_SCRIPTS_LOCATION + h2DataSourceProvider.dbType + "_" + version + ".sql");
//        }
        return h2DataSourceProvider;
    }

    public void setVersion(String version) {
        this.version = version;
    }

//    public String getInitialScript() {
//        return initialScript;
//    }
//
//    public void setInitialScript(String initialScript) {
//        this.initialScript = initialScript;
//    }

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

    @Override
    public DataSource createNewDataSource(String version) {
        JdbcDataSource ds = new JdbcDataSource();
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

        return ds;
    }
}
