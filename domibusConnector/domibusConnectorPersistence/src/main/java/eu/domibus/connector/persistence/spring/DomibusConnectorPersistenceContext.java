package eu.domibus.connector.persistence.spring;

import eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceFilesystemImpl;
import eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceJpaImpl;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import eu.domibus.connector.persistence.dao.PackageDomibusConnectorRepositories;

import javax.sql.DataSource;

import static eu.domibus.connector.persistence.spring.PersistenceProfiles.STORAGE_DB_PROFILE_NAME;
import static eu.domibus.connector.persistence.spring.PersistenceProfiles.STORAGE_FS_PROFILE_NAME;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
@EntityScan(basePackageClasses={PDomibusConnectorPersistenceModel.class})
@EnableJpaRepositories(basePackageClasses = {PackageDomibusConnectorRepositories.class} )
@EnableTransactionManagement
@PropertySource("classpath:eu/domibus/connector/persistence/config/default-persistence-config.properties")
public class DomibusConnectorPersistenceContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorPersistenceContext.class);


    @Bean
//    @ConditionalOnProperty(name="connector.persistence.big-data-impl-class", havingValue = "eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceFilesystemImpl")
//    @ConditionalOnMissingBean(DomibusConnectorBigDataPersistenceService.class)
    @Profile(STORAGE_FS_PROFILE_NAME)
    public DomibusConnectorBigDataPersistenceServiceFilesystemImpl domibusConnectorBigDataPersistenceServiceFilesystemImpl() {
        return new DomibusConnectorBigDataPersistenceServiceFilesystemImpl();
    }

    @Bean
    @Profile(STORAGE_DB_PROFILE_NAME)
//    @ConditionalOnProperty(name="connector.persistence.big-data-impl-class", havingValue = "eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceJpaImpl")
    public DomibusConnectorBigDataPersistenceServiceJpaImpl domibusConnectorBigDataPersistenceServiceJpaImpl() {
        return new DomibusConnectorBigDataPersistenceServiceJpaImpl();
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSourceProperties dataSourceConfigurationProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSourceProperties properties = dataSourceConfigurationProperties();
        LOGGER.debug("Creating data source with properties: [{}]", properties);
        return properties.initializeDataSourceBuilder().build();
    }

}
