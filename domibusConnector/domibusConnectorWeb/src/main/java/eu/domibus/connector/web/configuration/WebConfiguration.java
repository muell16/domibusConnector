package eu.domibus.connector.web.configuration;

import eu.domibus.connector.web.persistence.dao.WebPersistenceDaoPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Web module specific configuration
 */
@Configuration
@PropertySource({"classpath:/eu/domibus/connector/web/spring/web-default-configuration.properties", "classpath:/build-info.properties"})
public class WebConfiguration {



}
