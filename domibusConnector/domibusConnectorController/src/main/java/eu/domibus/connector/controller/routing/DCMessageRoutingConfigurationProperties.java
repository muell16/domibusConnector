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

    @Data
    public static class RoutingRule {

        @NotBlank
        private String backendName;

        @NotBlank
        private String matchClause;

    }

}
