/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.persistence.model.DomibusConnectorEvidence;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Repository
public interface DomibusConnectorEvidenceDao extends CrudRepository<DomibusConnectorEvidence, Long> {

    @Query("select e from DomibusConnectorEvidence e where e.message = ?1")
    public @Nonnull List<DomibusConnectorEvidence> findEvidencesForMessage(Long messageId);
    
    @Modifying
    @Query("update DomibusConnectorEvidence e set e.deliveredToGateway=CURRENT_TIMESTAMP where e.id = ?1")
    public int setDeliveredToGateway(Long evidenceId);
    
    @Modifying
    @Query("update DomibusConnectorEvidence e set e.deliveredToNationalSystem=CURRENT_TIMESTAMP where e.id = ?1")
    public int setDeliveredToBackend(Long evidenceId);
    
}
