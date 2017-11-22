package eu.domibus.webadmin.runner;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebRunner.class},  
    initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles("test")
//@Ignore //failes because cannot load database in test context!
public class WebRunnerContextTest {

	
	@Test
	public void testContextLoads() {
		
	}


}
