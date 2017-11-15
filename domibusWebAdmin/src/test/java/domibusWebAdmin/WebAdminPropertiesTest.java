package domibusWebAdmin;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.domibus.webadmin.commons.WebAdminProperties;
import eu.domibus.webadmin.runner.JpaContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaContext.class, WebAdminProperties.class})
@ActiveProfiles("test")
public class WebAdminPropertiesTest {

	@Autowired
	WebAdminProperties webAdminProperties;

	@Before
	public void setUp() {
		//TODO: init Database...
		
	}
	
	
	@Test
	public void testPropertiesLoad() {
		assertThat(webAdminProperties).isNotNull();
		webAdminProperties.loadProperties();		
		assertThat(webAdminProperties.getConnectorDatabaseUrl()).isNotNull();		
	}

}
