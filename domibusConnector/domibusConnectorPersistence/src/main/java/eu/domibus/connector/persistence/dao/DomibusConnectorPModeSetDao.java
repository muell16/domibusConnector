package eu.domibus.connector.persistence.dao;

import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import eu.domibus.connector.domain.model.DomibusConnectorPModeSet;
import eu.domibus.connector.persistence.model.PDomibusConnectorPModeSet;
import eu.domibus.connector.persistence.model.PDomibusConnectorParty;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DomibusConnectorPModeSetDao extends CrudRepository<PDomibusConnectorPModeSet, Long> {

    @Query("SELECT p FROM PDomibusConnectorPModeSet p WHERE p.active = true AND p.messageLane.name=?1 ORDER by p.created")
    public List<PDomibusConnectorPModeSet> getCurrentActivePModeSet(DomibusConnectorMessageLane.MessageLaneId id);

}
