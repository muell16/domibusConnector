package eu.domibus.bootstrap.connector;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.parameters.P;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static eu.domibus.connector.starter.DomibusConnectorStarter.CONNECTOR_CONFIG_FILE;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE + 10)
@ConfigurationProperties(prefix="spring.cloud.bootstrap")
public class BootstrapConfigValid {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapConfigValid.class);

    String location;

    String name;

    @PostConstruct
    public void postConstruct() {
        Path path = Paths.get(location);
        if (!Files.exists(path)) {
            LOGGER.error("Cannot read defined settings file from path [{}] check your system/env/servlet property [{}]", location, CONNECTOR_CONFIG_FILE);
        }
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
