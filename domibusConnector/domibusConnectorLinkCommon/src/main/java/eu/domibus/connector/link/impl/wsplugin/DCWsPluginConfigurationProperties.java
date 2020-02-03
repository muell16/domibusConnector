package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreAndTrustStoreConfigurationProperties;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Properties;


public abstract class DCWsPluginConfigurationProperties {

    private static Logger getLogger() {
      return LogManager.getLogger(DCWsPluginConfigurationProperties.class);
    }


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


    @ConfigurationLabel("Key- and Truststore configuration for soap level")
    @ConfigurationDescription("Configuration of the key store which is used to sign the transferred soap-messages and\n" +
        "decrypt the from the backendClient received messages")
    @Valid
    @NestedConfigurationProperty
    @NotNull
    private CertAndStoreConfigurationProperties soap;



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

    public CertAndStoreConfigurationProperties getSoap() {
        return soap;
    }

    public void setSoap(CertAndStoreConfigurationProperties soap) {
        this.soap = soap;
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
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.type", this.getSoap().getKeyStore().getType());
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.password", this.getSoap().getKeyStore().getPassword());
        getLogger().debug("setting [org.apache.wss4j.crypto.merlin.keystore.file={}]", this.getSoap().getKeyStore().getPath());
        try {
            p.setProperty("org.apache.wss4j.crypto.merlin.keystore.file", this.getSoap().getKeyStore().getPathUrlAsString());
        } catch (Exception e) {
            throw new RuntimeException("Error with property: [" + getPrefix() + ".soap.key-store.path]\n" +
                    "value is [" + this.getSoap().getKeyStore().getPath() + "]");
        }
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.alias", this.getSoap().getPrivateKey().getAlias());
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.private.password", this.getSoap().getPrivateKey().getPassword());


        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.type", this.getSoap().getTrustStore().getType());
        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.password", this.getSoap().getTrustStore().getPassword());
        try {
            getLogger().debug("setting [org.apache.wss4j.crypto.merlin.truststore.file={}]", this.getSoap().getTrustStore().getPath());
            p.setProperty("org.apache.wss4j.crypto.merlin.truststore.file", this.getSoap().getTrustStore().getPathUrlAsString());
        } catch (Exception e) {
            getLogger().info("Trust Store Property: [" + getPrefix() + ".soap.trust-store.path]" +
                            "\n cannot be processed. Using the configured key store [{}] as trust store",
                    p.getProperty("org.apache.wss4j.crypto.merlin.keystore.file"));
        }
        p.setProperty("org.apache.wss4j.crypto.merlin.load.cacerts", Boolean.toString(this.getSoap().isLoadCaCerts()));

        return p;
    }

    private String getPrefix() {
        return DCWsPluginConfiguration.DC_WS_PLUGIN_PROFILE_NAME;
    }



    public static class CertAndStoreConfigurationProperties extends KeyAndKeyStoreAndTrustStoreConfigurationProperties {

        @ConfigurationLabel("Should the default java truststore also be taken into account?")
        @ConfigurationDescription("Is mapped on org.apache.wss4j.crypto.merlin.load.cacerts")
        boolean loadCaCerts = false;

        public boolean isLoadCaCerts() {
            return loadCaCerts;
        }

        public void setLoadCaCerts(boolean loadCaCerts) {
            this.loadCaCerts = loadCaCerts;
        }
    }

}


