package eu.domibus.connector.security.spring;

import eu.domibus.connector.common.spring.CommonProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Test security toolkit config resolving
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={SecurityToolkitConfigurationProperties.class, CommonProperties.class})
@TestPropertySource(locations={"classpath:test.properties", "classpath:test-sig.properties"},
        properties= {   "liquibase.enabled=false",
//               "connector.security.keystore.password=password",
//                "connector.security.t=test"
        })
@EnableConfigurationProperties
public class SecurityToolkitConfigurationPropertiesTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityToolkitConfigurationPropertiesTest.class);

    @Autowired
    SecurityToolkitConfigurationProperties securityToolkitConfigurationProperties;

    @Test
    public void testKeyStorePathValid() {
        assertThat(securityToolkitConfigurationProperties.getKeyStore().getPath()).isNotNull();
    }

    @Test
    public void testKeyStorePasswordIsNotNull() {
        assertThat(securityToolkitConfigurationProperties.getKeyStore().getPassword()).isNotNull();
    }


}