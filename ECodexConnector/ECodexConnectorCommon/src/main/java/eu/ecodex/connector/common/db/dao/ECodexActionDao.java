package eu.ecodex.connector.common.db.dao;

import eu.ecodex.connector.common.db.model.ECodexAction;

public interface ECodexActionDao {

    ECodexAction getAction(String actionName);
}