package eu.domibus.webadmin.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorPartyPK;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorPartyDao;

public class DomibusWebAdminConnectorPartyDao implements IDomibusWebAdminConnectorPartyDao{

	private final static Logger LOG = LoggerFactory.getLogger(DomibusWebAdminConnectorPartyDao.class);
	
    @PersistenceContext(unitName = "domibus.connector")
    private EntityManager em;
    
	@Override
    public List<DomibusConnectorParty> getPartyList() {
        return em.createQuery("SELECT p FROM DomibusConnectorParty p", DomibusConnectorParty.class).getResultList();	
    }

	@Override
	@Transactional(readOnly=false, value="transactionManagerWebAdmin")
	public void persistNewParty(DomibusConnectorParty party){
		em.persist(party);
	}

	@Override
	@Transactional(readOnly=false, value="transactionManagerWebAdmin")
	public DomibusConnectorParty findById(DomibusConnectorPartyPK oldPartyId) {
		return em.find(DomibusConnectorParty.class, oldPartyId);
	}

	@Override
	@Transactional(readOnly=false, value="transactionManagerWebAdmin")
	public void update(DomibusConnectorParty dbParty) {
		LOG.trace("#update: called with Party: [{}]", dbParty);
		em.merge(dbParty);
	}

	@Override
	@Transactional(readOnly=false, value="transactionManagerWebAdmin")
	public void delete(DomibusConnectorParty domibusConnectorParty) {
		LOG.trace("#delete: called with party [{}]", domibusConnectorParty);
		em.remove(domibusConnectorParty);		
		
	}
}
