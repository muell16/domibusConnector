/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.liquibase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
//@RunWith(Parameterized.class)
public class DatabaseInitUpgradeITCase {

    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseInitUpgradeITCase.class);
    
    public final static String TEST_MYSQL_PROPERTY_PREFIX = "mysql";
    
    public final static String TEST_ORACLE_PROPERTY_PREFIX = "oracle";
    

   
    @Configuration
    @EnableAutoConfiguration
    static class TestConfiguration {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
           return new PropertySourcesPlaceholderConfigurer();
        }
    }

//    @BeforeClass
//    public static void beforeClass() {
//        Map env = System.getenv();
//        
//        env.entrySet().stream().forEach( entry -> entry.getClass() );
//        
//        env.entrySet().stream().forEach( e -> { Map.Entry entry = (Map.Entry) e; LOGGER.info("Property: [{}={}]", entry.getKey(), entry.getValue()); });
//        //LOGGER.info("Properties:\n{}", properties);
//        System.out.println("Hallo WELT!");
//        env.entrySet().stream().forEach( e -> { Map.Entry entry = (Map.Entry) e; System.out.println( "Property: " + entry.getKey() + "=" + entry.getValue() + "" ); });
//        
//    }

    /*
    * INSTALL VERSION 4 Tests
    */
    protected void checkInstallDB(String profile, Properties props) {
        System.out.println("\n\n\n######################\nRUNNING TEST: checkInstallDB");
        props.put("liquibase.change-log","classpath:/db/changelog/install/initial-4.0.xml");
        
        LOGGER.info("Running test with profile [{}] and \nProperties: [{}]", profile, props);
        SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder(TestConfiguration.class)
                .profiles("test", profile)                
                .properties(props);
        
        
        ConfigurableApplicationContext ctx = springAppBuilder.run();        
        try {
            DataSource ds = ctx.getBean(DataSource.class);
            //TODO: test DB
            Connection connection = ds.getConnection();
            connection.createStatement().executeQuery("SELECT * FROM DOMIBUS_CONNECTOR_PARTY");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
        
    @Test
    public void testInstall004Database_h2() {
        checkInstallDB("db_h2", new Properties());  
    }
    
    @Test
    public void testInstall004Database_mysql() {        
        Properties p = loadMysqlTestProperties();
        checkInstallDB("db_mysql", p);  
    }
    

    
    /*
    * INITIAL VERSION 3 Tests
    */
    protected void checkInital003DB(String profile, Properties props)  {
        System.out.println("\n\n\n######################\nRUNNING TEST: checkInital003DB");
        props.put("liquibase.change-log","classpath:/db/changelog/v003/initial-3.0.xml");
        
        LOGGER.info("Running test with profile [{}] and \nProperties: [{}]", profile, props);
        SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder(TestConfiguration.class)
                .profiles("test", profile)                
                .properties(props);
        
        
        ConfigurableApplicationContext ctx = springAppBuilder.run();        
        try {
            DataSource ds = ctx.getBean(DataSource.class);
            //TODO: test DB
            Connection connection = ds.getConnection();
            connection.createStatement().executeQuery("SELECT * FROM DOMIBUS_CONNECTOR_PARTY");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
//        if ("db_h2".equals(profile)) {
//            DataSource ds = ctx.getBean(DataSource.class);
//            
//        }
    }
    
    
    @Test
//    @Ignore
    public void testInitial003Database_h2() {
        checkInital003DB("db_h2", new Properties());  
    }
    
    @Test
    @Ignore
    public void testInitial003Database_mysql() {        
        Properties p = loadMysqlTestProperties();
        checkInital003DB("db_mysql", p);  
    }
    
    /*
     * TEST HELPER
     *
     */
    
    protected Properties loadProperties(String prefix) {
        org.junit.Assume.assumeTrue("true".equalsIgnoreCase(System.getenv("test.db." + prefix + ".enabled")));
        final String p = "test." + prefix;
        Properties props = new Properties();           
        Map<String, String> env = System.getenv();        
        Map<Object, Object> collect = env.entrySet()                  
                .stream()
                .filter( (Entry<String, String> entry) -> 
                { return entry.getKey() != null && entry.getKey().toUpperCase().startsWith(p.toUpperCase());} )
                .map( (Entry<String, String> entry) -> { 
                    return new  AbstractMap.SimpleEntry(
                            entry.getKey().substring(p.length() + 1).toLowerCase(), 
                            entry.getValue().toLowerCase());})
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
                //.collect(Collectors.toMap(Entry::getKey(), Entry::getValue())); 
        props.putAll(collect);
        return props;
    }
    
    protected Properties loadMysqlTestProperties() {
        return loadProperties("mysql");
    }
    
    protected Properties loadOracleTestProperties() {
        return loadProperties("oracle");
    }
    
    
}
