package eu.domibus.connector.security.configuration;


import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import eu.domibus.connector.security.spring.SecurityToolkitConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

public class DomibusConnectorEnvironmentConfigurationTest {


    /**
     * The truststore can be null, if this is the case
     * the configuration should not check the asics certs
     *
     */
    @Test
    public void testEnvironmentConfigurationWithNullTrustStore() throws Exception {
        SecurityToolkitConfigurationProperties props = new SecurityToolkitConfigurationProperties();
        DomibusConnectorEnvironmentConfiguration envConf = new DomibusConnectorEnvironmentConfiguration();
        envConf.setSecurityToolkitConfigurationProperties(props);
        envConf.afterPropertiesSet();
    }


    @Test
    public void testEnvironmentConfigurationWithTrustStore() throws Exception {
        DomibusConnectorEnvironmentConfiguration envConf = new DomibusConnectorEnvironmentConfiguration();
        envConf.setSecurityToolkitConfigurationProperties(generateSecurityToolkitConfigurationProperties());
        envConf.afterPropertiesSet();

    }


    private SecurityToolkitConfigurationProperties generateSecurityToolkitConfigurationProperties() {
        SecurityToolkitConfigurationProperties props = new SecurityToolkitConfigurationProperties();

        StoreConfigurationProperties trustStore = new StoreConfigurationProperties();
        trustStore.setPath(new ClassPathResource("/keys/ojStore.jks"));
        trustStore.setPassword("ecodex");
        props.setTruststore(trustStore);

        return props;
    }

}