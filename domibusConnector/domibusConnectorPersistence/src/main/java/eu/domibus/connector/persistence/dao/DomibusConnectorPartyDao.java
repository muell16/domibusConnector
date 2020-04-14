package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.persistence.model.PDomibusConnectorParty;
//import eu.domibus.connector.persistence.model.PDomibusConnectorPartyPK;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Dictionary;
import java.util.List;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Repository
public interface DomibusConnectorPartyDao extends CrudRepository<PDomibusConnectorParty, Long> {


//    @Query("SELECT p FROM PDomibusConnectorParty p WHERE p.partyId = ?1 AND p.deleted = false")
//    public List<PDomibusConnectorParty> findByPartyIdAndDeletedIsNot(String partyId);

//    public List<PDomibusConnectorParty> findByPartyIdAndRoleAndDeletedIsFalse(String partyId, String role);

//    List<PDomibusConnectorParty> findAllByDeletedIsFalse();

//    @Modifying
//    @Query("UPDATE PDomibusConnectorParty p SET p.deleted = true WHERE p = ?1")
//    void setDeleted(PDomibusConnectorParty dbParty);

}
