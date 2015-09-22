package eu.ecodex.webadmin.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.ecodex.webadmin.dao.IDomibusWebAdminConnectorActionDao;

public class DomibusWebAdminConnectorActionDao implements IDomibusWebAdminConnectorActionDao {

    @PersistenceContext(unitName = "domibus.connector")
    private EntityManager em;

	@Override
    public List<DomibusConnectorAction> getActionList() {
    	return em.createQuery("SELECT a FROM DomibusConnectorAction a").getResultList();	
    }
}
