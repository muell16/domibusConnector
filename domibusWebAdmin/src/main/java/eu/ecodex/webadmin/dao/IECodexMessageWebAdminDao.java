package eu.ecodex.webadmin.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import eu.ecodex.connector.common.db.model.ECodexMessageInfo;

public interface IECodexMessageWebAdminDao {

    public Long countOutgoingMessages();

    public Long countIncomingMessages();

    public HashMap<String, Long> countService();

    public List<ECodexMessageInfo> findMessageByDate(Date fromDate, Date toDate);

}
