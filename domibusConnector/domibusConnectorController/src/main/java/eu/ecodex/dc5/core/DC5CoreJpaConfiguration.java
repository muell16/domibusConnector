package eu.ecodex.dc5.core;


import eu.ecodex.dc5.core.model.DC5CoreEntities;
import eu.ecodex.dc5.core.model.DC5PersistenceSettings;
import eu.ecodex.dc5.core.repository.DC5CoreRepositories;
import eu.ecodex.dc5.message.repo.DC5EbmsRepo;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackageClasses = DC5CoreRepositories.class)
@EntityScan(basePackageClasses = DC5CoreEntities.class)
@Configuration
public class DC5CoreJpaConfiguration {

}
