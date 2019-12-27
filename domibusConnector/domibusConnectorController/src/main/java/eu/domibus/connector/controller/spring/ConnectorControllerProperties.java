package eu.domibus.connector.controller.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

@ConfigurationProperties(prefix = "connector.controller")
public class ConnectorControllerProperties {

    /**
     * a random instance name...
     */
    private String instanceName = UUID.randomUUID().toString().substring(0,6);

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
