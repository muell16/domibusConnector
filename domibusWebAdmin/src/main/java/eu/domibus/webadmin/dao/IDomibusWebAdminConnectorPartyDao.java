package eu.domibus.webadmin.dao;

import java.util.List;

import eu.domibus.connector.common.db.model.DomibusConnectorParty;

public interface IDomibusWebAdminConnectorPartyDao {

	public abstract List<DomibusConnectorParty> getPartyList();

	void persistNewParty(DomibusConnectorParty party);

}