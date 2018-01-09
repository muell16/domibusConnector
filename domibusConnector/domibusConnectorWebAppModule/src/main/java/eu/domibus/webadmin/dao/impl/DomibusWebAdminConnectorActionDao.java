package eu.domibus.webadmin.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.persistence.model.PDomibusConnectorAction;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorActionDao;

@Repository
public class DomibusWebAdminConnectorActionDao implements IDomibusWebAdminConnectorActionDao {

    @PersistenceContext //(unitName = "domibus.connector")
    private EntityManager em;

	@Override
    public List<PDomibusConnectorAction> getActionList() {
    	return em.createQuery("SELECT a FROM DomibusConnectorAction a", PDomibusConnectorAction.class).getResultList();	
    }
	
	@Override
	@Transactional(readOnly=false, value="transactionManager")
	public void persistNewAction(PDomibusConnectorAction action){
		em.persist(action);
	}

	@Override
	public PDomibusConnectorAction findById(String actionKey) {
		return em.find(PDomibusConnectorAction.class, actionKey);
	}

	@Override
	public void delete(PDomibusConnectorAction action) {
		em.remove(action);		
	}

	@Override
	public void update(PDomibusConnectorAction action) {
		em.merge(action);		
	}
	
	
	
}
