package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageLane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DomibusConnectorMessageLaneDao extends JpaRepository<PDomibusConnectorMessageLane, Long> {

    public Optional<PDomibusConnectorMessageLane> findByName(DomibusConnectorMessageLane.MessageLaneId name);

}
