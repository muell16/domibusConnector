package eu.ecodex.dc5.process.model;


import eu.ecodex.dc5.process.repository.DC5MsgProcessRepo;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackageClasses = DC5MsgProcess.class)
@EnableJpaRepositories(basePackageClasses = DC5MsgProcessRepo.class)
public class ProcessModelConfiguration {

}
