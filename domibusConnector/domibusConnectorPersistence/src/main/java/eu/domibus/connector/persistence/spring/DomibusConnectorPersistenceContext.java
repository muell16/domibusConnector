package eu.domibus.connector.persistence.spring;

import eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceFilesystemImpl;
import eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceJpaImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import eu.domibus.connector.persistence.dao.PackageDomibusConnectorRepositories;

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


    @Bean
    @ConditionalOnProperty(name="connector.persistence.big-data-impl-class", havingValue = "eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceFilesystemImpl")
    @ConditionalOnMissingBean(DomibusConnectorBigDataPersistenceService.class)
    public DomibusConnectorBigDataPersistenceServiceFilesystemImpl domibusConnectorBigDataPersistenceServiceFilesystemImpl() {
        return new DomibusConnectorBigDataPersistenceServiceFilesystemImpl();
    }

    @Bean
    @ConditionalOnProperty(name="connector.persistence.big-data-impl-class", havingValue = "eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceJpaImpl")
    public DomibusConnectorBigDataPersistenceServiceJpaImpl domibusConnectorBigDataPersistenceServiceJpaImpl() {
        return new DomibusConnectorBigDataPersistenceServiceJpaImpl();
    }

}
