package eu.ecodex.dc5.util.repository;

import eu.ecodex.dc5.util.model.DC5UserPassword;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = DC5UserDao.class)
@EntityScan(basePackageClasses = DC5UserPassword.class)
public class UtilJpaConfiguration {
}
