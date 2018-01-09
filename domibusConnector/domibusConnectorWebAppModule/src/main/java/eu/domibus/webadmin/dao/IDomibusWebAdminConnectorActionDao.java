package eu.domibus.webadmin.dao;

import java.util.List;

import eu.domibus.connector.persistence.model.PDomibusConnectorAction;

public interface IDomibusWebAdminConnectorActionDao {

	public abstract List<PDomibusConnectorAction> getActionList();

	void persistNewAction(PDomibusConnectorAction action);

	public PDomibusConnectorAction findById(String action);

	public void delete(PDomibusConnectorAction action);

	public void update(PDomibusConnectorAction action);

}
