package eu.domibus.webadmin.spring;

import eu.domibus.webadmin.spring.WebRunner;
import eu.domibus.test.db.TestDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebRunner.class, TestDatabase.class}, 
    initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles({"test", "db_h2"})
@WebAppConfiguration
@Import({TestDatabase.class})
@Transactional
public class WebRunnerContextTest {


    /**
     * This test ensures loading of context is working
     *  Database/dataSource is mocked by an embedded db
     */
	@Test
	public void testContextLoads() {
		
	}


}
