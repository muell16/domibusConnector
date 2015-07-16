package eu.ecodex.connector.common.db.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import eu.ecodex.connector.common.db.dao.ECodexPartyDao;
import eu.ecodex.connector.common.db.model.ECodexParty;
import eu.ecodex.connector.common.db.model.ECodexPartyPK;

public class ECodexPartyDaoImpl implements ECodexPartyDao {

    @PersistenceContext(unitName = "ecodex.connector")
    private EntityManager em;

    @Override
    public ECodexParty getParty(String partyId, String role) {
        ECodexPartyPK key = new ECodexPartyPK(partyId, role);

        return em.find(ECodexParty.class, key);
    }

    @Override
    public ECodexParty getPartyByPartyId(String partyId) {
        Query q = em.createQuery("from ECodexParty p where p.partyId = ?");

        q.setParameter(1, partyId);

        return (ECodexParty) q.getSingleResult();
    }
}
