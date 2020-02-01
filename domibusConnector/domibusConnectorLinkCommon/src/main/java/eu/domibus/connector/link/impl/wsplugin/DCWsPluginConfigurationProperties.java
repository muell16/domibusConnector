package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.lib.spring.configuration.CxfTrustKeyStoreConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import eu.domibus.connector.link.impl.gwjmsplugin.GwJmsPluginConfiguration;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Properties;


@Profile(DCWsPluginConfiguration.DC_WS_PLUGIN_PROFILE_NAME)
@ConfigurationProperties(prefix = DCWsPluginConfiguration.DC_WS_PLUGIN_PROFILE_NAME)
public class DCWsPluginConfigurationProperties {

    private static Logger getLogger() {
      return LogManager.getLogger(DCWsPluginConfigurationProperties.class);
    }

//    @ConfigurationLabel("Where should the cxf serlvet listen?")
//    @ConfigurationDescription("The mapping of the cxf servlet of the domibus connector web service plugin 'wsplugin'")
//    private String cxfServletAddress = "/wsplugin";


    /**
     * Specifies the address where the Backend WebService should be published
     * the path specefied here is added to the path of the CXF-Servlet
     * (which is per default configured as /service - this leads to the default URL of
     * "/services/backend"
     */
    @ConfigurationLabel("Where should the cxf endpoint be exposed")
    @ConfigurationDescription("Specifies the address where the Backend WebService should be published\n" +
            "the path specefied here is added to the path of the CXF-Servlet\n" +
            "(which is per default configured as /service - this leads to the default URL of\n" +
            "'/wsplugin/backend'")
    private String backendPublishAddress = "/backend";


    /**
     * This property configures the the path to the security policy which should be used for the
     * backend webservice
     * <p>
     * the default security policy requires signed and encrypted messages (body+header)
     * the signing and wss is done with certificates
     */
    @ConfigurationLabel("Web Security Policy")
    @ConfigurationDescription("The by this link impl used ws-security policy location")
    @Valid
    @NotNull
    private Resource wsPolicy = new ClassPathResource("/wsdl/backend.policy.xml");


    @ConfigurationLabel("Key and Keystore configuration")
    @ConfigurationDescription("Configuration of the key store which is used to sign the transferred soap-messages and\n" +
        "decrypt the from the backendClient received messages")
    @Valid
    @NestedConfigurationProperty
    @NotNull
    private KeyAndKeyStoreConfigurationProperties key;



    @ConfigurationDescription("Trust store which is used to verify the from the backendClient signed messages")
    @ConfigurationLabel("Trust and Truststore configuration")
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
        getLogger().debug("getSignatureProperties() are: [{}]", p);
        return p;
    }






    /**
     * Maps the own configured properties to the crypto Properties
     * also see https://ws.apache.org/wss4j/config.html
     *
     * @return the wss Properties
     */
    public Properties mapCertAndStoreConfigPropertiesToMerlinProperties() {

        Properties p = new Properties();
        p.setProperty("org.apache.wss4j.crypto.provider", "org.apache.wss4j.common.crypto.Merlin");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.type", this.getKey().getKeyStore().getType());
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.password", this.getKey().getKeyStore().getPassword());
        getLogger().debug("setting [org.apache.wss4j.crypto.merlin.keystore.file={}]", this.getKey().getKeyStore().getPath());
        try {
            p.setProperty("org.apache.wss4j.crypto.merlin.keystore.file", this.getKey().getKeyStore().getPathUrlAsString());
        } catch (Exception e) {
            throw new RuntimeException("Error with property: [" + getPrefix() + ".key.store.path]\n" +
                    "value is [" + this.getKey().getKeyStore().getPath() + "]");
        }
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.alias", this.getKey().getPrivateKey().getAlias());
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.private.password", this.getKey().getPrivateKey().getPassword());


        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.type", this.getTrust().getTrustStore().getType());
        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.password", this.getTrust().getTrustStore().getPassword());
        try {
            getLogger().debug("setting [org.apache.wss4j.crypto.merlin.truststore.file={}]", this.getTrust().getTrustStore().getPath());
            p.setProperty("org.apache.wss4j.crypto.merlin.truststore.file", this.getTrust().getTrustStore().getPathUrlAsString());
        } catch (Exception e) {
            getLogger().info("Trust Store Property: [" + getPrefix() + ".trust.store.path]" +
                            "\n cannot be processed. Using the configured key store [{}] as trust store",
                    p.getProperty("org.apache.wss4j.crypto.merlin.keystore.file"));
        }
        p.setProperty("org.apache.wss4j.crypto.merlin.load.cacerts", Boolean.toString(this.getTrust().isLoadCaCerts()));

        return p;
    }

    private String getPrefix() {
        return DCWsPluginConfiguration.DC_WS_PLUGIN_PROFILE_NAME;
    }



    public static class CertAndStoreConfigurationProperties {

        public CertAndStoreConfigurationProperties() {
        }

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
         * <p>
         * Whether or not to load the CA certs in ${java.home}/lib/security/cacerts (default is false)
         */
        @ConfigurationLabel("loadCaCerts - by default false")
        @ConfigurationDescription("Should the default java trust store also be taken into account?\n" +
                "For details look at the parameter 'org.apache.wss4j.crypto.merlin.load.cacerts' of cxf")
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


