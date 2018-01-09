package eu.domibus.webadmin.dao;

import java.util.List;

import eu.domibus.connector.persistence.model.PDomibusConnectorParty;
import eu.domibus.connector.persistence.model.PDomibusConnectorPartyPK;

public interface IDomibusWebAdminConnectorPartyDao {

	public abstract List<PDomibusConnectorParty> getPartyList();

	void persistNewParty(PDomibusConnectorParty party);

	public abstract PDomibusConnectorParty findById(PDomibusConnectorPartyPK oldPartyId);

	public abstract void update(PDomibusConnectorParty dbParty);
	
	public abstract void delete(PDomibusConnectorParty domibusConnectorParty);

}
