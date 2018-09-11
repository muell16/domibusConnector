package eu.domibus.bootstrap.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatabaseBackedPropertySourceConfiguration  implements PropertySourceLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseBackedPropertySourceConfiguration.class);

    @Autowired
    private ConfigurableEnvironment env;

    @Bean
    @ConfigurationProperties(prefix="bootstrap.datasource")
    public DataSource bootstrapDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Override
    public PropertySource<?> locate(Environment environment) {
        LOGGER.debug("added databaseBackedPropertySource to PropertySource");
        return databaseBackedPropertySource();
    }

    @Bean
    public DatatabaseBackedPropertySource databaseBackedPropertySource() {
        DatatabaseBackedPropertySource propertySource = new DatatabaseBackedPropertySource("DATABASE");
        propertySource.setDataSource(bootstrapDataSource());
        return propertySource;
    }



}
