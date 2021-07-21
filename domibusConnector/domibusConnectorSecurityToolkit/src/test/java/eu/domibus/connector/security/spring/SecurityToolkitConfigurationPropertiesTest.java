package eu.domibus.connector.security.spring;

import eu.domibus.connector.common.service.CurrentBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import test.context.SecurityToolkitTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Test security toolkit config resolving
 */


@SpringBootTest(classes = SecurityToolkitTestContext.class)
@ActiveProfiles({"test", "seclib-test", "test-sig"})
public class SecurityToolkitConfigurationPropertiesTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityToolkitConfigurationPropertiesTest.class);

    @Autowired
    SecurityToolkitConfigurationProperties securityToolkitConfigurationProperties;

    @BeforeEach
    public void beforeEach() {
        CurrentBusinessDomain.setCurrentBusinessDomain(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
    }

    @AfterEach
    public void afterEach() {
        CurrentBusinessDomain.setCurrentBusinessDomain(null);
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