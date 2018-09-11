package eu.domibus.connector.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "eu.domibus.connector")
@EnableTransactionManagement
@EnableAutoConfiguration
//@PropertySource({"file:${connector.config.file}", "classpath:/build-info.properties"})
@PropertySource({"classpath:/connector.properties", "classpath:/build-info.properties"})
public class DomibusConnectorStarter extends SpringBootServletInitializer {

    
	public static void main(String[] args) {
        runSpringApplication(args);
    }

    public static ConfigurableApplicationContext runSpringApplication(String[] args) {
    	SpringApplicationBuilder builder =
         new SpringApplicationBuilder()
                .sources(DomibusConnectorStarter.class)
                .profiles("connector",
                        //"embedded",     //use embedded database
                        "gwlink-ws", //use gw webservice based impl
                        "backendlink-ws"
                );
    	builder.properties("spring.banner.location=classpath:/ascii/domibusConnector.txt", "spring.output.ansi.enabled=DETECT");
    	SpringApplication springApplication = builder.build();
        ConfigurableApplicationContext appContext = springApplication.run(args);
        return appContext;
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.properties("spring.banner.location=classpath:/ascii/domibusConnector.txt", "spring.output.ansi.enabled=DETECT");
        application.profiles("connector",
                //"embedded",     //use embedded database
                "gwlink-ws", //use gw webservice based impl
                "backendlink-ws" //use backendlink ws based impl
        );
    	return application.sources(DomibusConnectorStarter.class);
    }
}
