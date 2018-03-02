
package eu.domibus.connector.backend.ws.link.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Type safe method to declare spring property values
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
@ConfigurationProperties(prefix = "connector.backend.ws")
public class WSBackendLinkConfigurationProperties {

    String backendPublishAddress = "/backend";

    Resource encryptionProperties = new ClassPathResource("/eu/domibus/connector/backend/config/decrypt.properties");

    Resource wsPolicy = new ClassPathResource("/wsdl/backend.policy.xml");

    public String getBackendPublishAddress() {
        return backendPublishAddress;
    }

    public void setBackendPublishAddress(String backendPublishAddress) {
        this.backendPublishAddress = backendPublishAddress;
    }

    public Resource getEncryptionProperties() {
        return encryptionProperties;
    }

    public void setEncryptionProperties(Resource encryptionProperties) {
        this.encryptionProperties = encryptionProperties;
    }

    public Resource getWsPolicy() {
        return wsPolicy;
    }

    public void setWsPolicy(Resource wsPolicy) {
        this.wsPolicy = wsPolicy;
    }
}
