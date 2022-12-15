package eu.ecodex.dc5.transport;

import eu.ecodex.dc5.transport.model.DC5TransportRequest;
import eu.ecodex.dc5.transport.repo.DC5TransportRequestRepo;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = DC5TransportRequestRepo.class)
@EntityScan(basePackageClasses = DC5TransportRequest.class)
public class TransportConfiguration {
}
