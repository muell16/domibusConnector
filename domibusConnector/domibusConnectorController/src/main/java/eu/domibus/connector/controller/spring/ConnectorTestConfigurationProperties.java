package eu.domibus.connector.controller.spring;

import eu.domibus.connector.controller.process.ToBackendBusinessMessageProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This configuration properties are for defining the connector to connector
 * test action and service
 *
 * If the connector receives a message with the configured service and action
 * the message will not be delivered to the gateway
 *
 * see {@link ToBackendBusinessMessageProcessor}
 */
@ConfigurationProperties(prefix="connector.test")
@Component
public class ConnectorTestConfigurationProperties {

    private String service = "";

    private String action = "";

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
