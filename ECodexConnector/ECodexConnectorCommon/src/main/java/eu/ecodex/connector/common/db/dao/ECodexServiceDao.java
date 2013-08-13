package eu.ecodex.connector.common.db.dao;

import eu.ecodex.connector.common.db.model.ECodexService;

public interface ECodexServiceDao {

    public ECodexService getService(String serviceName);

}