
package eu.domibus.connector.backend.persistence.spring;

import eu.domibus.connector.backend.persistence.dao.BackendPersistenceDaoPackage;
import eu.domibus.connector.backend.persistence.model.BackendPersistenceModelPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@EntityScan(basePackageClasses={BackendPersistenceModelPackage.class})
@EnableJpaRepositories(basePackageClasses={BackendPersistenceDaoPackage.class})
@Configuration
public class BackendPersistenceConfig {

}
