package eu.domibus.connector.persistence.spring;

import eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import eu.domibus.connector.persistence.dao.PackageDomibusConnectorRepositories;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
//@Configuration
//@EntityScan(basePackageClasses={PDomibusConnectorPersistenceModel.class})
//@EnableJpaRepositories(basePackageClasses = {PackageDomibusConnectorRepositories.class} )
//@EnableTransactionManagement
//@PropertySource("classpath:/eu/domibus/connector/persistence/config/default-persistence-config.properties")
public class DomibusConnectorPersistenceContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorPersistenceContext.class);
    
}
