package eu.domibus.connector.configuration;

import eu.domibus.connector.configuration.domain.ConfigurationProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindException;

import java.util.List;
import java.util.Properties;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class ConfigurationPropertyManagerImplTest {

    private static final Logger LOGGER = LogManager.getLogger(ConfigurationPropertyManagerImplTest.class);



    @Autowired
    private ConfigurationPropertyManagerImpl configurationPropertyManager;

    @org.junit.jupiter.api.Test
    void getAll() {

        List<ConfigurationProperty> all = configurationPropertyManager.getAll("eu.domibus.connector.configuration");

        LOGGER.info("all config properties are: [{}]", all);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(all),
                () -> assertThat(all).hasSize(4)
        );
    }

    @org.junit.jupiter.api.Test
    void getAll_withSubpackageFiltering() {

        List<ConfigurationProperty> all = configurationPropertyManager.getAll("eu.domibus.connector.configuration.testdata");

        LOGGER.info("all config properties are: [{}]", all);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(all),
                () -> assertThat(all).hasSize(2)
        );
    }

    @Test
    void isValidTest() {

        Properties properties = new Properties();
        properties.put("connector.configuration.text", "text");
        properties.put("connector.configuration.number", "59");
        properties.put("connector.abc.address", "Testgasse 2");

        ConfigurationPropertySource configurationPropertySource = new MapConfigurationPropertySource(properties);

        configurationPropertyManager.isConfigurationValid(configurationPropertySource, "eu.domibus.connector.configuration");

    }

    @Test
    void isValidTest_shouldThrow() {

        Properties properties = new Properties();
        properties.put("connector.configuration.text", "text");
        properties.put("connector.configuration.number", "89");
        properties.put("connector.abc.address", "Testgasse 2");

        ConfigurationPropertySource configurationPropertySource = new MapConfigurationPropertySource(properties);

        Assertions.assertThrows( org.springframework.boot.context.properties.bind.BindException.class, () -> {
            configurationPropertyManager.isConfigurationValid(configurationPropertySource, "eu.domibus.connector.configuration.testdata");
        });

    }

}