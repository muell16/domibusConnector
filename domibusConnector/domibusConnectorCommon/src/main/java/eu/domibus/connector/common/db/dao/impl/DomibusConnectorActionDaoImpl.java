package eu.domibus.connector.common.db.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import eu.domibus.connector.common.db.dao.DomibusConnectorActionDao;
import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import org.springframework.stereotype.Repository;

@Repository("actionDao")
public class DomibusConnectorActionDaoImpl implements DomibusConnectorActionDao {

    @PersistenceContext //(unitName = "domibus.connector")
    private EntityManager em;

    @Override
    public DomibusConnectorAction getAction(String actionName) {
        DomibusConnectorAction action = em.find(DomibusConnectorAction.class, actionName);

        return action;
    }
}
