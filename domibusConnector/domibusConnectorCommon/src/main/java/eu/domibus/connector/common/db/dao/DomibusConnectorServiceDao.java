package eu.domibus.connector.common.db.dao;

import eu.domibus.connector.common.db.model.DomibusConnectorService;

public interface DomibusConnectorServiceDao {

    public DomibusConnectorService getService(String serviceName);

}