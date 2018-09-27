
package eu.domibus.connector.gateway.link.spring;

import eu.domibus.connector.configuration.annotation.ConfigurationDescription;
import eu.domibus.connector.configuration.annotation.ConfigurationLabel;
import eu.domibus.connector.lib.spring.configuration.KeyConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * The Gateway Link Webservice Based Properties
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component("GatewayLinkWsServiceProperties")
@Profile("gwlink-ws")
@ConfigurationProperties(prefix = "connector.gatewaylink.ws")
@Validated
@Valid
public class GatewayLinkWsServiceProperties {

    /**
     * Defines the URL for submitting messages to the Gateway
     */
    @NotNull
    private String submissionEndpointAddress;

    /**
     * defines the address for the deliver message service.
     *
     * The service url is defined relative to the url of the CXF-Servlet (usually configured under /service),
     * the defined url is appended. So the default url for pushing messages from domibus gateway to connector
     * will be ${SERVER_CONTEXT}/service/delivermessage
     *
     */
    @NotNull
    private String address = "/domibusConnectorDeliveryWebservice";
    
//    private String name = "DeliverMessage";

    /**
     * SSL Key Store configuration
     *
     * The SSL-Key Store holds the path to the keyStore and the keyStore password to access the private-key which is needed to establish the TLS connection
     * to the Gateway. The private key is used to authenticate against the Gateway.
     */
    @Valid
    @NestedConfigurationProperty
    @ConfigurationDescription("Configures the key store which contains the private key which is used to authenticate on the gateway")
    @NotNull
    private StoreConfigurationProperties tlsKeyStore; // = new StoreConfigurationProperties(new ClassPathResource("/keys/ojStore.jks"), "ecodex");

    /**
     * The tlsKey configuration holds the key alias and the key password
     *
     * The tlsKey is located in the tlsKeyStore and is used to authenticate against the Gateway
     */
    @Valid
    @NestedConfigurationProperty
    @ConfigurationDescription("The private key for authenticating via certificate on the gateway (2way-ssl)")
    @NotNull
    private KeyConfigurationProperties tlsKey = new KeyConfigurationProperties();

    /**
     * SSL Trust Store configuration
     *
     * The SSL-Trust-Store holds the path to the trustStore and the trustStorePassword to access the public key(s) of the HTTPS-servers (Gateway) which should
     * be trusted.
     *
     */
    @Valid
    @NestedConfigurationProperty
    @ConfigurationLabel("TLS Trust Store Configuration")
    @ConfigurationDescription("This defines the tls trust store which is used to define the trusted server certificates for connecting to the gateway over https/tls")
    @NotNull
    private StoreConfigurationProperties tlsTrustStore = new StoreConfigurationProperties(new ClassPathResource("/keys/ojStore.jks"), "ecodex");


    @Valid
    @NestedConfigurationProperty
    @ConfigurationLabel("Trust Store for CXF Message validation")
    @NotNull
    private StoreConfigurationProperties cxfTrustStore;

    @Valid
    @NestedConfigurationProperty
    @ConfigurationDescription("Key-store which contains the private key for signing sent and decrypting received ws messages")
    @NotNull
    private StoreConfigurationProperties cxfKeyStore;


    @Valid
    @NestedConfigurationProperty
    @ConfigurationDescription("Private key for signing sent ws messages and decrypting received ws messages")
    @NotNull
    KeyConfigurationProperties cxfPrivateKey;


    @Valid
    @NestedConfigurationProperty
    @NotNull
    @ConfigurationLabel("WS Policy for GW <-> Connector Link")
    @ConfigurationDescription("This Property is used to define the location of the ws policy which is used for communication with the gateway")
    private Resource wsPolicy = new ClassPathResource("/wsdl/backend.policy.xml");

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

    public KeyConfigurationProperties getTlsKey() {
        return tlsKey;
    }

    public void setTlsKey(KeyConfigurationProperties tlsKey) {
        this.tlsKey = tlsKey;
    }

    public StoreConfigurationProperties getCxfTrustStore() {
        return cxfTrustStore;
    }

    public void setCxfTrustStore(StoreConfigurationProperties cxfTrustStore) {
        this.cxfTrustStore = cxfTrustStore;
    }

    public StoreConfigurationProperties getCxfKeyStore() {
        return cxfKeyStore;
    }

    public void setCxfKeyStore(StoreConfigurationProperties cxfKeyStore) {
        this.cxfKeyStore = cxfKeyStore;
    }

    public KeyConfigurationProperties getCxfPrivateKey() {
        return cxfPrivateKey;
    }

    public void setCxfPrivateKey(KeyConfigurationProperties cxfPrivateKey) {
        this.cxfPrivateKey = cxfPrivateKey;
    }

    public Resource getWsPolicy() {
        return wsPolicy;
    }

    public void setWsPolicy(Resource wsPolicy) {
        this.wsPolicy = wsPolicy;
    }
}
