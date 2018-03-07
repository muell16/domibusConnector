package eu.domibus.connector.security.spring;

import eu.domibus.connector.lib.spring.configuration.CertConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.Valid;
import java.io.IOException;

/**
 * contains security toolkit related configuration in a
 * typesafe way
 */
@Component
@ConfigurationProperties(prefix = "connector.security")
@Validated
public class SecurityToolkitConfigurationProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityToolkitConfigurationProperties.class);

    @Valid
    StoreConfigurationProperties keystore = new StoreConfigurationProperties();

    CertConfigurationProperties key = new CertConfigurationProperties();

    public StoreConfigurationProperties getKeyStore() {
        return keystore;
    }

    public void setKeyStore(StoreConfigurationProperties store) {
        this.keystore = store;
    }

    public CertConfigurationProperties getKey() {
        return key;
    }

    public void setKey(CertConfigurationProperties key) {
        this.key = key;
    }

    public StoreConfigurationProperties getKeystore() {
        return keystore;
    }

    public void setKeystore(StoreConfigurationProperties keystore) {
        this.keystore = keystore;
    }

}
