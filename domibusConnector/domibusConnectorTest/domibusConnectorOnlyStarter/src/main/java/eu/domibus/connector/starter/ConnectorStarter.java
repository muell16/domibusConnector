package eu.domibus.connector.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
@SpringBootApplication(scanBasePackages = "eu.domibus.connector")
public class ConnectorStarter {
    
    public static void main(String[] args) {
        runSpringApplication(args);
    }

    public static ConfigurableApplicationContext runSpringApplication(String[] args) {
        SpringApplication springApplication = new SpringApplicationBuilder()
                .sources(ConnectorStarter.class)
                .profiles("connector",
                        "embedded",     //use embedded database
                        "gwlink-ws", //use gw webservice based impl
                        "backendlink-ws"
                )
                .build();

        ConfigurableApplicationContext appContext = springApplication.run(args);
        return appContext;
    }


}
