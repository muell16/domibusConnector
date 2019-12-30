package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import eu.domibus.connector.persistence.model.PDomibusConnectorLinkInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DomibusConnectorLinkInfoDao extends JpaRepository<PDomibusConnectorLinkInfo, Long> {

    Optional<PDomibusConnectorLinkInfo> findOneBackendByLinkNameAndEnabledIsTrue(String name);

    Optional<PDomibusConnectorLinkInfo> findOneByLinkName(String linkName);

    @Query("SELECT max(e.id) from PDomibusConnectorLinkInfo e")
    Long findHighestId();

    List<PDomibusConnectorLinkInfo> findAllByEnabledIsTrue();
}
