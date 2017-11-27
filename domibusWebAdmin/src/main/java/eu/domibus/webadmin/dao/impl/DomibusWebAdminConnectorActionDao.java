package eu.domibus.webadmin.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorActionDao;

public class DomibusWebAdminConnectorActionDao implements IDomibusWebAdminConnectorActionDao {

    @PersistenceContext(unitName = "domibus.connector")
    private EntityManager em;

	@Override
    public List<DomibusConnectorAction> getActionList() {
    	return em.createQuery("SELECT a FROM DomibusConnectorAction a").getResultList();	
    }
	
	@Override
	 @Transactional(readOnly=false, value="transactionManagerWebAdmin")
	public void persistNewAction(DomibusConnectorAction action){
		em.persist(action);
	}
}
