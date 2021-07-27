package eu.domibus.connector.security.spring;

import eu.domibus.connector.common.annotations.BusinessDomainScoped;
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

import java.io.IOException;
import javax.validation.constraints.NotNull;

/**
 * contains security toolkit related configuration in a
 * typesafe way
 */
//@BusinessDomainScoped
//@Component
//@ConfigurationProperties(prefix = SecurityToolkitConfigurationProperties.CONFIG_PREFIX)
//@PropertySource("classpath:/eu/domibus/connector/security/spring/security-default-configuration.properties")
//@Validated
@BusinessDomainScoped
@Component
@Valid
@ConfigurationProperties(prefix = SecurityToolkitConfigurationProperties.CONFIG_PREFIX)
public class SecurityToolkitConfigurationProperties {

    public static final String CONFIG_PREFIX = "connector.security";

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityToolkitConfigurationProperties.class);


    @Valid
    @NotNull
    @NestedConfigurationProperty
    StoreConfigurationProperties keyStore = new StoreConfigurationProperties();

    @Valid
    @NotNull
    @NestedConfigurationProperty
    KeyConfigurationProperties privateKey = new KeyConfigurationProperties();

//    @Valid
//    @NotNull
    @NestedConfigurationProperty
    StoreConfigurationProperties ojStore = new StoreConfigurationProperties();

    @Valid
    @NotNull
    @NestedConfigurationProperty
    StoreConfigurationProperties trustStore = new StoreConfigurationProperties();

    /**
     * Should the trust store created if it is missing?
     */
    boolean createOjStoreIfMissing = true;

    public StoreConfigurationProperties getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(StoreConfigurationProperties store) {
        this.keyStore = store;
    }

    public KeyConfigurationProperties getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(KeyConfigurationProperties key) {
        this.privateKey = key;
    }

    public StoreConfigurationProperties getKeystore() {
        return keyStore;
    }

    public void setKeystore(StoreConfigurationProperties keystore) {
        this.keyStore = keystore;
    }

    public StoreConfigurationProperties getOjStore() {
        return ojStore;
    }

    public void setOjStore(StoreConfigurationProperties ojStore) {
        this.ojStore = ojStore;
    }

    public StoreConfigurationProperties getTruststore() {
        return trustStore;
    }

    public void setTruststore(StoreConfigurationProperties trustStore) {
        this.trustStore = trustStore;
    }

    public StoreConfigurationProperties getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(StoreConfigurationProperties trustStore) {
        this.trustStore = trustStore;
    }

    public boolean isCreateOjStoreIfMissing() {
        return createOjStoreIfMissing;
    }

    public void setCreateOjStoreIfMissing(boolean createOjStoreIfMissing) {
        this.createOjStoreIfMissing = createOjStoreIfMissing;
    }

}
