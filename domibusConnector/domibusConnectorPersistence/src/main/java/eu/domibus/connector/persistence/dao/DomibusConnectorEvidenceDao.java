package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.persistence.model.PDomibusConnectorEvidence;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Repository
@Transactional
public interface DomibusConnectorEvidenceDao extends CrudRepository<PDomibusConnectorEvidence, Long> {

    //@Query("select e from PDomibusConnectorEvidence e where e.message = ?1")
    @Query(value = "select * FROM DOMIBUS_CONNECTOR_EVIDENCE where MESSAGE_ID=?1", nativeQuery = true) //native sql query
    public @Nonnull List<PDomibusConnectorEvidence> findEvidencesForMessage(Long messageId);
    
    @Modifying
    @Query("update PDomibusConnectorEvidence e set e.deliveredToGateway=CURRENT_TIMESTAMP where e.id = ?1")
    public int setDeliveredToGateway(Long evidenceId);
    
    @Modifying
    @Query("update PDomibusConnectorEvidence e set e.deliveredToNationalSystem=CURRENT_TIMESTAMP where e.id = ?1")
    public int setDeliveredToBackend(Long evidenceId);
    
}
