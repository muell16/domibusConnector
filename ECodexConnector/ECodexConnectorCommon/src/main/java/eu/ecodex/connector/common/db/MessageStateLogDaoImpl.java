package eu.ecodex.connector.common.db;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import eu.ecodex.connector.common.MessageState;

public class MessageStateLogDaoImpl implements MessageStateLogDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void saveMessageStateLog(String messageId, MessageState messageState) {
        em.persist(new MessageStateLog(messageId, messageState));
    }

}
