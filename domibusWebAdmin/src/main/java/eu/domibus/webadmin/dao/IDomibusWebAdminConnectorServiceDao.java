package eu.domibus.webadmin.dao;

import java.util.List;

import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorService;

public interface IDomibusWebAdminConnectorServiceDao {

	public abstract List<DomibusConnectorService> getServiceList();

	void persistNewService(DomibusConnectorService service);

	public abstract void update(DomibusConnectorService service);

	public abstract DomibusConnectorService findById(String service);

	public abstract void delete(DomibusConnectorService service);

}