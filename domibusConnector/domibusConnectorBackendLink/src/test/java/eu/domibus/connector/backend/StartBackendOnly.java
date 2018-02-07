
package eu.domibus.connector.backend;

import eu.domibus.connector.controller.service.DomibusConnectorBackendSubmissionService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class StartBackendOnly {

    private final static Logger LOGGER = LoggerFactory.getLogger(StartBackendOnly.class);
    
    public static void main(String[] args) {
        startUpSpringApplication(new String[]{}, new String[]{});
    }
    
    public static ConfigurableApplicationContext startUpSpringApplication(String[] profiles, String[] properties) {
        SpringApplication springApplication = new SpringApplicationBuilder()
                .sources(TestBackendContext.class)
                .profiles(profiles)
                .profiles(properties)
                .build();
        
        ConfigurableApplicationContext run = springApplication.run();
        return run;
    }
    
    
}
