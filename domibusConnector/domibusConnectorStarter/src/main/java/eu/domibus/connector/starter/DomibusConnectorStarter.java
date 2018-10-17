package eu.domibus.connector.starter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;

import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.SystemPropertyUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@SpringBootApplication(scanBasePackages = "eu.domibus.connector")
@EnableTransactionManagement
//@PropertySource({"classpath:build-info.properties", "classpath:default.properties"})
@PropertySource({"classpath:build-info.properties"})
public class DomibusConnectorStarter extends SpringBootServletInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorStarter.class);

	public static final String SPRING_CLOUD_BOOTSTRAP_NAME = "spring.cloud.bootstrap.name";
    public static final String SPRING_CLOUD_BOOTSTRAP_LOCATION = "spring.cloud.bootstrap.location";
    public static final String SPRING_CONFIG_LOCATION = "spring.config.location";
    public static final String SPRING_CONFIG_NAME = "spring.config.name";

	public static final String CONNECTOR_CONFIG_FILE = "connector.config.file";

	public static final String CONNECTOR_CONFIG_LOCATION = "connector.config.location";

	private ServletContext servletContext;

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
    	String connectorConfigFile = System.getProperty(CONNECTOR_CONFIG_FILE);
        Properties springProperties = new Properties();
        if (connectorConfigFile != null) {
            connectorConfigFile = SystemPropertyUtils.resolvePlaceholders(connectorConfigFile);
            Path connectorConfigFilePath = Paths.get(connectorConfigFile);
            if (!Files.exists(connectorConfigFilePath)) {
                throw new RuntimeException(String.format("Cannot start because the via System Property [%s] provided config file does not exist!", CONNECTOR_CONFIG_FILE));
            }
            
            int lastIndex = connectorConfigFile.contains(File.separator)?connectorConfigFile.lastIndexOf(File.separatorChar):connectorConfigFile.lastIndexOf("/");
            lastIndex++;
            String connectorConfigLocation = connectorConfigFile.substring(0, lastIndex);
            String configName = connectorConfigFile.substring(lastIndex);
           
            LOGGER.info(String.format("Setting:\n %s=%s\n%s=%s\n%s=%s\n%s=%s",
                    SPRING_CLOUD_BOOTSTRAP_NAME, configName,
                    SPRING_CLOUD_BOOTSTRAP_LOCATION, connectorConfigLocation,
                    SPRING_CONFIG_LOCATION, connectorConfigLocation,
                    SPRING_CONFIG_NAME, configName));

            //springProperties.setProperty(SPRING_CLOUD_BOOTSTRAP_NAME, configName);
//            springProperties.setProperty(SPRING_CLOUD_BOOTSTRAP_LOCATION, connectorConfigLocation);
            springProperties.setProperty(SPRING_CLOUD_BOOTSTRAP_LOCATION, connectorConfigFile);
            
            springProperties.setProperty(SPRING_CONFIG_LOCATION, connectorConfigLocation);
            springProperties.setProperty(SPRING_CONFIG_NAME, configName);

        }else {
        	LOGGER.warn("SystemProperty \"{}\" not given or not resolveable! Startup using default spring external configuration!", CONNECTOR_CONFIG_FILE);
//            System.exit(-1);
        }
        //map spring.config.location on connector.config.location: connector.config.location=${spring.config.location}
        //springProperties.setProperty(CONNECTOR_CONFIG_LOCATION, "${" + SPRING_CONFIG_LOCATION + "}");
        //springProperties.setProperty("spring.cloud.config.enabled", "false");

        application.profiles("connector"); //always start with the connector profile!
        application.properties(springProperties); //pass the mapped CONNECTOR_CONFIG_FILE to the spring properties...

        return application.sources(DomibusConnectorStarter.class);
    }


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.servletContext = servletContext;
	    super.onStartup(servletContext);
    }


    private void setFromSystemPropertyIfNotNull(String name, String setPropertyName) {
        String value = System.getProperty(name);
        LOGGER.info("Config name from SystemProperty is [{}] value is [{}]", name, value);
        if (value != null) {
            LOGGER.info("Setting servletInitParam [{}] to value [{}]", setPropertyName, value);
            servletContext.setInitParameter(setPropertyName, value);
        }


    }

    private void setFromServletContextIfNotNull(String name, String setPropertyName) {
        String value = servletContext.getInitParameter(name);
        LOGGER.info("Config name from servletContext is [{}] value is [{}]", name, value);
        if (value != null) {
            LOGGER.info("Setting servletInitParam [{}] to value [{}]", setPropertyName, value);
            servletContext.setInitParameter(setPropertyName, value);
        }
    }
//
//    /**
//     * this function is used to set the System properties for logging.config and connector.config.file 4.0 to be compatible with
//     * the connector 4.0
//     */
//    private static void connector4_0Compatibility() {
//        String connectorConfigFile = System.getProperty("connector.config.file");
//        if (connectorConfigFile != null) {
//            System.setProperty("spring.config.location", connectorConfigFile);
//        }
//        String connectorLoggingConfigFile = System.getProperty("connector.logging.config");
//        if (connectorLoggingConfigFile != null) {
//            System.setProperty("logging.config", connectorConfigFile);
//        }
//
//    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return configureApplicationContext(application);
    }
}
