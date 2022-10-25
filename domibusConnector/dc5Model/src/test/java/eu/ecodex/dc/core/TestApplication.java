package eu.ecodex.dc.core;

import eu.ecodex.dc.core.repository.DC5EbmsRepo;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = DC5EbmsRepo.class)
public class TestApplication {
}
