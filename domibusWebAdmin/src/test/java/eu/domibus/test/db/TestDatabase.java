package eu.domibus.test.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * A Test Database
 * 
 * the corresponding test database must be on the class path!
 * @author spindlest
 *
 */
@Configuration
@Profile("test")
public class TestDatabase {
        
    private final static Logger LOG = LoggerFactory.getLogger(TestDatabase.class);
    
   
    @Bean
    @Profile("db_h2")
    public DataSource embeddedH2DB() {
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder()                
                .setType(EmbeddedDatabaseType.H2)                
                .build();
        return db;
    }
    
	
	@Bean
	@Profile("db_hsql")
	public DataSource embeddedDatabaseHSQLDB() {
		EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
	    		.setType(EmbeddedDatabaseType.HSQL)
	    	//	.addScript("DbScripts/createTable_hsqldb.sql")
	    	//	.addScript("dbscripts/data/testdata.sql")
	    		.build();
		return db;
	}
	
	@Bean
	@Profile("db_derby")
	public DataSource embeddedDatabase() {
		EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
	    		.setType(EmbeddedDatabaseType.DERBY)
	    	//	.addScript("DbScripts/createTable_derby.sql")
	    	//	.addScript("dbscripts/data/testdata.sql")
	    		.build();
		return db;
	}

	
	
	

}
