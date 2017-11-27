package eu.domibus.webadmin.dao;

import java.util.List;

import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorPartyPK;

public interface IDomibusWebAdminConnectorPartyDao {

	public abstract List<DomibusConnectorParty> getPartyList();

	void persistNewParty(DomibusConnectorParty party);

	public abstract DomibusConnectorParty findById(DomibusConnectorPartyPK oldPartyId);

	public abstract void update(DomibusConnectorParty dbParty);
	
	public abstract void delete(DomibusConnectorParty domibusConnectorParty);

}
