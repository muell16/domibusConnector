package eu.ecodex.dc.core.configuration;


import eu.ecodex.dc.core.repository.DC5EbmsRepo;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackageClasses = DC5EbmsRepo.class)
public class DC5JpaConfiguration {

}
