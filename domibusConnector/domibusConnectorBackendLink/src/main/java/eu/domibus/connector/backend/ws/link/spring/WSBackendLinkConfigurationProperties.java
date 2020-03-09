
package eu.domibus.connector.backend.ws.link.spring;

import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.KeyConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Properties;

import static eu.domibus.connector.backend.ws.link.spring.WSBackendLinkContextConfiguration.WS_BACKEND_LINK_PROFILE;

/**
 * Type safe method to declare spring property values
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
@Profile(WS_BACKEND_LINK_PROFILE)
@ConfigurationProperties(prefix = WSBackendLinkConfigurationProperties.PREFIX)
@Validated
public class WSBackendLinkConfigurationProperties {

    public static final String PREFIX = "connector.backend.ws";

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
    @Valid
    @NestedConfigurationProperty
    @NotNull
    private Resource wsPolicy = new ClassPathResource("/wsdl/backend.policy.xml");

    /**
     * Configuration of the key store which is used to sign the transferred soap-messages and
     * decrypt the from the backendClient received messages
     */
    @Valid
    @NestedConfigurationProperty
    @NotNull
    private KeyAndKeyStoreConfigurationProperties key;

    /**
     * Trust store which is used to verify the from the backendClient signed messages
     */
    @Valid
    @NestedConfigurationProperty
    @NotNull
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
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.password", this.getKey().getKeyStore().getPassword());
        LOGGER.debug("setting [org.apache.wss4j.crypto.merlin.keystore.file={}]", this.getKey().getKeyStore().getPath());
        try {
            p.setProperty("org.apache.wss4j.crypto.merlin.keystore.file", this.getKey().getKeyStore().getPathUrlAsString());
        } catch (Exception e) {
            throw new RuntimeException("Error with property: [" + PREFIX + ".key.store.path]\n" +
                    "value is [" + this.getKey().getKeyStore().getPath() + "]");
        }
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.alias", this.getKey().getPrivateKey().getAlias());
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.private.password", this.getKey().getPrivateKey().getPassword());
        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.password", this.getTrust().getTrustStore().getPassword());
        try {
            LOGGER.debug("setting [org.apache.wss4j.crypto.merlin.truststore.file={}]", this.getTrust().getTrustStore().getPath());
            p.setProperty("org.apache.wss4j.crypto.merlin.truststore.file", this.getTrust().getTrustStore().getPathUrlAsString());
        } catch (Exception e) {
            LOGGER.info("Trust Store Property: [" + PREFIX + ".trust.store.path]" +
                    "\n cannot be processed. Using the configured key store [{}] as trust store",
                    p.getProperty("org.apache.wss4j.crypto.merlin.keystore.file"));

            p.setProperty("org.apache.wss4j.crypto.merlin.truststore.file", p.getProperty("org.apache.wss4j.crypto.merlin.keystore.file"));
            p.setProperty("org.apache.wss4j.crypto.merlin.truststore.password", p.getProperty("org.apache.wss4j.crypto.merlin.keystore.password"));
        }
        p.setProperty("org.apache.wss4j.crypto.merlin.load.cacerts", Boolean.toString(this.getTrust().isLoadCaCerts()));

        return p;
    }

    public static class CertAndStoreConfigurationProperties {

        public CertAndStoreConfigurationProperties() {}

        public CertAndStoreConfigurationProperties(StoreConfigurationProperties keyStore) {
            this.trustStore = keyStore;
        }

        /**
         * Configuration of the (Key/Certificate)Store
         */
        @NestedConfigurationProperty
        private StoreConfigurationProperties trustStore;

        /**
         * Load system Ca Certs? (default false).
         *
         * Whether or not to load the CA certs in ${java.home}/lib/security/cacerts (default is false)
         */
        private boolean loadCaCerts = false;

        public void setLoadCaCerts(boolean loadCaCerts) {
            this.loadCaCerts = loadCaCerts;
        }

        public boolean isLoadCaCerts() {
            return loadCaCerts;
        }

        public StoreConfigurationProperties getTrustStore() {
            return trustStore;
        }

        public void setTrustStore(StoreConfigurationProperties trustStore) {
            this.trustStore = trustStore;
        }
    }


}
