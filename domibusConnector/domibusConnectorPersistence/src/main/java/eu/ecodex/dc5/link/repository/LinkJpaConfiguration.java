package eu.ecodex.dc5.link.repository;

import eu.ecodex.dc5.link.model.DC5LinkConfigJpaEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = DomibusConnectorLinkPartnerDao.class)
@EntityScan(basePackageClasses = DC5LinkConfigJpaEntity.class)
public class LinkJpaConfiguration {
}
