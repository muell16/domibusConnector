package eu.domibus.webadmin.runner;

import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.test.db.TestDatabase;
import org.junit.Ignore;
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
@ContextConfiguration(classes = {WebRunner.class}, //, TestDatabase.class, TestDatabaseInitByLiquibase.class},  
    initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles({"test", "db_h2"})
@WebAppConfiguration
@Ignore //fails because cannot load/connect to database in test context!
@Import({TestDatabase.class})
@Transactional
public class WebRunnerContextTest {


	//@Test
	public void testContextLoads() {
		
	}


}
