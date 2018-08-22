package eu.domibus.connector.persistence.spring;

import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceFilesystemImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Contains all properties related to the persistence module
 *  except DataSource which is configured via spring boot datasource
 *
 */
@Component
@ConfigurationProperties(prefix = "connector.persistence")
@PropertySource("classpath:eu/domibus/connector/persistence/config/default-persistence-config.properties")
public class DomibusConnectorPersistenceProperties {

    /**
     * Which big DataImpl should be used
     *  part of the connector are
     *  <ul>
     *      <li>FileBased ({@link eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceFilesystemImpl}</li>
     *      <li>DatabaseBased ({@link eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceJpaImpl}</li>
     *  </ul>
     *
     *
     */
    private Class<? extends DomibusConnectorBigDataPersistenceService> bigDataImplClass = DomibusConnectorBigDataPersistenceServiceFilesystemImpl.class;

    public Class<? extends DomibusConnectorBigDataPersistenceService> getBigDataImplClass() {
        return bigDataImplClass;
    }

    public void setBigDataImplClass(Class<DomibusConnectorBigDataPersistenceService> bigDataImplClass) {
        this.bigDataImplClass = bigDataImplClass;
    }
}
