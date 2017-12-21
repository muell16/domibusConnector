/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.liquibase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.statement.SqlStatement;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/**
 * A Integration test for database  setup and migration
 * within spring context
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DatabaseInitUpgradeITCase extends CommonDatabaseMigrationITCase {

    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseInitUpgradeITCase.class);
    
       
    @Configuration
    @EnableAutoConfiguration
    static class TestConfiguration {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
           return new PropertySourcesPlaceholderConfigurer();
        }
    }

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
    @Ignore
    public void testInstall004Database_mysql() {        
        Properties p = loadMysqlTestProperties();
        checkInstallDB("db_mysql", p);  
    }
    
    
    @Test
    public void testMigrate3to4_h2() throws SQLException, LiquibaseException, DatabaseException, IOException {
        Properties props = new Properties();
        props.setProperty("spring.datasource.url", "jdbc:h2:mem:");
        testMigrate3to4Database(H2_PROFILE, props);
    }
    
    @Test
    public void testMigrate3to4_mysql() throws SQLException, LiquibaseException, DatabaseException, IOException {
        Properties props = loadMysqlTestProperties();
        //props.setProperty("spring.datasource.url", "jdbc:h2:mem:");
        testMigrate3to4Database("db_mysql", props);
    }
    
    /**
     * DB MIGRATION TEST
     */
    public void testMigrate3to4Database(String profile, Properties props) throws SQLException, DatabaseException, LiquibaseException, FileNotFoundException, IOException {
        System.out.println("\n\n\n######################\nRUNNING TEST: checkInital003DB");
        props.put("liquibase.change-log","classpath:/db/changelog/v004/upgrade-3to4.xml");       
//        if (H2_PROFILE.equalsIgnoreCase(profile)) {
        props.put("liquibase.enabled", "false"); //disable liquibase!
//        } else {
//            
//        }
        //SETUP OLD DB
        LOGGER.info("Running test with profile [{}] and \nProperties: [{}]", profile, props);
        SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder(TestConfiguration.class)
                .profiles("test", profile)                
                .properties(props);
        
        ConfigurableApplicationContext ctx = springAppBuilder.run();  
        DataSource ds = ctx.getBean(DataSource.class);
        
        Connection connection = ds.getConnection();
        if (H2_PROFILE.equalsIgnoreCase(profile)) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("/database/testdata/v3/domibus_v3_database_h2.sql"));                        
        }
        
       
        //OLD DB IS SETUP WITH OLD DATA
        

        //write migration sql file
        String testFilesDir = getTestFilesDir();
        File f = new File(testFilesDir);
        f.mkdirs();
        File sqlFile = new File(testFilesDir + "/sqlmigrate3to4_" + profile + ".sql");
        FileOutputStream fstream = new FileOutputStream(sqlFile);
        
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase = new Liquibase("db/changelog/v004/upgrade-3to4.xml", new ClassLoaderResourceAccessor(), database);
        
        DatabaseChangeLog databaseChangeLog = liquibase.getDatabaseChangeLog();
        for (ChangeSet set : databaseChangeLog.getChangeSets()) {
            for (Change change: set.getChanges()) {
                SqlStatement[] generateStatements = change.generateStatements(database);
                SqlGeneratorFactory instance = SqlGeneratorFactory.getInstance();
                Sql[] generateSql = instance.generateSql(generateStatements, database);
                for (Sql sql : generateSql) {
                    System.out.println("sql: " +  sql);                    
                    fstream.write(sql.toString().getBytes());
                    fstream.write("\n".getBytes());
                }                
            }
        }
        //getTestFilesDir
        
        //start migration
       
        liquibase.update(new Contexts());
        database.commit();
        
        
        //verify correct migration
        
        
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
    public void testInitial003Database_h2() {
        checkInital003DB("db_h2", new Properties());  
    }
    
    @Test
    @Ignore
    public void testInitial003Database_mysql() {        
        Properties p = loadMysqlTestProperties();
        checkInital003DB("db_mysql", p);  
    }
    
    
}
