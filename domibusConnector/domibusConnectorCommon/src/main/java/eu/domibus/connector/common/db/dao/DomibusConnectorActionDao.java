package eu.domibus.connector.common.db.dao;

import eu.domibus.connector.persistence.model.DomibusConnectorAction;

public interface DomibusConnectorActionDao {

    DomibusConnectorAction getAction(String actionName);
}