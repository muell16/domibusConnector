package eu.domibus.connector.persistence.liquibase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Properties;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
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
 @Disabled
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
        props.put("spring.liquibase.change-log","classpath:/db/changelog/install/initial-4.0.xml");
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
        } finally {
            ctx.close();
        }
    }


    @Test //(timeout=20000)
    public void testInstall004Database_h2() {
        Properties props = loadH2TestProperties();
        props.put("spring.datasource.url", "jdbc:h2:mem:install004");
        checkInstallDB("db_h2", props);  
    }

    @Test //(timeout=20000)
    @Disabled
    // setenv: test.mysql.db.enabled=true
    public void testInstall004Database_mysql() {        
        Properties p = loadMysqlTestProperties();
        checkInstallDB("db_mysql", p);  
    }


    @Test //(timeout=20000)
    @Disabled
    public void testMigrate3to4_h2() throws SQLException, LiquibaseException, DatabaseException, IOException {
        Properties props = loadH2TestProperties();
        props.put("spring.datasource.url", "jdbc:h2:mem:3to4db");
        testMigrate3to4Database(H2_PROFILE, props);
    }

    @Test //(timeout=20000)
    @Disabled
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
        props.put("spring.liquibase.change-log","classpath:/db/changelog/v4_0/upgrade-3to4.xml");
//        if (H2_PROFILE.equalsIgnoreCase(profile)) {
        props.put("spring.liquibase.enabled", "false"); //disable liquibase!
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
        Liquibase liquibase = new Liquibase("db/changelog/v4_0/upgrade-3to4.xml", new ClassLoaderResourceAccessor(), database);
        
        DatabaseChangeLog databaseChangeLog = liquibase.getDatabaseChangeLog();
        for (ChangeSet set : databaseChangeLog.getChangeSets()) {
            for (Change change: set.getChanges()) {
                try {
                    SqlStatement[] generateStatements = change.generateStatements(database);
                    SqlGeneratorFactory instance = SqlGeneratorFactory.getInstance();
                    Sql[] generateSql = instance.generateSql(generateStatements, database);
                    for (Sql sql : generateSql) {
                        System.out.println("sql: " +  sql);                    
                        fstream.write(sql.toString().getBytes());
                        fstream.write("\n".getBytes());
                    }    
                } catch (Exception e ) {
                    LOGGER.warn("Failed to print changeset", e);
                    //do nothing just don't print change set
                }
            }
        }
        //getTestFilesDir
        
        //start migration
       
        liquibase.update(new Contexts());
        database.commit();
        
        database.close();
        //verify correct migration
        
        ctx.close();
    }
    
    /*
    * INITIAL VERSION 3 Tests
    */
    protected void checkInital003DB(String profile, Properties props)  {
        System.out.println("\n\n\n######################\nRUNNING TEST: checkInital003DB");
        props.put("spring.liquibase.change-log","classpath:/db/changelog/v3_0/initial-3.0.xml");
        
        LOGGER.info("Running test with profile [{}] and \nProperties: [{}]", profile, props);
        SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder(TestConfiguration.class)                
                .profiles("test", profile)                   
                .properties(props);
        
        
        ConfigurableApplicationContext ctx = springAppBuilder.run();    

        DataSource ds = ctx.getBean(DataSource.class);
        try ( Connection connection = ds.getConnection() )  {            
            //TODO: test DB
         
            connection.createStatement().executeQuery("SELECT * FROM DOMIBUS_CONNECTOR_PARTY");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
        
//        if ("db_h2".equals(profile)) {
//            DataSource ds = ctx.getBean(DataSource.class);
//            
//        }

        ctx.close();
    }
    

//@Ignore    
    @Test //(timeout=20000)
    public void testInitial003Database_h2() {
        Assertions.assertTimeoutPreemptively(Duration.ofMinutes(2), ()-> {
            Properties props = super.loadH2TestProperties();
            props.put("spring.datasource.url", "jdbc:h2:mem:install003");
            checkInital003DB("db_h2", props);
        });
    }

    @Test //(timeout=20000)
    //@Ignore
    @Disabled
    public void testInitial003Database_mysql() {        
        Properties p = loadMysqlTestProperties();
        checkInital003DB("db_mysql", p);  
    }
    
    //TODO: oracle tests






}
