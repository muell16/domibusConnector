/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.liquibase;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import org.junit.Assume;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public abstract class CommonDatabaseMigrationITCase {

    public static final String H2_PROFILE = "db_h2";
    public static final String MYSQL_PROFILE = "db_mysql";
    public static final String ORACLE_PROFILE = "db_oracle";
    public static String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";
    public static final String TEST_MYSQL_PROPERTY_PREFIX = "mysql";
    public static final String TEST_ORACLE_PROPERTY_PREFIX = "oracle";

    protected static String getTestFilesDir() {
        String dir = System.getProperty(DatabaseInitUpgradeITCase.TEST_FILE_RESULTS_DIR_PROPERTY_NAME, "./target/testfileresults/");
        dir = dir + DatabaseInitUpgradeITCase.class.getSimpleName();
        return dir;
    }

    protected Properties loadH2TestProperties() {
        Properties p = new Properties();
        return p;
    }
    
    protected Properties loadMysqlTestProperties() {
        return loadProperties("mysql");
    }

    protected Properties loadOracleTestProperties() {
        return loadProperties("oracle");
    }

    /*
     * TEST HELPER
     *
     */
    protected Properties loadProperties(String prefix) {
        Assume.assumeTrue("true".equalsIgnoreCase(System.getenv("test.db." + prefix + ".enabled")));
        final String p = "test." + prefix;
        Properties props = new Properties();
        Map<String, String> env = System.getenv();
        Map<Object, Object> collect = env.entrySet().stream().filter((Map.Entry<String, String> entry) -> {
            return entry.getKey() != null && entry.getKey().toUpperCase().startsWith(p.toUpperCase());
        }).map((Map.Entry<String, String> entry) -> {
            return new AbstractMap.SimpleEntry(entry.getKey().substring(p.length() + 1).toLowerCase(), entry.getValue().toLowerCase());
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        //.collect(Collectors.toMap(Entry::getKey(), Entry::getValue()));
        props.putAll(collect);
        return props;
    }

    
    
}
