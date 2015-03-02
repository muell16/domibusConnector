package eu.ecodex.connector.common.db.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import eu.ecodex.connector.common.db.dao.ECodexEvidenceDao;
import eu.ecodex.connector.common.db.model.ECodexEvidence;
import eu.ecodex.connector.common.db.model.ECodexMessage;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;

@Repository
public class ECodexEvidenceDaoImpl implements ECodexEvidenceDao {

    @PersistenceContext(unitName = "ecodex.connector")
    private EntityManager em;

    @Override
    public void saveNewEvidence(ECodexEvidence evidence) {
        evidence.setUpdated(new Date());
        em.persist(evidence);
    }

    public ECodexEvidence getDeliveredNonDeliveredForMessage(String ebmsMessageId) {
        Query q = createEvidenceQueryForMessage(ebmsMessageId);
        q.setParameter(2, ECodexEvidenceType.DELIVERY.name());
        q.setParameter(3, ECodexEvidenceType.NON_DELIVERY.name());

        return (ECodexEvidence) q.getSingleResult();
    }

    private Query createEvidenceQueryForMessage(String ebmsMessageId) {
        Query q = em
                .createQuery("from ECodexEvidence e where e.message.ebmsMessageId = ? and (e.type = ? of e.type = ?)");
        q.setParameter(1, ebmsMessageId);

        return q;
    }

    @Override
    public void mergeEvidence(ECodexEvidence evidence) {
        evidence.setUpdated(new Date());
        em.merge(evidence);

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ECodexEvidence> findEvidencesForMessage(ECodexMessage message) {
        Query q = em.createQuery("from ECodexEvidence e where e.message.id=?");
        q.setParameter(1, message.getId());

        return q.getResultList();
    }

}
