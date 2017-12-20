package domibus.test.db;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.SQLException;

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
        
    @SuppressWarnings("unused")
	private final static Logger LOG = LoggerFactory.getLogger(TestDatabase.class);
    
   
    @Bean("dataSource")
    @Profile("db_h2")
    public static DataSource embeddedH2DB() {
        try {
            EmbeddedDatabase db = new EmbeddedDatabaseBuilder()                
                    .setType(EmbeddedDatabaseType.H2)                
                    .build();

            DataSource dataSource = db;

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));
            Liquibase liquibase = new Liquibase("db/changelog/changelog-master.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts());
            database.commit();
            
            
            return db;
        } catch (DatabaseException | SQLException ex) {
            throw new RuntimeException("Exception occured during test database init", ex);
        } catch (LiquibaseException ex) {
            throw new RuntimeException("Exception occured during test database init", ex);
        }
    }
    
}
