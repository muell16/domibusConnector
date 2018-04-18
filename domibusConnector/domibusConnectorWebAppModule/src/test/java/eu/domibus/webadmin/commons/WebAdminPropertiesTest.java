package eu.domibus.webadmin.commons;

import static org.assertj.core.api.Assertions.assertThat;

import eu.domibus.test.db.TestDatabase;
import java.sql.SQLException;
import javax.sql.DataSource;
import liquibase.exception.LiquibaseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;

/**
 *
 * @author spindlest
 */
@Ignore
public class WebAdminPropertiesTest {
    
    public static DataSource DATA_SOURCE; 
    
    private WebAdminProperties webAdminProperties;
   
    @BeforeClass
    public static void setUpClass() throws SQLException, LiquibaseException {
       DATA_SOURCE = TestDatabase.embeddedH2DB();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        webAdminProperties = new WebAdminProperties(DATA_SOURCE);
    }
    
    @After
    public void tearDown() {
    }
       
    /**
     * Test of loadProperties method, of class WebAdminProperties.
     */
    @Test
    public void testLoadProperties() {
        System.out.println("loadProperties");       
        webAdminProperties.loadProperties();   
        
    }

    /**
     * Test of saveProperty method, of class WebAdminProperties.
     */
    @Test
    @Ignore //test does not make sens yet, so ignore it meanwhile!
    public void testSaveProperty() {
        System.out.println("saveProperty");

        assertThat(webAdminProperties).isNotNull();
        
        webAdminProperties.setRestServerAddress("localhost");
        webAdminProperties.setRestServerPort("8021");
 
    }


    
}
