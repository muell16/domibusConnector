package eu.domibus.connector.controller.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

/**
 * This configuration properties are for defining the connector to connector
 * test action and service
 *
 * If the connector receives a message with the configured service and action
 * the message will not be delivered to the gateway
 *
 *
 */
@ConfigurationProperties(prefix="connector.test")
@Component
public class ConnectorTestConfigurationProperties {

    @NotBlank
    private String service = "testService";

    @NotBlank
    private String action = "testAction";

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
