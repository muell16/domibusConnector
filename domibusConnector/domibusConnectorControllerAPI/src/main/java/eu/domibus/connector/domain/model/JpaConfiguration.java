package eu.domibus.connector.domain.model;

import eu.domibus.connector.domain.model.repo.DomibusConnectorMessageRepo;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = DomibusConnectorMessageRepo.class)
@EntityScan(basePackageClasses = JpaConfiguration.class)
public class JpaConfiguration {
}
