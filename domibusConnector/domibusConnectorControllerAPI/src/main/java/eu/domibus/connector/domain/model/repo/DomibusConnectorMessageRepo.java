package eu.domibus.connector.domain.model.repo;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomibusConnectorMessageRepo extends JpaRepository<DomibusConnectorMessage, Long> {
}
