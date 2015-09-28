package eu.domibus.connector.common.db.dao.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import eu.domibus.connector.common.db.dao.DomibusConnectorMessageInfoDao;
import eu.domibus.connector.common.db.model.DomibusConnectorMessage;
import eu.domibus.connector.common.db.model.DomibusConnectorMessageInfo;
import eu.domibus.connector.common.exception.PersistenceException;

public class DomibusConnectorMessageInfoDaoImpl implements DomibusConnectorMessageInfoDao {

    @PersistenceContext(unitName = "domibus.connector")
    private EntityManager em;

    @Override
    public void persistMessageInfo(DomibusConnectorMessageInfo messageInfo) {
        Date creationDate = new Date();
        messageInfo.setCreated(creationDate);
        messageInfo.setUpdated(creationDate);

        em.persist(messageInfo);
    }

    @Override
    public void mergeMessageInfo(DomibusConnectorMessageInfo messageInfo) {
        messageInfo.setUpdated(new Date());

        em.merge(messageInfo);
    }

    @Override
    public DomibusConnectorMessageInfo getMessageInfoForMessage(DomibusConnectorMessage message)
            throws PersistenceException {
        Query q = em.createQuery("from DomibusConnectorMessageInfo i where i.message = ?");
        q.setParameter(1, message.getId());

        DomibusConnectorMessageInfo messageInfo = null;
        try {
            messageInfo = (DomibusConnectorMessageInfo) q.getSingleResult();
        } catch (NoResultException nre) {
            throw new PersistenceException("No message info object found for message with id " + message.getId());
        } catch (NonUniqueResultException nur) {
            throw new PersistenceException("More than one message info object found for message with id "
                    + message.getId());
        }
        return messageInfo;
    }

}
