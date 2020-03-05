package eu.domibus.connector.web.configuration;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
//@ConfigurationProperties(prefix = SecurityToolkitConfigurationProperties.CONFIG_PREFIX)
@PropertySource({"classpath:/eu/domibus/connector/web/spring/web-default-configuration.properties", "classpath:/build-info.properties"})
//@Validated
public class WebConfigurationProperties {

	public WebConfigurationProperties() {
		// TODO Auto-generated constructor stub
	}

}
