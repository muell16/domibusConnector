package eu.domibus.connector.security.spring;

import eu.domibus.connector.common.spring.CommonProperties;
import eu.domibus.connector.lib.spring.configuration.KeyConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

/**
 * contains security toolkit related configuration in a
 * typesafe way
 */
@Component
@ConfigurationProperties(prefix = SecurityToolkitConfigurationProperties.CONFIG_PREFIX)
@PropertySource("classpath:/eu/domibus/connector/security/spring/security-default-configuration.properties")
@Validated
public class SecurityToolkitConfigurationProperties {

    public static final String CONFIG_PREFIX = "connector.security";

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityToolkitConfigurationProperties.class);

    @Autowired
    CommonProperties commonProperties;

    @Valid
    @NestedConfigurationProperty
    StoreConfigurationProperties keystore = new StoreConfigurationProperties();

    @NestedConfigurationProperty
    KeyConfigurationProperties key = new KeyConfigurationProperties();

    @NestedConfigurationProperty
    StoreConfigurationProperties ojStore = new StoreConfigurationProperties();

    @NestedConfigurationProperty
    StoreConfigurationProperties truststore = new StoreConfigurationProperties();

    /**
     * Should the trust store created if it is missing?
     */
    boolean createOjStoreIfMissing = true;

    public StoreConfigurationProperties getKeyStore() {
        return keystore;
    }

    public void setKeyStore(StoreConfigurationProperties store) {
        this.keystore = store;
    }

    public KeyConfigurationProperties getKey() {
        return key;
    }

    public void setKey(KeyConfigurationProperties key) {
        this.key = key;
    }

    public StoreConfigurationProperties getKeystore() {
        return keystore;
    }

    public void setKeystore(StoreConfigurationProperties keystore) {
        this.keystore = keystore;
    }

    public StoreConfigurationProperties getOjStore() {
        return ojStore;
    }

    public void setOjStore(StoreConfigurationProperties ojStore) {
        this.ojStore = ojStore;
    }

    public StoreConfigurationProperties getTruststore() {
        return truststore;
    }

    public void setTruststore(StoreConfigurationProperties trustStore) {
        this.truststore = trustStore;
    }

    public boolean isCreateOjStoreIfMissing() {
        return createOjStoreIfMissing;
    }

    public void setCreateOjStoreIfMissing(boolean createOjStoreIfMissing) {
        this.createOjStoreIfMissing = createOjStoreIfMissing;
    }

    @PostConstruct
    public void checkValues() {
        throwIfNull(this.getKey(), "key");
        throwIfNull(this.getKey().getAlias(), "key.alias");

    }

    public void throwIfNull(Object prop, String propName) {
        if (prop == null) {
            String error = "Check property: " + CONFIG_PREFIX + "." + propName + " is not allowed to be null!";
            if (commonProperties.isFailOnInvalidProperty()) {
                throw new IllegalArgumentException(error);
            } else {
                LOGGER.warn(error);
            }
        }
    }
}
