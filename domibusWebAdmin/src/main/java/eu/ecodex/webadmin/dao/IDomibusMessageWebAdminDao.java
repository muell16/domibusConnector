package eu.ecodex.webadmin.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import eu.domibus.connector.common.db.model.DomibusConnectorMessageInfo;

public interface IDomibusMessageWebAdminDao {

    public Long countOutgoingMessages();

    public Long countIncomingMessages();

    public HashMap<String, Long> countService();

    public List<DomibusConnectorMessageInfo> findMessageByDate(Date fromDate, Date toDate);

}
