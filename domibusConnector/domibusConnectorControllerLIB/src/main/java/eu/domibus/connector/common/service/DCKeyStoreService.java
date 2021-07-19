package eu.domibus.connector.common.service;

import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import org.springframework.stereotype.Service;

import java.security.KeyStore;

/**
 * Handles loading and storing of keystores
 * centrally
 *
 *  so any extensions for HSM support, ... should be put here
 *
 *
 */
@Service
public class DCKeyStoreService {

    public KeyStore loadKeyStore(StoreConfigurationProperties storeConfigurationProperties) {
        return storeConfigurationProperties.loadKeyStore();
    }

    public void saveKeyStore(StoreConfigurationProperties storeConfigurationProperties, KeyStore keyStore) {
        throw new UnsupportedOperationException(); //not yet supported
    }

}
