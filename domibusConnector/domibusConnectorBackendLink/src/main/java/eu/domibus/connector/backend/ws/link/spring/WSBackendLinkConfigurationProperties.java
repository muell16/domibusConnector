
package eu.domibus.connector.backend.ws.link.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * Type safe method to declare spring property values
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
@ConfigurationProperties(prefix = "connector.backend.ws")
public class WSBackendLinkConfigurationProperties {

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
     *  This property configures the the path to the encryptionPropertiesResource
     *  which are used by WSS to encrypt the over the webservice sent soap messages
     *
     */
    @NestedConfigurationProperty
    private Resource encryptionPropertiesResource = new ClassPathResource("/eu/domibus/connector/backend/config/decrypt.properties");

    /**
     *  This property configures the the path to the signaturePropertiesResource
     *  which are used by WSS to sign the over the webservice sent soap messages
     *
     */
    @NestedConfigurationProperty
    private Resource signaturePropertiesResource = new ClassPathResource("/eu/domibus/connector/backend/config/decrypt.properties");

    /**
     *  This property configures the the path to the security policy which should be used for the
     *  backend webservice
     *
     *  the default security policy requires signed and encrypted messages (body+header)
     *  the signing and encryption is done with certificates
     *
     */
    @NestedConfigurationProperty
    private Resource wsPolicy = new ClassPathResource("/wsdl/backend.policy.xml");

    /**
     * This property configures the name of the certificate which should be used to
     * sign the messages sent to the backend clients
     */
    private String connectorCertAlias = "connector";




    public String getBackendPublishAddress() {
        return backendPublishAddress;
    }

    public void setBackendPublishAddress(String backendPublishAddress) {
        this.backendPublishAddress = backendPublishAddress;
    }

    public Resource getEncryptionPropertiesResource() {
        return encryptionPropertiesResource;
    }

    public Properties getEncryptionProperties() throws IOException {
        Properties p = new Properties();
        p.load(getEncryptionPropertiesResource().getInputStream());
        return p;
    }

    public void setEncryptionPropertiesResource(Resource encryptionPropertiesResource) {
        this.encryptionPropertiesResource = encryptionPropertiesResource;
    }

    public Resource getWsPolicy() {
        return wsPolicy;
    }

    public void setWsPolicy(Resource wsPolicy) {
        this.wsPolicy = wsPolicy;
    }

    public Resource getSignaturePropertiesResource() {
        return signaturePropertiesResource;
    }

    public Properties getSignatureProperties() throws IOException {
        Properties p = new Properties();
        p.load(getSignaturePropertiesResource().getInputStream());
        return p;
    }

    public void setSignaturePropertiesResource(Resource signaturePropertiesResource) {
        this.signaturePropertiesResource = signaturePropertiesResource;
    }

    public String getConnectorCertAlias() {
        return connectorCertAlias;
    }

    public void setConnectorCertAlias(String connectorCertAlias) {
        this.connectorCertAlias = connectorCertAlias;
    }
}
