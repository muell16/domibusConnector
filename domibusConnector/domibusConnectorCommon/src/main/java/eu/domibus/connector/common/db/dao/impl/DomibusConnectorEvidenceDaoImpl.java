package eu.domibus.connector.common.db.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import eu.domibus.connector.common.db.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.common.db.model.DomibusConnectorEvidence;
import eu.domibus.connector.common.db.model.DomibusConnectorMessage;

@Repository("evidenceDao")
public class DomibusConnectorEvidenceDaoImpl implements DomibusConnectorEvidenceDao {

    @PersistenceContext //(unitName = "domibus.connector")
    private EntityManager em;

    @Override
    public void saveNewEvidence(DomibusConnectorEvidence evidence) {
        evidence.setUpdated(new Date());
        em.persist(evidence);
    }

//    public DomibusConnectorEvidence getDeliveredNonDeliveredForMessage(String ebmsMessageId) {
//        Query q = createEvidenceQueryForMessage(ebmsMessageId);
//        q.setParameter("type1", EvidenceType.DELIVERY.name());
//        q.setParameter("type2", EvidenceType.NON_DELIVERY.name());
//
//        return (DomibusConnectorEvidence) q.getSingleResult();
//    }
//
//    private Query createEvidenceQueryForMessage(String ebmsMessageId) {
//        Query q = em
//                .createQuery("from DomibusConnectorEvidence e where e.message.ebmsMessageId = :msgId and (e.type = :type1 or e.type = :type2)");
//        q.setParameter("msgId", ebmsMessageId);
//
//        return q;
//    }

    @Override
    public void mergeEvidence(DomibusConnectorEvidence evidence) {
        evidence.setUpdated(new Date());
        em.merge(evidence);

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DomibusConnectorEvidence> findEvidencesForMessage(DomibusConnectorMessage message) {
        Query q = em.createQuery("from DomibusConnectorEvidence e where e.message.id=:msgId");
        q.setParameter("msgId", message.getId());

        return q.getResultList();
    }

}
