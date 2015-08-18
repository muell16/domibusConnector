package eu.domibus.connector.common.db.dao;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;

public interface DomibusConnectorActionDao {

    DomibusConnectorAction getAction(String actionName);
}