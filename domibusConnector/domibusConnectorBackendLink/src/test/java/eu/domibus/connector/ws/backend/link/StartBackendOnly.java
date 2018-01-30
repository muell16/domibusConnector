
package eu.domibus.connector.ws.backend.link;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@SpringBootApplication(
    scanBasePackages="eu.domibus.connector.ws.backend.link", 
    exclude = {
        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class}
)  
public class StartBackendOnly {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplicationBuilder()
                .sources(StartBackendOnly.class)
                .build();
        
        springApplication.run(args);
    }
    
    
}
