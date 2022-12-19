package eu.domibus.connector.link.common;

import eu.domibus.connector.common.service.DCKeyStoreService;
import eu.domibus.connector.lib.spring.configuration.CxfTrustKeyStoreConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreAndTrustStoreConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MerlinPropertiesFactory {

    private final static Logger LOGGER = LogManager.getLogger(MerlinPropertiesFactory.class);

//    private final DCKeyStoreService dcKeyStoreService;
//
//    public MerlinPropertiesFactory(DCKeyStoreService dcKeyStoreService) {
//        this.dcKeyStoreService = dcKeyStoreService;
//    }

    //    public Map<String, Object> mapCertAndStoreConfigPropertiesToMerlinProperties(KeyAndKeyStoreAndTrustStoreConfigurationProperties config, String prefix);
//    CxfTrustKeyStoreConfigurationProperties

    /**
     * Maps the own configured properties to the crypto Properties
     * also see https://ws.apache.org/wss4j/config.html
     *
     * @return the wss Properties
     */
    public Properties mapCertAndStoreConfigPropertiesToMerlinProperties(KeyAndKeyStoreAndTrustStoreConfigurationProperties config, String prefix) {
        if (config == null) {
            throw new IllegalArgumentException(prefix + ".config.* properties are missing!");
        }
        StoreConfigurationProperties keyStore = config.getKeyStore();
        if (keyStore == null) {
            throw new IllegalArgumentException(prefix + ".config.key-store.* properties are missing or wrong!");
        }

//        HashMap<String, Object> p = new HashMap<>();
        Properties p = new Properties();
        p.put("org.apache.wss4j.crypto.provider", "org.apache.wss4j.common.crypto.Merlin");
        p.put("org.apache.wss4j.crypto.merlin.keystore.type", keyStore.getType());
        p.put("org.apache.wss4j.crypto.merlin.keystore.password", keyStore.getPassword());

        String propertyName = "org.apache.wss4j.crypto.merlin.keystore.file";
        String keyStorePath = keyStore.getPath();
        setPath(prefix + ".config.key-store.path", keyStore, p, propertyName, keyStorePath);

        p.put("org.apache.wss4j.crypto.merlin.keystore.alias", config.getPrivateKey().getAlias());
        p.put("org.apache.wss4j.crypto.merlin.keystore.private.password", config.getPrivateKey().getPassword());


        StoreConfigurationProperties trustStore = config.getTrustStore();
        p.put("org.apache.wss4j.crypto.merlin.truststore.type", trustStore.getType());
        p.put("org.apache.wss4j.crypto.merlin.truststore.password", trustStore.getPassword());

        String trustStorePathPropertyName = "org.apache.wss4j.crypto.merlin.truststore.file";
        String trustStorePath = trustStore.getPath();
        setPath(prefix + "config.trust-store.path", keyStore, p, trustStorePathPropertyName, trustStorePath);

        if (config instanceof CxfTrustKeyStoreConfigurationProperties) {
            CxfTrustKeyStoreConfigurationProperties cxfConfig = (CxfTrustKeyStoreConfigurationProperties) config;
            p.put("org.apache.wss4j.crypto.merlin.load.cacerts", Boolean.toString(cxfConfig.isLoadCaCerts()));
            p.put("security.encryption.username", cxfConfig.getEncryptAlias());
        }

        return p;
    }

    private static void setPath(String prefix, StoreConfigurationProperties keyStore, Properties p, String propertyName, String keyStorePath) {
        try {
            if (keyStorePath.startsWith("classpath:")) {
                keyStorePath = keyStorePath.substring("classpath:".length());
            }
            LOGGER.debug("setting [{}}={}]", propertyName, keyStore.getPath());
            p.put(propertyName, keyStorePath);
        } catch (Exception e) {
            throw new RuntimeException("Error with property: [" + prefix + "]\n" +
                    "value is [" + keyStore.getPath() + "]");
        }
    }

}
