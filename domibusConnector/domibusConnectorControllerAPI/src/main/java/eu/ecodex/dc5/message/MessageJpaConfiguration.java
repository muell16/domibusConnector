package eu.ecodex.dc5.message;


import eu.ecodex.dc5.message.model.DC5Message;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EntityScan(basePackageClasses = DC5Message.class)
public class MessageJpaConfiguration {
}
