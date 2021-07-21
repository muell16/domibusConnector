package eu.domibus.connector.security.spring;

import eu.domibus.connector.common.service.CurrentBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.persistence.service.testutil.SecurityToolkitTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Test security toolkit config resolving
 */

//@TestPropertySource(locations={"classpath:test.properties", "classpath:test-sig.properties"},
//        properties= {   "liquibase.enabled=false",
//               "connector.security.keystore.password=password",
//                "connector.security.t=test"
//        })
@SpringBootTest(classes = SecurityToolkitTestContext.class)
@ActiveProfiles({"test", "seclib-test", "test-sig"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SecurityToolkitConfigurationPropertiesTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityToolkitConfigurationPropertiesTest.class);

    @Autowired
    SecurityToolkitConfigurationProperties securityToolkitConfigurationProperties;

    @BeforeEach
    public void setCurrentScope() {
        CurrentBusinessDomain.setCurrentBusinessDomain(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
    }

    @Test
    public void testKeyStorePathValid() {
        assertThat(securityToolkitConfigurationProperties.getKeyStore().getPath()).isNotNull();
    }

    @Test
    public void testKeyStorePasswordIsNotNull() {
        assertThat(securityToolkitConfigurationProperties.getKeyStore().getPassword()).isNotNull();
    }


}