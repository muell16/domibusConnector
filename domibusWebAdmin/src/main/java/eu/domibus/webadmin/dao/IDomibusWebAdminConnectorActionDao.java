package eu.domibus.webadmin.dao;

import java.util.List;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;

public interface IDomibusWebAdminConnectorActionDao {

	public abstract List<DomibusConnectorAction> getActionList();

	void persistNewAction(DomibusConnectorAction action);

}