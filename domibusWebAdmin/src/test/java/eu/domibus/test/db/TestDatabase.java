package eu.domibus.test.db;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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

//	private static DataSource DS = null;
//	
//	public static DataSource getDataSource() throws ManagedProcessException, PropertyVetoException {
//		if (DS == null) {
//			TestDatabase td = new TestDatabase();
//			DBConfiguration dbConfig = td.createMariaDB();
//			
//			ComboPooledDataSource cpds = new ComboPooledDataSource();
//			cpds.setDriverClass( "org.postgresql.Driver" ); //loads the jdbc driver            
//			cpds.setJdbcUrl(""  );
//			cpds.setUser("root");                                  
//			cpds.setPassword("dbpassword");  
//			
//			
//			
//		}		
//		return DS;
//	}
//	
//	
//	public DBConfiguration createMariaDB() throws ManagedProcessException {
//			
//		DBConfigurationBuilder configBuilder = DBConfigurationBuilder.newBuilder();
//		configBuilder.setPort(0); // OR, default: setPort(0); => autom. detect free port
//		//configBuilder.setDataDir("/home/theapp/db"); // just an example
//		DB db = DB.newEmbeddedDB(configBuilder.build());
//		
//		db.start();
//		
//		db.getConfiguration().getPort();
//		
//		return db.getConfiguration();
//	}
//	
//	
//	public DataSource createDataSource() throws PropertyVetoException {
//		ComboPooledDataSource cpds = new ComboPooledDataSource();
//		cpds.setDriverClass( "org.postgresql.Driver" ); //loads the jdbc driver            
//		cpds.setJdbcUrl( "jdbc:postgresql://localhost/testdb" );
//		cpds.setUser("dbuser");                                  
//		cpds.setPassword("dbpassword");     
//		
//		return cpds;
//	}
	
	
	
//    @Bean 
//    public MariaDB4jSpringService mariaDB4jSpringService() {
//        MariaDB4jSpringService mariaDB4jSpringService = new MariaDB4jSpringService();
//        configureMariaDB4jSpringService(mariaDB4jSpringService);
//        return mariaDB4jSpringService;
//    }
//	
//    protected void configureMariaDB4jSpringService(MariaDB4jSpringService mariaDB4jSpringService) {
//    	
//    }

}
