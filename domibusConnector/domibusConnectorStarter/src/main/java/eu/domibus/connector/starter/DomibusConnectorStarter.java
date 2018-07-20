package eu.domibus.connector.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "eu.domibus.connector")
@EnableTransactionManagement
@EnableAutoConfiguration
@PropertySource("classpath:/connector.properties")
public class DomibusConnectorStarter {

	public static void main(String[] args) {
        runSpringApplication(args);
    }

    public static ConfigurableApplicationContext runSpringApplication(String[] args) {
        SpringApplication springApplication = new SpringApplicationBuilder()
                .sources(DomibusConnectorStarter.class)
                .profiles("connector",
                        //"embedded",     //use embedded database
                        "gwlink-ws", //use gw webservice based impl
                        "backendlink-ws" //use backendlink ws based impl
                )
                .build();

        ConfigurableApplicationContext appContext = springApplication.run(args);
        return appContext;
    }
}
