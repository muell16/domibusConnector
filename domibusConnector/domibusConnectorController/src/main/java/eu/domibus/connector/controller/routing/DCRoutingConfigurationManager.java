package eu.domibus.connector.controller.routing;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DCRoutingConfigurationManager {

    private final DCMessageRoutingConfigurationProperties routingConfigurationProperties;

    public DCRoutingConfigurationManager(DCMessageRoutingConfigurationProperties routingConfigurationProperties) {
        this.routingConfigurationProperties = routingConfigurationProperties;
    }

    public List<DCMessageRoutingConfigurationProperties.RoutingRule> getBackendRoutingRules() {
        return routingConfigurationProperties.getBackendRules();
    }

    public String getDefaultBackendName() {
        return routingConfigurationProperties.getDefaultBackendName();
    }

    public boolean isBackendRoutingEnabled() {
        return routingConfigurationProperties.isEnabled();
    }

}
