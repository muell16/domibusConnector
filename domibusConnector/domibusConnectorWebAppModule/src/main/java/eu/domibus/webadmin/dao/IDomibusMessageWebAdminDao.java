package eu.domibus.webadmin.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import eu.domibus.connector.persistence.model.DomibusConnectorMessageInfo;

public interface IDomibusMessageWebAdminDao {

    public Long countOutgoingMessages();

    public Long countIncomingMessages();

    public HashMap<String, Long> countService(String service);

    public List<DomibusConnectorMessageInfo> findMessageByDate(Date fromDate, Date toDate);
    
    public HashMap<String, Long> countUndefinedService();

}
