package eu.domibus.connector.common.persistence.dao;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageLane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DomibusConnectorBusinessDomainDao extends JpaRepository<PDomibusConnectorMessageLane, Long> {

    public Optional<PDomibusConnectorMessageLane> findByName(DomibusConnectorBusinessDomain.BusinessDomainId name);

}
