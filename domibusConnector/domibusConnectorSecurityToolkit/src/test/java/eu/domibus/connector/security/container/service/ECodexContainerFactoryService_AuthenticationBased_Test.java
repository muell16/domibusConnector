package eu.domibus.connector.security.container.service;

import eu.domibus.connector.persistence.service.testutil.SecurityToolkitTestContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;


//@TestPropertySource({"classpath:test.properties", "classpath:test-auth.properties"})
@SpringBootTest(classes = SecurityToolkitTestContext.class)
@ActiveProfiles({"test", "test-auth", "seclib-test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ECodexContainerFactoryService_AuthenticationBased_Test extends ECodexContainerFactoryServiceITCaseTemplate {


}