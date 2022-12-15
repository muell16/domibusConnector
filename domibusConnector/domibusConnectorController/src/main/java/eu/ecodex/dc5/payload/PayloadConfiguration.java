package eu.ecodex.dc5.payload;

import eu.ecodex.dc5.payload.provider.jpa.DomibusConnectorBigDataDao;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = DomibusConnectorBigDataDao.class)
public class PayloadConfiguration {
}
