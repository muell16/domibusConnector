package eu.domibus.connector.domain.model;

import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
//@EnableJpaRepositories(basePackageClasses = DC5MessageRepo.class)
//@EntityScan(basePackageClasses = JpaConfiguration.class)
public class JpaConfiguration {
}
