package eu.ecodex.connector.common.db.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import eu.ecodex.connector.common.db.dao.ECodexServiceDao;
import eu.ecodex.connector.common.db.model.ECodexService;

public class ECodexServiceDaoImpl implements ECodexServiceDao {

    @PersistenceContext(unitName = "ecodex.connector")
    private EntityManager em;

    @Override
    public ECodexService getService(String serviceName) {
        return em.find(ECodexService.class, serviceName);
    }
}
