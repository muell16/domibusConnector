package eu.domibus.webadmin.dao;

import java.util.List;

import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorService;

public interface IDomibusWebAdminConnectorServiceDao {

	public abstract List<DomibusConnectorService> getServiceList();

	void persistNewService(DomibusConnectorService service);

}