package eu.domibus.connector.controller.routing;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = DCMessageRoutingConfigurationProperties.ROUTING_CONFIG_PREFIX)
@Data
public class DCMessageRoutingConfigurationProperties {

    public static final String ROUTING_CONFIG_PREFIX = "connector.routing";

    boolean enabled = true;

    private List<RoutingRule> rules = new ArrayList<>();

    @NotBlank
    private String defaultBackendName;

    /**
     * Backend name of the connector itself,
     * is used for connector2connector tests,
     * when the connector itself acts as a backend
     * see {@link eu.domibus.connector.controller.processor.steps.Connector2ConnectorTestStep}
     */
    @NotBlank
    private String connectorBackendName = "connectorBackend";

    /**
     * Gateway name of the connector itself,
     * is used for backend2backend tests,
     * when the connector itself acts as a gateway
     *
     *
     *
     * NOT IMPLEMENTED YET!
     */
    @NotBlank
    private String connectorGatewayName = "connectorGateway";

    @Data
    public static class RoutingRule {

        @NotBlank
        private String backendName;

        @NotBlank
        private RoutingRulePattern matchClause;

    }

}
