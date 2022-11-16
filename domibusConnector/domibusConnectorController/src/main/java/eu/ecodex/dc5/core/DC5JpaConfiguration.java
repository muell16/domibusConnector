package eu.ecodex.dc5.core;


import eu.ecodex.dc5.core.model.DC5PersistenceSettings;
import eu.ecodex.dc5.core.repository.DC5EbmsRepo;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackageClasses = DC5EbmsRepo.class)
@EntityScan(basePackageClasses = DC5PersistenceSettings.class)
@Configuration
public class DC5JpaConfiguration {

}
