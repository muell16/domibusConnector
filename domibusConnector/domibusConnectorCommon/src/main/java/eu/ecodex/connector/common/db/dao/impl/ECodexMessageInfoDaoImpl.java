package eu.ecodex.connector.common.db.dao.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import eu.ecodex.connector.common.db.dao.ECodexMessageInfoDao;
import eu.ecodex.connector.common.db.model.ECodexMessage;
import eu.ecodex.connector.common.db.model.ECodexMessageInfo;
import eu.ecodex.connector.common.exception.PersistenceException;

public class ECodexMessageInfoDaoImpl implements ECodexMessageInfoDao {

    @PersistenceContext(unitName = "ecodex.connector")
    private EntityManager em;

    @Override
    public void persistMessageInfo(ECodexMessageInfo messageInfo) {
        Date creationDate = new Date();
        messageInfo.setCreated(creationDate);
        messageInfo.setUpdated(creationDate);

        em.persist(messageInfo);
    }

    @Override
    public void mergeMessageInfo(ECodexMessageInfo messageInfo) {
        messageInfo.setUpdated(new Date());

        em.merge(messageInfo);
    }

    @Override
    public ECodexMessageInfo getMessageInfoForMessage(ECodexMessage message) throws PersistenceException {
        Query q = em.createQuery("from ECodexMessageInfo i where i.message = ?");
        q.setParameter(1, message.getId());

        ECodexMessageInfo messageInfo = null;
        try {
            messageInfo = (ECodexMessageInfo) q.getSingleResult();
        } catch (NoResultException nre) {
            throw new PersistenceException("No message info object found for message with id " + message.getId());
        } catch (NonUniqueResultException nur) {
            throw new PersistenceException("More than one message info object found for message with id "
                    + message.getId());
        }
        return messageInfo;
    }

}
