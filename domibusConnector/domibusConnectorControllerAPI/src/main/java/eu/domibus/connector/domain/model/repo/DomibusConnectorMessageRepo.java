package eu.domibus.connector.domain.model.repo;

import eu.ecodex.dc5.message.model.DomibusConnectorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomibusConnectorMessageRepo extends JpaRepository<DomibusConnectorMessage, Long> {
}
