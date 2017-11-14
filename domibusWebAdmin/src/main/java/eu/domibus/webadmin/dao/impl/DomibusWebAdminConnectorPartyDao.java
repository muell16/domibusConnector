package eu.domibus.webadmin.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorPartyPK;
import eu.domibus.webadmin.dao.IDomibusWebAdminConnectorPartyDao;

@Repository
public class DomibusWebAdminConnectorPartyDao implements IDomibusWebAdminConnectorPartyDao{

	private final static Logger LOG = LoggerFactory.getLogger(DomibusWebAdminConnectorPartyDao.class);
	
    @PersistenceContext //(unitName = "domibus.connector")
    private EntityManager em;
    
    
	@Override
    public List<DomibusConnectorParty> getPartyList() {
        return em.createQuery("SELECT p FROM DomibusConnectorParty p", DomibusConnectorParty.class).getResultList();	
    }

	@Override
	public void persistNewParty(DomibusConnectorParty party){
		LOG.trace("#persistNewParty: [{}]", party);
		em.persist(party);
	}

	@Override
	public DomibusConnectorParty findById(DomibusConnectorPartyPK partyId) {
		LOG.trace("#findById: [{}]", partyId);
		return em.find(DomibusConnectorParty.class, partyId);
	}

	@Override
	public void update(DomibusConnectorParty dbParty) {
		LOG.trace("#update: called with Party: [{}]", dbParty);
		em.merge(dbParty);
	}

	@Override
	public void delete(DomibusConnectorParty domibusConnectorParty) {
		LOG.trace("#delete: called with party [{}]", domibusConnectorParty);
		LOG.trace("#delete: party ID [{}] with role [{}]", domibusConnectorParty.getPartyId(), domibusConnectorParty.getRole());
		em.remove(domibusConnectorParty);	
	}
	
}
