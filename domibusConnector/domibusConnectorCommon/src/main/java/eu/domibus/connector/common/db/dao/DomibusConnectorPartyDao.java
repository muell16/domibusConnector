package eu.domibus.connector.common.db.dao;

import eu.domibus.connector.common.db.model.DomibusConnectorParty;

public interface DomibusConnectorPartyDao {

    public DomibusConnectorParty getParty(String partyId, String role);

    public DomibusConnectorParty getPartyByPartyId(String partyId);

}