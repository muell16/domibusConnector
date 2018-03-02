
package eu.domibus.connector.backend.persistence.dao;

import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Repository
@Transactional
public interface BackendClientDao extends CrudRepository<BackendClientInfo, Long> {

    public BackendClientInfo findOneBackendByBackendNameAndEnabledIsTrue(String name);

    public List<BackendClientInfo> findByServices_serviceAndEnabledIsTrue(String service);

    public BackendClientInfo findOneByDefaultBackendIsTrue();
}
