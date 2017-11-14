package eu.domibus.webadmin.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorServiceDao;

@Repository
public class DomibusWebAdminConnectorServiceDao implements IDomibusWebAdminConnectorServiceDao {

    @PersistenceContext //(unitName = "domibus.connector")
    private EntityManager em;

	@Override
    public List<DomibusConnectorService> getServiceList() {
        return em.createQuery("SELECT s FROM DomibusConnectorService s", DomibusConnectorService.class).getResultList();	
    }
	
	@Override
	@Transactional(readOnly=false, value="transactionManager")
	public void persistNewService(DomibusConnectorService service){
		em.persist(service);
	}

	@Override
	public void update(DomibusConnectorService service) {
		em.merge(service);
		
	}

	@Override	
	@Transactional(readOnly=true, value="transactionManager")
	public DomibusConnectorService findById(String service) {
		return em.find(DomibusConnectorService.class, service);
	}

	@Override
	public void delete(DomibusConnectorService service) {
		em.remove(service);
	}
}
