package eu.domibus.webadmin.dao;

import java.util.List;

import eu.domibus.connector.persistence.model.PDomibusConnectorService;

public interface IDomibusWebAdminConnectorServiceDao {

	public abstract List<PDomibusConnectorService> getServiceList();

	void persistNewService(PDomibusConnectorService service);

	public abstract void update(PDomibusConnectorService service);

	public abstract PDomibusConnectorService findById(String service);

	public abstract void delete(PDomibusConnectorService service);

}
