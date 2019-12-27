package eu.domibus.connector.persistence.liquibase;

import eu.domibus.connector.persistence.testutil.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class HandWrittenDatabaseScriptsITCase {

    public static final String INITIAL_SCRIPTS_CLASSPATH = "/dbscripts/initial/";
    public static final String MIGRATE_SCRIPTS_CLASSPATH = "/dbscripts/migrate/";

    public static List<String> INITIAL_VERSIONS = Stream
            .of("4.1.0")
            .collect(Collectors.toList());

    public static List<TestDatabaseFactory> TEST_DATABASE_FACTORIES_LIST = Stream
            .of(
                    H2TestDatabaseFactory.h2Oracle(),
                    H2TestDatabaseFactory.h2Mysql(),
                    new MysqlTestDatabaseFactory(),
                    new OracleTestDatabaseFactory(),
                    new MysqlContainerTestDatabaseFactory()
                    //new PostgresContainerTestDatabaseFactory() //not supported yet!
            ).collect(Collectors.toList());


    public static final List<UpgradePath> UPGRADE_PATHS = Stream
            .of(upgradePath("3.5.x").to("4.0.0"))
            .collect(Collectors.toList());

    static UpgradePath upgradePath(String fromVersion) {
        UpgradePath up = new UpgradePath();
        up.fromVersion = fromVersion;
        return up;
    }

    public static class UpgradePath {
        String fromVersion;
        List<String> toVersion;

        public UpgradePath to(String... version) {
            toVersion = Stream.of(version).collect(Collectors.toList());
            return this;
        }
    }

    @TestFactory
    public Stream<DynamicContainer> testMigrateScripts() {
        return UPGRADE_PATHS.stream()
                .map(upgradePath -> dynamicContainer("From " + upgradePath.fromVersion,
                    upgradePath.toVersion.stream().map(toVersion -> dynamicContainer("migrate to " + toVersion, createMigrateToTest(upgradePath.fromVersion, toVersion)))
                ));
    }

    public Stream<DynamicNode> createMigrateToTest(String from, String toVersion) {
        return TEST_DATABASE_FACTORIES_LIST.stream()
                .map(d -> dynamicTest(d.getName(), () -> migrateToScriptTest(from, toVersion, d)));
    }

    public void migrateToScriptTest(String fromVersion, String toVersion, TestDatabaseFactory dataSourceFactory) throws SQLException {
        String migrateScriptLocation = MIGRATE_SCRIPTS_CLASSPATH + dataSourceFactory.getDatabaseType() + "_migrate_" + fromVersion + "_to_" + toVersion + ".sql";
        Resource migrateScriptResource = new ClassPathResource(migrateScriptLocation);

        Assertions.assertThat(migrateScriptResource.exists()).as("The migrate database script mus be available under %s", migrateScriptLocation).isTrue();
        Assumptions.assumeTrue(dataSourceFactory.isAvailable(fromVersion), "TestDatabase " + dataSourceFactory.getName() +  " must be available in version " + fromVersion);
        try (TestDatabase newDatabase = dataSourceFactory.createNewDatabase(fromVersion)) {
            DataSource dataSource = newDatabase.getDataSource();
            Connection connection = dataSource.getConnection();
            ScriptUtils.executeSqlScript(connection, migrateScriptResource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @TestFactory
    public Stream<DynamicContainer> testInitialScripts() {
        return INITIAL_VERSIONS.stream()
                .map(version -> {
                    DynamicContainer dynamicContainer = dynamicContainer("Initial to " + version, checkInitialScriptTest(version));
                    return dynamicContainer;
                });
    }

    public Stream<DynamicNode> checkInitialScriptTest(String version) {
        return TEST_DATABASE_FACTORIES_LIST.stream()
                .map(d -> dynamicTest(d.getName(),
                        () -> checkInitialScriptTest(version, d)));
    }



    public void checkInitialScriptTest(String initialScriptVersion, TestDatabaseFactory dataSourceFactory) throws SQLException {

        String initialScriptLocation = INITIAL_SCRIPTS_CLASSPATH + dataSourceFactory.getDatabaseType() + "_" + initialScriptVersion + "_initial.sql";
        Resource initialScriptResource = new ClassPathResource(initialScriptLocation);

        Assertions.assertThat(initialScriptResource.exists()).as("The initial database script mus be available under %s", initialScriptLocation).isTrue();

        Assumptions.assumeTrue(dataSourceFactory.isAvailable(null), "TestDatabase " + dataSourceFactory.getName() +  " must be available empty!");
        try (TestDatabase db = dataSourceFactory.createNewDatabase(null); ) {
            DataSource dataSource = db.getDataSource();
            Connection connection = dataSource.getConnection();
            ScriptUtils.executeSqlScript(connection, initialScriptResource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}