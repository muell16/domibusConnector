
package eu.domibus.webadmin.spring;

import eu.domibus.webadmin.persistence.dao.DomibusWebAdminPersistenceDaoPackage;
import eu.domibus.webadmin.persistence.model.DomibusWebAdminPersistenceModelPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
@EntityScan(basePackageClasses={DomibusWebAdminPersistenceModelPackage.class})
@EnableJpaRepositories(basePackageClasses = {DomibusWebAdminPersistenceDaoPackage.class} )
@EnableTransactionManagement
public class DomibusWebAdminPersistenceContext {

}
