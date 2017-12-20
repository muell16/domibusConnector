package eu.domibus.connector.persistence.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import eu.domibus.connector.common.db.dao.DomibusConnectorMessageErrorDao;
import eu.domibus.connector.persistence.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.model.DomibusConnectorMessageError;
import org.springframework.stereotype.Repository;

@Repository("messageErrorDao")
public class DomibusConnectorMessageErrorDaoImpl implements DomibusConnectorMessageErrorDao {

    @PersistenceContext //(unitName = "domibus.connector")
    private EntityManager em;

    @Override
    public void persistMessageError(DomibusConnectorMessageError messageError) {
        Date creationDate = new Date();
        messageError.setCreated(creationDate);

        em.persist(messageError);
    }

    @Override
    public void mergeMessageError(DomibusConnectorMessageError messageError) {
        em.merge(messageError);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DomibusConnectorMessageError> getErrorsForMessage(DomibusConnectorMessage message) {
        Query q = em.createQuery("from DomibusConnectorMessageError e where e.message = :msgId");
        q.setParameter("msgId", message.getId());

        return q.getResultList();
    }

}
