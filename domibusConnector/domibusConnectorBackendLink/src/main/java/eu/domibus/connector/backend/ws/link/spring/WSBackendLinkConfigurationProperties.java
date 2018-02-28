
package eu.domibus.connector.backend.ws.link.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Type safe method to declare spring property values
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
@ConfigurationProperties(prefix = "connector.backend.ws")
public class WSBackendLinkConfigurationProperties {

    String backendPublishAddress = "/backend";

    public String getBackendPublishAddress() {
        return backendPublishAddress;
    }

    public void setBackendPublishAddress(String backendPublishAddress) {
        this.backendPublishAddress = backendPublishAddress;
    }
    
    
    
}
