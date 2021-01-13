package eu.domibus.connector.link.common;

import eu.domibus.connector.lib.spring.configuration.CxfTrustKeyStoreConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreAndTrustStoreConfigurationProperties;
import eu.domibus.connector.link.impl.wsplugin.DCWsPluginConfigurationProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class MerlinPropertiesFactory {

    private final static Logger LOGGER = LogManager.getLogger(MerlinPropertiesFactory.class);


//    public Map<String, Object> mapCertAndStoreConfigPropertiesToMerlinProperties(KeyAndKeyStoreAndTrustStoreConfigurationProperties config, String prefix);
//    CxfTrustKeyStoreConfigurationProperties

    /**
     * Maps the own configured properties to the crypto Properties
     * also see https://ws.apache.org/wss4j/config.html
     *
     * @return the wss Properties
     */
    public Map<String, Object> mapCertAndStoreConfigPropertiesToMerlinProperties(KeyAndKeyStoreAndTrustStoreConfigurationProperties config, String prefix) {
        if (config == null) {
            throw new IllegalArgumentException(prefix + ".config.* properties are missing!");
        }
        if (config.getKeyStore() == null) {
            throw new IllegalArgumentException(prefix + ".config.key-store.* properties are missing!");
        }

        HashMap<String, Object> p = new HashMap<>();
        p.put("org.apache.wss4j.crypto.provider", "org.apache.wss4j.common.crypto.Merlin");
        p.put("org.apache.wss4j.crypto.merlin.keystore.type", config.getKeyStore().getType());
        p.put("org.apache.wss4j.crypto.merlin.keystore.password", config.getKeyStore().getPassword());
        LOGGER.debug("setting [org.apache.wss4j.crypto.merlin.keystore.file={}]", config.getKeyStore().getPath());
        try {
            p.put("org.apache.wss4j.crypto.merlin.keystore.file", config.getKeyStore().getPathUrlAsString());
        } catch (Exception e) {
            throw new RuntimeException("Error with property: [" + prefix + ".config.key-store.path]\n" +
                    "value is [" + config.getKeyStore().getPath() + "]");
        }
        p.put("org.apache.wss4j.crypto.merlin.keystore.alias", config.getPrivateKey().getAlias());
        p.put("org.apache.wss4j.crypto.merlin.keystore.private.password", config.getPrivateKey().getPassword());


        p.put("org.apache.wss4j.crypto.merlin.truststore.type", config.getTrustStore().getType());
        p.put("org.apache.wss4j.crypto.merlin.truststore.password", config.getTrustStore().getPassword());
        try {
            LOGGER.debug("setting [org.apache.wss4j.crypto.merlin.truststore.file={}]", config.getTrustStore().getPath());
            p.put("org.apache.wss4j.crypto.merlin.truststore.file", config.getTrustStore().getPathUrlAsString());
        } catch (Exception e) {
            LOGGER.info("Trust Store Property: [" + prefix + ".config.trust-store.path]" +
                            "\n cannot be processed. Using the configured key store [{}] as trust store",
                    p.get("org.apache.wss4j.crypto.merlin.keystore.file"));
        }

        if (config instanceof CxfTrustKeyStoreConfigurationProperties) {
            CxfTrustKeyStoreConfigurationProperties cxfConfig = (CxfTrustKeyStoreConfigurationProperties) config;
            p.put("org.apache.wss4j.crypto.merlin.load.cacerts", Boolean.toString(cxfConfig.isLoadCaCerts()));
            p.put("security.encryption.username", cxfConfig.getEncryptAlias());
        }

        return p;
    }

}
