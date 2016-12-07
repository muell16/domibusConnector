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
import eu.domibus.connector.common.enums.EvidenceType;
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
        message.setRejected(null);
        message = mergeMessage(message);
        return message;
    }

    @Override
    public DomibusConnectorMessage rejectMessage(DomibusConnectorMessage message) {
        message.setRejected(new Date());
        message.setConfirmed(null);
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
    public List<DomibusConnectorMessage> findOutgoingMessagesNotRejectedAndWithoutDelivery(){
    	Query q = em
                .createQuery("from DomibusConnectorMessage m where m.rejected is null and m.direction = :dir and m.deliveredToGateway is not null "
                		+ "and not exists (select 1 from DomibusConnectorEvidence e where e.message = m and (e.type=:type1 or e.type=:type2))");
        q.setParameter("dir", MessageDirection.NAT_TO_GW);
        q.setParameter("type1", EvidenceType.DELIVERY.name());
        q.setParameter("type2", EvidenceType.NON_DELIVERY.name());

        return q.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<DomibusConnectorMessage> findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD(){
    	Query q = em
                .createQuery("from DomibusConnectorMessage m where m.confirmed is null and m.rejected is null and m.direction = :dir and m.deliveredToGateway is not null "
                		+ "and not exists (select 1 from DomibusConnectorEvidence e where e.message = m and (e.type=:type1 or e.type=:type2))");
        q.setParameter("dir", MessageDirection.NAT_TO_GW);
        q.setParameter("type1", EvidenceType.RELAY_REMMD_ACCEPTANCE.name());
        q.setParameter("type2", EvidenceType.RELAY_REMMD_REJECTION.name());

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
