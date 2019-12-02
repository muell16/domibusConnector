package eu.domibus.connector.persistence.liquibase;

import org.assertj.core.api.Assertions;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(DatabaseDatasourceInvocationContextProvider.class)
public class HandWrittenDatabaseScriptsITCase {

    public static final String INITIAL_SCRIPTS_CLASSPATH = "/dbscripts/initial/";

    public static List<String> INITIAL_VERSIONS = Stream
            .of("4.1.0")
            .collect(Collectors.toList());

    public List<DataSourceProvider> dataSourceFactoryStream = Stream
            .of(h2OracleDataSourceFactory(), h2MysqlDataSourceFactory(), new MysqlDataSourceProvider(), new MysqlContainerDataSourceProvider())
            .collect(Collectors.toList());



    public DataSourceProvider h2MysqlDataSourceFactory() {
        DataSourceProvider dsf = new DataSourceProvider() {
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
                return "H2 mysql";
            }

            @Override
            public DataSource createNewDataSource() {
                JdbcDataSource ds = new JdbcDataSource();
                ds.setURL("jdbc:h2:mem:" + UUID.randomUUID().toString().substring(0,6) + ";MODE=MYSQL");
                ds.setUser("sa");
                return ds;
            }
        };
        return dsf;
    }


    public DataSourceProvider h2OracleDataSourceFactory() {
        DataSourceProvider dsf = new DataSourceProvider() {
            @Override
            public String getDatabaseType() {
                return "oracle";
            }

            @Override
            public String getVersion() {
                return "0";
            }

            @Override
            public String getName() {
                return "H2 oracle";
            }

            @Override
            public DataSource createNewDataSource() {
                JdbcDataSource ds = new JdbcDataSource();
                ds.setURL("jdbc:h2:mem:" + UUID.randomUUID().toString().substring(0,6) + ";MODE=Oracle");
                ds.setUser("sa");
                return ds;
            }
        };
        return dsf;
    }


    @TestFactory
    public Stream<DynamicContainer> testInitialScripts() {
        return INITIAL_VERSIONS.stream()
                .map(version -> {
                    DynamicContainer dynamicContainer = dynamicContainer(version, checkInitialScriptTest(version));
                    return dynamicContainer;
                });
    }

    public Stream<DynamicNode> checkInitialScriptTest(String version) {
        return dataSourceFactoryStream.stream()
                .map(d -> dynamicTest(d.getName() + " " + d.getVersion(),
                        () -> checkInitialScriptTest(version, d)));
    }



    public void checkInitialScriptTest(String initialScriptVersion, DataSourceProvider dataSourceFactory) throws SQLException {

        String initialScriptLocation = INITIAL_SCRIPTS_CLASSPATH + dataSourceFactory.getDatabaseType() + "_" + initialScriptVersion + "_initial.sql";
        Resource initialScriptResource = new ClassPathResource(initialScriptLocation);

        Assertions.assertThat(initialScriptResource.exists()).as("The initial database script mus be available under %s", initialScriptLocation).isTrue();

        DataSource dataSource = dataSourceFactory.createNewDataSource();
        Connection connection = dataSource.getConnection();
        ScriptUtils.executeSqlScript(connection, initialScriptResource);

    }






}
