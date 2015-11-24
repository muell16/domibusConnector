package eu.domibus.connector.common.db.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import eu.domibus.connector.common.db.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.common.db.model.DomibusConnectorMessage;
import eu.domibus.connector.common.enums.MessageDirection;

@Repository
public class DomibusConnectorMessageDaoImpl implements DomibusConnectorMessageDao {

    @PersistenceContext(unitName = "domibus.connector")
    private EntityManager em;

    @Override
    public void saveNewMessage(DomibusConnectorMessage message) {
        message.setUpdated(new Date());
        try {
            em.persist(message);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public DomibusConnectorMessage mergeMessage(DomibusConnectorMessage message) {
        message.setUpdated(new Date());
        return em.merge(message);
    }

    @Override
    public DomibusConnectorMessage findMessageByNationalId(String nationalMessageId) {
        Query q = em.createQuery("from DomibusConnectorMessage m where m.nationalMessageId=:msgId");
        q.setParameter("msgId", nationalMessageId);

        return (DomibusConnectorMessage) q.getSingleResult();
    }

    @Override
    public DomibusConnectorMessage confirmMessage(DomibusConnectorMessage message) {
        message.setConfirmed(new Date());
        message = mergeMessage(message);
        return message;
    }

    @Override
    public DomibusConnectorMessage rejectMessage(DomibusConnectorMessage message) {
        message.setRejected(new Date());
        message = mergeMessage(message);
        return message;
    }

    @Override
    public DomibusConnectorMessage findMessageByEbmsId(String ebmsMessageId) {
        Query q = em.createQuery("from DomibusConnectorMessage m where m.ebmsMessageId=:msgId");
        q.setParameter("msgId", ebmsMessageId);

        return (DomibusConnectorMessage) q.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DomibusConnectorMessage> findMessagesByConversationId(String conversationId) {
        Query q = em.createQuery("from DomibusConnectorMessage m where m.conversationId=:convId");
        q.setParameter("convId", conversationId);

        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DomibusConnectorMessage> findOutgoingUnconfirmedMessages() {
        Query q = em
                .createQuery("from DomibusConnectorMessage m where m.confirmed is null and m.rejected is null and m.direction = :dir and m.deliveredToGateway is not null ");
        q.setParameter("dir", MessageDirection.NAT_TO_GW);

        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DomibusConnectorMessage> findIncomingUnconfirmedMessages() {
        Query q = em
                .createQuery("from DomibusConnectorMessage m where m.confirmed is null and m.rejected is null and m.direction = :dir and m.deliveredToNationalSystem is not null ");
        q.setParameter("dir", MessageDirection.GW_TO_NAT);

        return q.getResultList();
    }
}
