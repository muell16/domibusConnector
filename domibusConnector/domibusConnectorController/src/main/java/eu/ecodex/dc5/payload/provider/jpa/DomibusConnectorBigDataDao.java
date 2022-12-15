package eu.ecodex.dc5.payload.provider.jpa;

import eu.ecodex.dc5.payload.model.PDomibusConnectorBigData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomibusConnectorBigDataDao extends CrudRepository<PDomibusConnectorBigData, Long> {
}
