package eu.domibus.connector.common;

import eu.domibus.connector.common.configuration.ConnectorConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ConnectorConfigurationProperties.class})
public class CommonConfiguration {
}
