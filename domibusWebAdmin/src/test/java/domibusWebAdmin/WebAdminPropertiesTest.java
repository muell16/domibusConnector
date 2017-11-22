package domibusWebAdmin;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.Import;

import eu.domibus.test.db.TestDatabase;
import eu.domibus.webadmin.commons.WebAdminProperties;
import eu.domibus.webadmin.runner.DomibusWebAdminContext;
import eu.domibus.webadmin.runner.JpaContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaContext.class, WebAdminProperties.class, DomibusWebAdminContext.class},  
	initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles({"test", "mariadb"}) //test with mariaDb
//@ActiveProfiles({"test", "hsqldb"})
@Import({TestDatabase.class}) //TODO: mark as DB Test!
public class WebAdminPropertiesTest {

	@Autowired
	WebAdminProperties webAdminProperties;


	@Before
	public void setUp() {
		//TODO: init Database...
		
	}
	
	
//	@Value("${spring.datasource.schema}")
//	String datasourceSchema;
	
	@Test
	public void testPropertiesLoad() {
		assertThat(webAdminProperties).isNotNull();
		webAdminProperties.loadProperties();		
		//assertThat(webAdminProperties.getConnectorDatabaseUrl()).isNotNull();		
		
		assertThat(webAdminProperties.getRestServerAddress()).isEqualTo("127.0.0.1");
	}

}
