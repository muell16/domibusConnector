package eu.ecodex.dc5.events;

import eu.ecodex.dc5.events.model.JPAEventStorageItem;
import eu.ecodex.dc5.events.repo.DC5EventStorageItemRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = DC5EventStorageItemRepository.class)
@EntityScan(basePackageClasses = JPAEventStorageItem.class)
public class EventConfiguration {
}
