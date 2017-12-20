package eu.domibus.connector.common.db.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import eu.domibus.connector.common.db.dao.DomibusConnectorPartyDao;
import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorPartyPK;
import org.springframework.stereotype.Repository;

@Repository("partyDao")
public class DomibusConnectorPartyDaoImpl implements DomibusConnectorPartyDao {

    @PersistenceContext //(unitName = "domibus.connector")
    private EntityManager em;

    @Override
    public DomibusConnectorParty getParty(String partyId, String role) {
        DomibusConnectorPartyPK key = new DomibusConnectorPartyPK(partyId, role);

        return em.find(DomibusConnectorParty.class, key);
    }

    @Override
    public DomibusConnectorParty getPartyByPartyId(String partyId) {
        Query q = em.createQuery("from DomibusConnectorParty p where p.partyId = :partyId");

        q.setParameter("partyId", partyId);

        return (DomibusConnectorParty) q.getSingleResult();
    }
}
