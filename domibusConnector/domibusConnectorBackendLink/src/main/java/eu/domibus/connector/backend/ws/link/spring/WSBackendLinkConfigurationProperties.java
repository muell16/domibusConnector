
package eu.domibus.connector.backend.ws.link.spring;

import eu.domibus.connector.lib.spring.configuration.CertConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Type safe method to declare spring property values
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
@ConfigurationProperties(prefix = "connector.backend.ws")
public class WSBackendLinkConfigurationProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(WSBackendLinkConfigurationProperties.class);

    /**
     * Specifies the address where the Backend WebService should be published
     *  the path specefied here is added to the path of the CXF-Servlet
     *  (which is per default configured as /service - this leads to the default URL of
     *   "/services/backend"
     *
     *
     *
     */
    private String backendPublishAddress = "/backend";



    /**
     *  This property configures the the path to the security policy which should be used for the
     *  backend webservice
     *
     *  the default security policy requires signed and encrypted messages (body+header)
     *  the signing and wss is done with certificates
     *
     */
    @NestedConfigurationProperty
    private Resource wsPolicy = new ClassPathResource("/wsdl/backend.policy.xml");

    /**
     * Configuration of the key store which is used to sign the transferred soap-messages and
     * decrypt the from the backendClient received messages
     */
    @NestedConfigurationProperty
    private KeyAndKeyStoreConfigurationProperties key;

    /**
     * Trust store which is used to verify the from the backendClient signed messages
     */
    @NestedConfigurationProperty
    private CertAndStoreConfigurationProperties trust;

    public String getBackendPublishAddress() {
        return backendPublishAddress;
    }

    public void setBackendPublishAddress(String backendPublishAddress) {
        this.backendPublishAddress = backendPublishAddress;
    }

    public KeyAndKeyStoreConfigurationProperties getKey() {
        return key;
    }

    public void setKey(KeyAndKeyStoreConfigurationProperties key) {
        this.key = key;
    }

    public CertAndStoreConfigurationProperties getTrust() {
        return trust;
    }

    public void setTrust(CertAndStoreConfigurationProperties trust) {
        this.trust = trust;
    }

    public Resource getWsPolicy() {
        return wsPolicy;
    }

    public void setWsPolicy(Resource wsPolicy) {
        this.wsPolicy = wsPolicy;
    }

    public Properties getWssProperties() {
        Properties p = mapCertAndStoreConfigPropertiesToMerlinProperties();
        LOGGER.debug("getSignatureProperties() are: [{}]", p);
        return p;
    }

    /**
     * Maps the own configured properties to the crypto Properties
     *  also see https://ws.apache.org/wss4j/config.html
     * @return the wss Properties
     */
    public Properties mapCertAndStoreConfigPropertiesToMerlinProperties() {
        Properties p = new Properties();
        p.setProperty("org.apache.wss4j.crypto.provider", "org.apache.wss4j.common.crypto.Merlin");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.type", "jks");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.password", this.key.getStore().getPassword());
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.file", this.key.getStore().getPathUrlAsString());
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.alias", this.key.getKey().getAlias());
        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.password", this.trust.getStore().getPassword());
        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.file", this.trust.getStore().getPathUrlAsString());
        p.setProperty("org.apache.wss4j.crypto.merlin.load.cacerts", Boolean.toString(this.trust.isLoadCaCerts()));

        return p;
    }

    public static class KeyAndKeyStoreConfigurationProperties {
        public KeyAndKeyStoreConfigurationProperties() {}

        public KeyAndKeyStoreConfigurationProperties(StoreConfigurationProperties keyStore, CertConfigurationProperties key) {
            this.store = keyStore;
            this.key = key;
        }

        /**
         * Configuration of the (Key/Certificate)Store
         */
        @NestedConfigurationProperty
        private StoreConfigurationProperties store;

        /**
         * Configures the default alias to use
         */
        @NestedConfigurationProperty
        private CertConfigurationProperties key;

        public StoreConfigurationProperties getStore() {
            return store;
        }

        public void setStore(StoreConfigurationProperties store) {
            this.store = store;
        }

        public CertConfigurationProperties getKey() {
            return key;
        }

        public void setKey(CertConfigurationProperties key) {
            this.key = key;
        }

    }


    public static class CertAndStoreConfigurationProperties {

        public CertAndStoreConfigurationProperties() {}

        public CertAndStoreConfigurationProperties(StoreConfigurationProperties keyStore) {
            this.store = keyStore;
        }

        /**
         * Configuration of the (Key/Certificate)Store
         */
        @NestedConfigurationProperty
        private StoreConfigurationProperties store;

        /**
         * Load system Ca Certs? (default false).
         *
         * Whether or not to load the CA certs in ${java.home}/lib/security/cacerts (default is false)
         */
        private boolean loadCaCerts = false;

        public StoreConfigurationProperties getStore() {
            return store;
        }

        public void setStore(StoreConfigurationProperties store) {
            this.store = store;
        }

        public void setLoadCaCerts(boolean loadCaCerts) {
            this.loadCaCerts = loadCaCerts;
        }

        public boolean isLoadCaCerts() {
            return loadCaCerts;
        }
    }
}
