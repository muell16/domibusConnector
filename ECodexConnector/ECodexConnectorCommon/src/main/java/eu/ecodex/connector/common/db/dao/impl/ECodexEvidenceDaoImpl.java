package eu.ecodex.connector.common.db.dao.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.ecodex.connector.common.db.dao.ECodexEvidenceDao;
import eu.ecodex.connector.common.db.model.ECodexEvidence;

@Repository
public class ECodexEvidenceDaoImpl implements ECodexEvidenceDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void saveEvidence(ECodexEvidence evidence) {
        evidence.setUpdated(new Date());
        em.persist(evidence);
    }
}
