
package eu.domibus.connector.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@SpringBootApplication(
    scanBasePackages={"eu.domibus.connector.ws.backend.link", "eu.domibus.connector.backend", "eu.domibus.connector.persistence"},
    exclude = {
//        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class
    }
)  

public class StartBackendOnly {

    public static void main(String[] args) {
        startUpSpringApplication(args);
    }
    
    public static ConfigurableApplicationContext startUpSpringApplication(String[] args) {
        SpringApplication springApplication = new SpringApplicationBuilder()
                .sources(StartBackendOnly.class)
                .build();
        
        ConfigurableApplicationContext run = springApplication.run(args);
        return run;
    }
    
    
}
