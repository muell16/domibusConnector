
package eu.domibus.connector.backend.persistence.dao;

import eu.domibus.connector.backend.persistence.model.BackendClientInfo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Repository
@Transactional
public interface BackendClientDao extends CrudRepository<BackendClientInfo, Long> {

    BackendClientInfo findOneBackendByBackendNameAndEnabledIsTrue(String name);

//    List<BackendClientInfo> findByServices_serviceAndEnabledIsTrue(String service);
    List<BackendClientInfo> findByServicesAndEnabledIsTrue(String service);

    BackendClientInfo findOneByDefaultBackendIsTrue();

    BackendClientInfo findOneByBackendName(String backendName);

    BackendClientInfo findOneBackendByBackendName(String backendName);
    
    @Query("SELECT max(b.id) from BackendClientInfo b")
    Long findHighestId();
}
