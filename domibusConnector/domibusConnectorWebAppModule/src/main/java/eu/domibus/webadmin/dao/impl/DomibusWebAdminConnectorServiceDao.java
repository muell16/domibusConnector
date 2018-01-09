package eu.domibus.webadmin.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.persistence.model.PDomibusConnectorService;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorServiceDao;

@Repository
public class DomibusWebAdminConnectorServiceDao implements IDomibusWebAdminConnectorServiceDao {

    @PersistenceContext //(unitName = "domibus.connector")
    private EntityManager em;

	@Override
    public List<PDomibusConnectorService> getServiceList() {
        return em.createQuery("SELECT s FROM DomibusConnectorService s", PDomibusConnectorService.class).getResultList();	
    }
	
	@Override
	@Transactional(readOnly=false, value="transactionManager")
	public void persistNewService(PDomibusConnectorService service){
		em.persist(service);
	}

	@Override
	public void update(PDomibusConnectorService service) {
		em.merge(service);
		
	}

	@Override	
	@Transactional(readOnly=true, value="transactionManager")
	public PDomibusConnectorService findById(String service) {
		return em.find(PDomibusConnectorService.class, service);
	}

	@Override
	public void delete(PDomibusConnectorService service) {
		em.remove(service);
	}
}
