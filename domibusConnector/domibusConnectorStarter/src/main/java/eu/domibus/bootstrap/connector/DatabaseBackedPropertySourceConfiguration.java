package eu.domibus.bootstrap.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
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
@ConditionalOnProperty(name="bootstrap.database.property-loading.enabled", havingValue="true") //funktioniert das hier? TODO!!
@Order(Ordered.LOWEST_PRECEDENCE)
@org.springframework.context.annotation.PropertySource({"classpath:default.properties"})
public class DatabaseBackedPropertySourceConfiguration implements PropertySourceLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseBackedPropertySourceConfiguration.class);

    private static final String BOOTSTRAP_DATASOURCE_PROPERTIES = "bootstrap_datasource_properties";

    public static final String BOOTSTRAP_DATASOURCE = "bootstrap_datasource";

    @Bean
    @Qualifier(BOOTSTRAP_DATASOURCE_PROPERTIES)
    @ConfigurationProperties(prefix="bootstrap.datasource")
    public DataSourceProperties bootstrapDataSourceConfiguration() {
        return new DataSourceProperties();
    }

    @Bean
    @Qualifier(BOOTSTRAP_DATASOURCE)
    public DataSource bootstrapDataSource() {
        System.out.println("INITIALIZE BOOTSTRAP DATASOURCE");
        DataSourceProperties properties = bootstrapDataSourceConfiguration();
        LOGGER.trace("initialize bootstrap datasource with [{}]", properties);
        return properties.initializeDataSourceBuilder().build();
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
