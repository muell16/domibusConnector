package eu.domibus.webadmin.persistence.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import eu.domibus.connector.persistence.model.PDomibusConnectorMessageInfo;

@Deprecated //will be moved to persistence
public interface IDomibusMessageWebAdminDao {

    public Long countOutgoingMessages();

    public Long countIncomingMessages();

    public HashMap<String, Long> countService(String service);

    public List<PDomibusConnectorMessageInfo> findMessageByDate(Date fromDate, Date toDate);
    
    public HashMap<String, Long> countUndefinedService();

}
