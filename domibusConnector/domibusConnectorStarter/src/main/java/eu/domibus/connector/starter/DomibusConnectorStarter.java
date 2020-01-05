package eu.domibus.connector.starter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import javax.annotation.Nullable;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@SpringBootApplication(scanBasePackages = "eu.domibus.connector")
@EnableTransactionManagement
@PropertySource({"classpath:/build-info.properties", "classpath:/default.properties", "classpath:/default-bootstrap.properties"})
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


    public static Properties loadConnectorConfigProperties(String connectorConfigFile) {
        Properties p = new Properties();
        if (connectorConfigFile != null) {
            Path connectorConfigFilePath = Paths.get(connectorConfigFile);
            if (!Files.exists(connectorConfigFilePath)) {
                String errorString = String.format("Cannot start because the via System Property [%s] provided config file [%s] mapped to path [%s] does not exist!", CONNECTOR_CONFIG_FILE, connectorConfigFile, connectorConfigFilePath);
                LOGGER.error(errorString);
                throw new RuntimeException(errorString);
            }
            try {
                p.load(new FileInputStream(connectorConfigFilePath.toFile()));
                return p;
            } catch (IOException e) {
                throw new RuntimeException(String.format("Cannot load properties from file [%s], is it a valid and readable properties file?", connectorConfigFilePath), e);
            }
        }
        return p;
    }

    public static @Nullable
    String getConnectorConfigFile() {
        String connectorConfigFile = System.getProperty(CONNECTOR_CONFIG_FILE);
        Properties springProperties = new Properties();
        if (connectorConfigFile != null) {
            connectorConfigFile = SystemPropertyUtils.resolvePlaceholders(connectorConfigFile);
            return connectorConfigFile;
        }
        return null;
    }


//    public static Properties configureApplicationProperties() {
//
//        return springProperties;
//    }


    public static SpringApplicationBuilder configureApplicationContext(SpringApplicationBuilder application) {
        String connectorConfigFile = getConnectorConfigFile();
        Properties springProperties = new Properties();
        if (connectorConfigFile != null) {

            int lastIndex = connectorConfigFile.contains(File.separator) ? connectorConfigFile.lastIndexOf(File.separatorChar) : connectorConfigFile.lastIndexOf("/");
            lastIndex++;
            String connectorConfigLocation = connectorConfigFile.substring(0, lastIndex);
            String configName = connectorConfigFile.substring(lastIndex);

            LOGGER.info(String.format("Setting:\n%s=%s\n%s=%s\n%s=%s\n%s=%s",
                    SPRING_CLOUD_BOOTSTRAP_NAME, configName,
                    SPRING_CLOUD_BOOTSTRAP_LOCATION, connectorConfigLocation,
                    SPRING_CONFIG_LOCATION, connectorConfigLocation,
                    SPRING_CONFIG_NAME, configName));

            springProperties.setProperty(SPRING_CLOUD_BOOTSTRAP_LOCATION, connectorConfigFile);
            springProperties.setProperty(SPRING_CONFIG_LOCATION, connectorConfigLocation);
            springProperties.setProperty(SPRING_CONFIG_NAME, configName);

        } else {
            LOGGER.warn("SystemProperty \"{}\" not given or not resolvable! Startup using default spring external configuration!", CONNECTOR_CONFIG_FILE);
        }
        application.properties(springProperties); //pass the mapped CONNECTOR_CONFIG_FILE to the spring properties...
        return application.sources(DomibusConnectorStarter.class);
    }


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.servletContext = servletContext;

        //read logging.config from connector properties and set it before the application context ist started
        //so its already available for the spring logging servlet initializer to configure logging!
        String connectorConfigFile = getConnectorConfigFile();
        if (connectorConfigFile != null) {
            Properties p = loadConnectorConfigProperties(connectorConfigFile);
            String loggingConfig = p.getProperty("logging.config");
            if (loggingConfig != null) {
                servletContext.setInitParameter("logging.config", loggingConfig);
            }
        }
        super.onStartup(servletContext);
    }

    private void setFromServletContextIfNotNull(String name, String setPropertyName) {
        String value = servletContext.getInitParameter(name);
        LOGGER.info("Config name from servletContext is [{}] value is [{}]", name, value);
        if (value != null) {
            LOGGER.info("Setting servletInitParam [{}] to value [{}]", setPropertyName, value);
            servletContext.setInitParameter(setPropertyName, value);
        }
    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return configureApplicationContext(application);
    }
}
