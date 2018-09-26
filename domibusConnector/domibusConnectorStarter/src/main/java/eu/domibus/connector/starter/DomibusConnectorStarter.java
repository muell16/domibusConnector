package eu.domibus.connector.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "eu.domibus.connector")
@EnableTransactionManagement
//@PropertySource({"classpath:connector.properties", "classpath:build-info.properties"}) //connector.properties should be loaded by default, because
//spring.application.name=connector is set so spring boot is looking for ${spring.application.name}.properties
@PropertySource({"classpath:build-info.properties"})
public class DomibusConnectorStarter extends SpringBootServletInitializer {

	    
	public static void main(String[] args) {
        runSpringApplication(args);
    }

    public static ConfigurableApplicationContext runSpringApplication(String[] args) {
    	SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder = configureApplicationContext(builder);
    	SpringApplication springApplication = builder.build();
        ConfigurableApplicationContext appContext = springApplication.run(args);
        return appContext;
    }

    public static SpringApplicationBuilder configureApplicationContext(SpringApplicationBuilder application) {
        connector4_0Compatibility();


        application.profiles("connector",
                //"embedded",     //use embedded database
                "gwlink-ws", //use gw webservice based impl
                "backendlink-ws" //use backendlink ws based impl
        );
        return application.sources(DomibusConnectorStarter.class);
    }

    /**
     * this function is used to set the System properties for logging.config and connector.config.file 4.0 to be compatible with
     * the connector 4.0
     */
    private static void connector4_0Compatibility() {
        String connectorConfigFile = System.getProperty("connector.config.file");
        if (connectorConfigFile != null) {
            System.setProperty("spring.config.location", connectorConfigFile);
        }
        String connectorLoggingConfigFile = System.getProperty("connector.logging.config");
        if (connectorLoggingConfigFile != null) {
            System.setProperty("logging.config", connectorConfigFile);
        }

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return configureApplicationContext(application);
    }
}
