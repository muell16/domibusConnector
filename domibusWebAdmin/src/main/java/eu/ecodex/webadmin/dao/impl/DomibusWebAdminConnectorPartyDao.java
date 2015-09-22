package eu.ecodex.webadmin.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.ecodex.webadmin.dao.IDomibusWebAdminConnectorPartyDao;

public class DomibusWebAdminConnectorPartyDao implements IDomibusWebAdminConnectorPartyDao{

    @PersistenceContext(unitName = "domibus.connector")
    private EntityManager em;
    
	@Override
    public List<DomibusConnectorParty> getPartyList() {
        return em.createQuery("SELECT p FROM DomibusConnectorParty p").getResultList();	
    }

}
