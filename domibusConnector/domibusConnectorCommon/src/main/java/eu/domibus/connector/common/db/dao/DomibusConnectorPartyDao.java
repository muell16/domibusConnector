package eu.domibus.connector.common.db.dao;

import eu.domibus.connector.persistence.model.DomibusConnectorParty;

public interface DomibusConnectorPartyDao {

    public DomibusConnectorParty getParty(String partyId, String role);

    public DomibusConnectorParty getPartyByPartyId(String partyId);

}