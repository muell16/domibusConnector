
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

import java.io.IOException;
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


    @NestedConfigurationProperty
    private CertAndStoreConfigurationProperties wss;

    public String getBackendPublishAddress() {
        return backendPublishAddress;
    }

    public void setBackendPublishAddress(String backendPublishAddress) {
        this.backendPublishAddress = backendPublishAddress;
    }


    public Properties getEncryptionProperties() throws IOException {
        Properties p = mapCertAndStoreConfigPropertiesToMerlinProperties(this.wss);
        LOGGER.debug("getEncryptionProperties() are: [{}]", p);
        return p;
    }


    public Resource getWsPolicy() {
        return wsPolicy;
    }

    public void setWsPolicy(Resource wsPolicy) {
        this.wsPolicy = wsPolicy;
    }

    public Properties getWssProperties() {
        Properties p = mapCertAndStoreConfigPropertiesToMerlinProperties(this.wss);
        LOGGER.debug("getSignatureProperties() are: [{}]", p);
        return p;
    }

    /**
     * Maps the own configured properties to the crypto Properties
     *  also see https://ws.apache.org/wss4j/config.html
     * @param c - the Properties
     * @return the wss Properties
     */
    //using same store for trust and keystore
    public Properties mapCertAndStoreConfigPropertiesToMerlinProperties(CertAndStoreConfigurationProperties c) {
        Properties p = new Properties();
        p.setProperty("org.apache.wss4j.crypto.provider", "org.apache.wss4j.common.crypto.Merlin");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.type", "jks");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.password", this.wss.getStore().getPassword());
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.file", this.wss.getStore().getPathUrlAsString());
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.alias", this.wss.getCert().getAlias());
        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.password", this.wss.getStore().getPassword());
        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.file", this.wss.getStore().getPathUrlAsString());
        p.setProperty("org.apache.wss4j.crypto.merlin.load.cacerts", Boolean.toString(this.wss.isLoadCaCerts()));

        return p;
    }


    public CertAndStoreConfigurationProperties getWss() {
        return wss;
    }

    public void setWss(CertAndStoreConfigurationProperties wss) {
        this.wss = wss;
    }


    public static class CertAndStoreConfigurationProperties {

        public CertAndStoreConfigurationProperties() {}

        public CertAndStoreConfigurationProperties(StoreConfigurationProperties keyStore, CertConfigurationProperties cert) {
            this.store = keyStore;
            this.cert = cert;
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
        private CertConfigurationProperties cert;

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

        public CertConfigurationProperties getCert() {
            return cert;
        }

        public void setCert(CertConfigurationProperties cert) {
            this.cert = cert;
        }

        public void setLoadCaCerts(boolean loadCaCerts) {
            this.loadCaCerts = loadCaCerts;
        }

        public boolean isLoadCaCerts() {
            return loadCaCerts;
        }
    }
}
