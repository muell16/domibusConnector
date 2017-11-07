package eu.domibus.webadmin.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorPartyDao;

public class DomibusWebAdminConnectorPartyDao implements IDomibusWebAdminConnectorPartyDao{

    @PersistenceContext(unitName = "domibus.connector")
    private EntityManager em;
    
	@Override
    public List<DomibusConnectorParty> getPartyList() {
        return em.createQuery("SELECT p FROM DomibusConnectorParty p").getResultList();	
    }

	@Override
	 @Transactional(readOnly=false, value="transactionManagerWebAdmin")
	public void persistNewParty(DomibusConnectorParty party){
		em.persist(party);
	}
}
