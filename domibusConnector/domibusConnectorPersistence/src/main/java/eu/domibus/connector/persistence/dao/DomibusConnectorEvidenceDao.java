package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.persistence.model.PDomibusConnectorEvidence;
import java.util.List;
import javax.annotation.Nonnull;

import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.enums.EvidenceType;
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

    public @Nonnull List<PDomibusConnectorEvidence> findByMessage_Id(Long messageId);

    /**
     *
     * @param evidenceId the evidence to change
     * @return modified rows
     */
    @Modifying
    @Query("update PDomibusConnectorEvidence e set e.deliveredToGateway=CURRENT_TIMESTAMP where e.id = ?1")
    public int setDeliveredToGateway(Long evidenceId);

    /**
     *
     * @param msg - the database message the evidence belongs to
     * @param type - the evidence type to set delivered
     * @return modified rows
     */
    @Modifying
    @Query("update PDomibusConnectorEvidence e set e.deliveredToGateway=CURRENT_TIMESTAMP where e.message = ?1 AND e.type = ?2")
    public int setDeliveredToGateway(PDomibusConnectorMessage msg, EvidenceType type);

    /**
     *
     * @param evidenceId the evidence to change
     * @return modified rows
     */
    @Modifying
    @Query("update PDomibusConnectorEvidence e set e.deliveredToNationalSystem=CURRENT_TIMESTAMP where e.id = ?1")
    public int setDeliveredToBackend(Long evidenceId);

    /**
     *
     * @param message - the database message the evidence belongs to
     * @param type - the evidence type to set delivered
     * @return modified rows
     */
    @Modifying
    @Query("update PDomibusConnectorEvidence e set e.deliveredToNationalSystem=CURRENT_TIMESTAMP where e.message = ?1 AND e.type = ?2")
    public int setDeliveredToBackend(PDomibusConnectorMessage message, EvidenceType type);

}
