package eu.ecodex.connector.common.db.dao;

import eu.ecodex.connector.common.db.model.ECodexParty;

public interface ECodexPartyDao {

    public ECodexParty getParty(String partyId, String role);

    public ECodexParty getPartyByPartyId(String partyId);

}