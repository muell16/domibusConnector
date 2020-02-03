package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.persistence.model.PDomibusConnectorTransportStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomibusConnectorTransportStepDao extends JpaRepository<PDomibusConnectorTransportStep, Long> {


}
