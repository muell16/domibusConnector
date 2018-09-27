package eu.domibus.connector.lib.spring.configuration;

import eu.domibus.connector.lib.spring.configuration.validation.CheckKeyIsLoadableFromKeyStore;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;



@CheckKeyIsLoadableFromKeyStore
public class KeyAndKeyStoreAndTrustStoreConfigurationProperties {


    public KeyAndKeyStoreAndTrustStoreConfigurationProperties() {
    }


    /**
     * Configuration of the (Key/Certificate)Store
     */
    @NestedConfigurationProperty
    @Valid
    @NotNull
    private StoreConfigurationProperties keyStore;

    /**
     * Configures the default alias to use
     */
    @NestedConfigurationProperty
    @Valid
    @NotNull
    private KeyConfigurationProperties key;


    /**
     * Configuration of the TrustStore
     */
    @NestedConfigurationProperty
    @Valid
    @NotNull
    private StoreConfigurationProperties trustStore;

    public StoreConfigurationProperties getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(StoreConfigurationProperties keyStore) {
        this.keyStore = keyStore;
    }

    public KeyConfigurationProperties getKey() {
        return key;
    }

    public void setKey(KeyConfigurationProperties key) {
        this.key = key;
    }

    public StoreConfigurationProperties getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(StoreConfigurationProperties trustStore) {
        this.trustStore = trustStore;
    }
}
