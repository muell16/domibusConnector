package eu.ecodex.dc5.domain;


import eu.ecodex.dc5.domain.repo.DC5BusinessDomainJpaEntity;
import eu.ecodex.dc5.domain.repo.DomibusConnectorBusinessDomainDao;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = DomibusConnectorBusinessDomainDao.class)
@EntityScan(basePackageClasses = DC5BusinessDomainJpaEntity.class)
public class DC5DomainConfiguration {
}
