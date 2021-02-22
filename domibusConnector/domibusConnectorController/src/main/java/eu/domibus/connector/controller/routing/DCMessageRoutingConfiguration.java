package eu.domibus.connector.controller.routing;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(prefix = DCMessageRoutingConfigurationProperties.ROUTING_CONFIG_PREFIX,
        name = "enabled", havingValue = "true", matchIfMissing = true)
@Configuration
@EnableConfigurationProperties(DCMessageRoutingConfigurationProperties.class)
public class DCMessageRoutingConfiguration {




}
