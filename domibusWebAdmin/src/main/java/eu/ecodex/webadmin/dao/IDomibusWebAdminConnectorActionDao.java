package eu.ecodex.webadmin.dao;

import java.util.List;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;

public interface IDomibusWebAdminConnectorActionDao {

	public abstract List<DomibusConnectorAction> getActionList();

}