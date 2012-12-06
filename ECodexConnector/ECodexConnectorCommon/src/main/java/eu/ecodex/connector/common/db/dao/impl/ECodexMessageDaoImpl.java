package eu.ecodex.connector.common.db.dao.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
