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

import com.mchange.v2.c3p0.ComboPooledDataSource;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfiguration;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;

/**
 * A Test Database
 * @author spindlest
 *
 */
@Configuration
@Profile("test")
public class TestDatabase {

	
	@Bean
	@Profile("hsqldb")
	public DataSource embeddedDatabaseHSQLDB() {
		EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
	    		.setType(EmbeddedDatabaseType.HSQL)
	    		.addScript("DbScripts/createTable_hsqldb.sql")
	    	//	.addScript("dbscripts/data/testdata.sql")
	    		.build();
		return db;
	}
	
	@Bean
	@Profile("derbydb")
	public DataSource embeddedDatabase() {
		EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
	    		.setType(EmbeddedDatabaseType.DERBY)
	    		.addScript("DbScripts/createTable_derby.sql")
	    	//	.addScript("dbscripts/data/testdata.sql")
	    		.build();
		return db;
	}

	
	@Bean
	@Profile("mariadb")
	public DataSource createDataSource() throws PropertyVetoException, ManagedProcessException, ScriptException, SQLException {
			
		DBConfigurationBuilder configBuilder = DBConfigurationBuilder.newBuilder();
		configBuilder.setPort(0); // setPort(0); => autom. detect free port
//		configBuilder.setDataDir("/home/theapp/db"); // just an example
		
		DB db = DB.newEmbeddedDB(configBuilder.build());
		db.start();
		
		String url = configBuilder.getURL("test");		
		ComboPooledDataSource cpds = new ComboPooledDataSource();
					
		cpds.setDriverClass( "org.mariadb.jdbc.Driver" );
		cpds.setJdbcUrl( url );
		cpds.setUser("root");                                  
		cpds.setPassword("");     
		

		//create db; insert testdata
		Connection conn = cpds.getConnection();
		
		ScriptUtils.executeSqlScript(conn, new ClassPathResource("/DbScripts/createTable_MySQL.sql"));
	
		ScriptUtils.executeSqlScript(conn, new ClassPathResource("/dbscripts/data/testdata.sql"));
			

		return cpds;
	}
	
	

}
