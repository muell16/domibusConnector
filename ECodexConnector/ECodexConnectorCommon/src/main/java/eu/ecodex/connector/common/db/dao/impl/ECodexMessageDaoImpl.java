package eu.ecodex.connector.common.db.dao.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.ecodex.connector.common.db.dao.ECodexMessageDao;
import eu.ecodex.connector.common.db.model.ECodexMessage;

@Repository
public class ECodexMessageDaoImpl implements ECodexMessageDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void saveNewMessage(ECodexMessage message) {
        message.setUpdated(new Date());
        em.persist(message);
    }

    @Override
    public void mergeMessage(ECodexMessage message) {
        message.setUpdated(new Date());
        em.merge(message);
    }

    @Override
    public ECodexMessage findMessageByNationalId(String nationalMessageId) {
        Query q = em.createQuery("from ECodexMessage m where m.nationalMessageId=?");
        q.setParameter(0, nationalMessageId);

        return (ECodexMessage) q.getSingleResult();
    }

    @Override
    public ECodexMessage findMessageByEbmsId(String ebmsMessageId) {
        Query q = em.createQuery("from ECodexMessage m where m.ebmsMessageId=?");
        q.setParameter(0, ebmsMessageId);

        return (ECodexMessage) q.getSingleResult();
    }
}
