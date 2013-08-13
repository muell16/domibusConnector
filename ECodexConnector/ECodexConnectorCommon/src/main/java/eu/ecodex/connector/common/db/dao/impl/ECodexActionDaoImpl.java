package eu.ecodex.connector.common.db.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import eu.ecodex.connector.common.db.dao.ECodexActionDao;
import eu.ecodex.connector.common.db.model.ECodexAction;

public class ECodexActionDaoImpl implements ECodexActionDao {

    @PersistenceContext(unitName = "ecodex.connector")
    private EntityManager em;

    @Override
    public ECodexAction getAction(String actionName) {
        ECodexAction action = em.find(ECodexAction.class, actionName);

        return action;
    }
}
