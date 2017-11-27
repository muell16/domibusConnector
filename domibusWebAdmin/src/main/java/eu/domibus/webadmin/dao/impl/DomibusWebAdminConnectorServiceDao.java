package eu.domibus.webadmin.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorServiceDao;

public class DomibusWebAdminConnectorServiceDao implements IDomibusWebAdminConnectorServiceDao {

    @PersistenceContext(unitName = "domibus.connector")
    private EntityManager em;

	@Override
    public List<DomibusConnectorService> getServiceList() {
        return em.createQuery("SELECT s FROM DomibusConnectorService s").getResultList();	
    }
	
	@Override
	 @Transactional(readOnly=false, value="transactionManagerWebAdmin")
	public void persistNewService(DomibusConnectorService service){
		em.persist(service);
	}
}
