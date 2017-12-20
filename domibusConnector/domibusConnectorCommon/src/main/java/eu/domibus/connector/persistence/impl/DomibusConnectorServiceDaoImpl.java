package eu.domibus.connector.persistence.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import eu.domibus.connector.common.db.dao.DomibusConnectorServiceDao;
import eu.domibus.connector.persistence.model.DomibusConnectorService;
import org.springframework.stereotype.Repository;

@Repository("serviceDao")
public class DomibusConnectorServiceDaoImpl implements DomibusConnectorServiceDao {

    @PersistenceContext //(unitName = "domibus.connector")
    private EntityManager em;

    @Override
    public DomibusConnectorService getService(String serviceName) {
        return em.find(DomibusConnectorService.class, serviceName);
    }
}
