package eu.domibus.connector.starter;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
@SpringBootApplication(scanBasePackages = "eu.domibus.connector")
public class ConnectorStarter {
    
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplicationBuilder()        
                .sources(ConnectorStarter.class)        
                .profiles("connector", "mysql")      
                .build();
        
        springApplication.run(args);
        
    }
    
    
}
