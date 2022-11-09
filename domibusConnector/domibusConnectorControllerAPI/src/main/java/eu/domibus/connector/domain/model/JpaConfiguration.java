package eu.domibus.connector.domain.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = JpaConfiguration.class)
@EntityScan(basePackageClasses = JpaConfiguration.class)
public class JpaConfiguration {
}
