package eu.domibus.connector.evidences.spring;

import eu.domibus.connector.evidences.HashValueBuilder;
import eu.domibus.connector.lib.spring.configuration.CertConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix = EvidencesToolkitConfigurationProperties.CONFIG_PREFIX)
@Validated
public class EvidencesToolkitConfigurationProperties {

    public static final String CONFIG_PREFIX = "connector.evidences";

    /**
     * Configuration of the keyStore which holds the private key to sign the evidences
     */
    @NestedConfigurationProperty
    StoreConfigurationProperties keystore = new StoreConfigurationProperties();

    /**
     * Configuration of the private key which is used to sign the evidences
     */
    @NestedConfigurationProperty
    CertConfigurationProperties key = new CertConfigurationProperties();

    /**
     * The hashAlgorithm used for signing the evidences
     * The default value is SHA256
     */
    HashValueBuilder.HashAlgorithm hashAlgorithm = HashValueBuilder.HashAlgorithm.SHA256;

    public StoreConfigurationProperties getKeystore() {
        return keystore;
    }

    public void setKeystore(StoreConfigurationProperties keystore) {
        this.keystore = keystore;
    }

    public CertConfigurationProperties getKey() {
        return key;
    }

    public void setKey(CertConfigurationProperties key) {
        this.key = key;
    }

    public HashValueBuilder.HashAlgorithm getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(HashValueBuilder.HashAlgorithm hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }


    @PostConstruct
    public void checkValues() {
        try {
            this.getKeystore().validatePathReadable();
        } catch (StoreConfigurationProperties.ValidationException ve) {
            throw new IllegalArgumentException("Check property: " + CONFIG_PREFIX + ".keystore" , ve);
        }
        throwIfNull(getKey(), "key");
        throwIfNull(getKey().getAlias(), "key.alias");
    }

    public void throwIfNull(Object nullcheck, String message) {
        if (nullcheck == null) {
            throw new IllegalArgumentException("Check property: " + CONFIG_PREFIX + "." + message + " is not allowed to be null!");
        }
    }
}
