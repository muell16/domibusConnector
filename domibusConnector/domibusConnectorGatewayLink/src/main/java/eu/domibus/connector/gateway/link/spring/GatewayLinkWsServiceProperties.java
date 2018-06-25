
package eu.domibus.connector.gateway.link.spring;

import eu.domibus.connector.lib.spring.configuration.CertConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * The Gateway Link Webservice Based Properties
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component("GatewayLinkWsServiceProperties")
@Profile("gwlink-ws")
@ConfigurationProperties(prefix = "connector.gatewaylink.ws")
public class GatewayLinkWsServiceProperties {

    /**
     * Defines the URL for submitting messages to the Gateway
     */
    private String submissionEndpointAddress;

    /**
     * defines the address for the deliver message service.
     *
     * The service url is defined relative to the url of the CXF-Servlet (usually configured under /service),
     * the defined url is appended. So the default url for pushing messages from domibus gateway to connector
     * will be ${SERVER_CONTEXT}/service/delivermessage
     *
     */
    private String address = "/domibusConnectorDeliveryWebservice";
    
//    private String name = "DeliverMessage";

    /**
     * SSL Key Store configuration
     *
     * The SSL-Key Store holds the path to the keyStore and the keyStore password to access the private-key which is needed to establish the TLS connection
     * to the Gateway. The private key is used to authenticate against the Gateway.
     */
    @NestedConfigurationProperty
    private StoreConfigurationProperties tlsKeyStore = new StoreConfigurationProperties(new ClassPathResource("/keys/ojStore.jks"), "ecodex");

    /**
     * The tlsKey configuration holds the key alias and the key password
     *
     * The tlsKey is located in the tlsKeyStore and is used to authenticate against the Gateway
     */
    @NestedConfigurationProperty
    private CertConfigurationProperties tlsKey = new CertConfigurationProperties();

    /**
     * SSL Trust Store configuration
     *
     * The SSL-Trust-Store holds the path to the trustStore and the trustStorePassword to access the public key(s) of the HTTPS-servers (Gateway) which should
     * be trusted.
     *
     */
    @NestedConfigurationProperty
    private StoreConfigurationProperties tlsTrustStore = new StoreConfigurationProperties(new ClassPathResource("/keys/ojStore.jks"), "ecodex");

    public String getSubmissionEndpointAddress() {
        return submissionEndpointAddress;
    }

    public void setSubmissionEndpointAddress(String submissionEndpointAddress) {
        this.submissionEndpointAddress = submissionEndpointAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public StoreConfigurationProperties getTlsKeyStore() {
        return tlsKeyStore;
    }

    public void setTlsKeyStore(StoreConfigurationProperties tlsKeyStore) {
        this.tlsKeyStore = tlsKeyStore;
    }

    public StoreConfigurationProperties getTlsTrustStore() {
        return tlsTrustStore;
    }

    public void setTlsTrustStore(StoreConfigurationProperties tlsTrustStore) {
        this.tlsTrustStore = tlsTrustStore;
    }

    public CertConfigurationProperties getTlsKey() {
        return tlsKey;
    }

    public void setTlsKey(CertConfigurationProperties tlsKey) {
        this.tlsKey = tlsKey;
    }
}
