package eu.domibus.connector.common.db.dao;

import eu.domibus.connector.persistence.model.DomibusConnectorService;

public interface DomibusConnectorServiceDao {

    public DomibusConnectorService getService(String serviceName);

}