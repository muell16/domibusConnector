package eu.domibus.connector.lib.spring.configuration;

import eu.domibus.connector.lib.spring.configuration.validation.CheckKeyIsLoadableFromKeyStore;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A Property class to map properties for an
 * keystore with path and password
 * and an key with alias and password
 *
 *  .store.path=keystore path
 *  .store.password=keystore password
 *
 *  .key.alias=alias for a private key in the configured key store
 *  .key.password=password for this key
 *
 *
 */
@CheckKeyIsLoadableFromKeyStore
@Validated
public class KeyAndKeyStoreConfigurationProperties {


    public KeyAndKeyStoreConfigurationProperties() {
    }

    public KeyAndKeyStoreConfigurationProperties(StoreConfigurationProperties keyStore, KeyConfigurationProperties key) {
        this.store = keyStore;
        this.key = key;
    }

    /**
     * Configuration of the (Key/Certificate)Store
     */
    @NestedConfigurationProperty
    @Valid
    @NotNull
    private StoreConfigurationProperties store;

    /**
     * Configures the default alias to use
     */
    @NestedConfigurationProperty
    @Valid
    @NotNull
    private KeyConfigurationProperties key;

    public StoreConfigurationProperties getStore() {
        return store;
    }

    public void setStore(StoreConfigurationProperties store) {
        this.store = store;
    }

    public KeyConfigurationProperties getKey() {
        return key;
    }

    public void setKey(KeyConfigurationProperties key) {
        this.key = key;
    }


}
