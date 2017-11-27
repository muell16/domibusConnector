package eu.domibus.webadmin.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorActionDao;

@Repository
public class DomibusWebAdminConnectorActionDao implements IDomibusWebAdminConnectorActionDao {

    @PersistenceContext //(unitName = "domibus.connector")
    private EntityManager em;

	@Override
    public List<DomibusConnectorAction> getActionList() {
    	return em.createQuery("SELECT a FROM DomibusConnectorAction a", DomibusConnectorAction.class).getResultList();	
    }
	
	@Override
	@Transactional(readOnly=false, value="transactionManager")
	public void persistNewAction(DomibusConnectorAction action){
		em.persist(action);
	}

	@Override
	public DomibusConnectorAction findById(String actionKey) {
		return em.find(DomibusConnectorAction.class, actionKey);
	}

	@Override
	public void delete(DomibusConnectorAction action) {
		em.remove(action);		
	}

	@Override
	public void update(DomibusConnectorAction action) {
		em.merge(action);		
	}
	
	
	
}
