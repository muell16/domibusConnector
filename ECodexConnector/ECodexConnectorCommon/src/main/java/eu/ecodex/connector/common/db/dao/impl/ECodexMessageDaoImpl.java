package eu.ecodex.connector.common.db.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.ecodex.connector.common.db.dao.ECodexMessageDao;
import eu.ecodex.connector.common.db.model.ECodexMessage;
import eu.ecodex.connector.common.enums.ECodexMessageDirection;

@Repository
public class ECodexMessageDaoImpl implements ECodexMessageDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void saveNewMessage(ECodexMessage message) {
        message.setUpdated(new Date());
        try {
            em.persist(message);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    public ECodexMessage mergeMessage(ECodexMessage message) {
        message.setUpdated(new Date());
        return em.merge(message);
    }

    @Override
    @Transactional
    public ECodexMessage findMessageByNationalId(String nationalMessageId) {
        Query q = em.createQuery("from ECodexMessage m where m.nationalMessageId=?");
        q.setParameter(1, nationalMessageId);

        return (ECodexMessage) q.getSingleResult();
    }

    @Override
    @Transactional
    public ECodexMessage findMessageByEbmsId(String ebmsMessageId) {
        Query q = em.createQuery("from ECodexMessage m where m.ebmsMessageId=?");
        q.setParameter(1, ebmsMessageId);

        return (ECodexMessage) q.getSingleResult();
    }

    @Override
    public List<ECodexMessage> findOutgoingUnconfirmedMessages() {
        Query q = em
                .createQuery("from ECodexMessage m where m.confirmed is null and m.direction = ? and m.deliveredToGateway is not null ");
        q.setParameter(1, ECodexMessageDirection.NAT_TO_GW);

        return q.getResultList();
    }

    @Override
    public List<ECodexMessage> findIncomingUnconfirmedMessages() {
        Query q = em
                .createQuery("from ECodexMessage m where m.confirmed is null and m.direction = ? and m.deliveredToNationalSystem is not null ");
        q.setParameter(1, ECodexMessageDirection.GW_TO_NAT);

        return q.getResultList();
    }
}
