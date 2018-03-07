package eu.domibus.connector.security.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Test security toolkit config resolving
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={SecurityToolkitConfigurationProperties.class})
@TestPropertySource(locations={"classpath:test.properties", "classpath:test-sig.properties"},
        properties= {   "liquibase.enabled=false",
               "connector.security.keystore.password=password",
                "connector.security.t=test"
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