package eu.ecodex.dc5.payload;

import eu.ecodex.dc5.payload.model.PDomibusConnectorBigData;
import eu.ecodex.dc5.payload.provider.jpa.DomibusConnectorBigDataDao;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = DomibusConnectorBigDataDao.class)
@EntityScan(basePackageClasses = PDomibusConnectorBigData.class)
public class PayloadConfiguration {}