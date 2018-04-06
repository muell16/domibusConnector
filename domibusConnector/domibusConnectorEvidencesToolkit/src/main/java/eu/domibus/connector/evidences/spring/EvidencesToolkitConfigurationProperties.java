package eu.domibus.connector.evidences.spring;

import eu.domibus.connector.evidences.HashValueBuilder;
import eu.domibus.connector.lib.spring.configuration.CertConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "connector.evidences")
@Validated
public class EvidencesToolkitConfigurationProperties {

    StoreConfigurationProperties keystore = new StoreConfigurationProperties();

    CertConfigurationProperties key = new CertConfigurationProperties();

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
}
