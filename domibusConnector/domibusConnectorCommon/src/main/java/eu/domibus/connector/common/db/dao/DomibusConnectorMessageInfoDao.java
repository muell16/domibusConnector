package eu.domibus.connector.common.db.dao;

import eu.domibus.connector.common.db.model.DomibusConnectorMessage;
import eu.domibus.connector.common.db.model.DomibusConnectorMessageInfo;
import eu.domibus.connector.common.exception.PersistenceException;

public interface DomibusConnectorMessageInfoDao {

    void persistMessageInfo(DomibusConnectorMessageInfo messageInfo);

    void mergeMessageInfo(DomibusConnectorMessageInfo messageInfo);

    DomibusConnectorMessageInfo getMessageInfoForMessage(DomibusConnectorMessage message) throws PersistenceException;

}
