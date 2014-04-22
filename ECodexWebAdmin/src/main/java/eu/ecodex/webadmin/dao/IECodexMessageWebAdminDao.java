package eu.ecodex.webadmin.dao;

import java.util.HashMap;

public interface IECodexMessageWebAdminDao {

    public Long countOutgoingMessages();

    public Long countIncomingMessages();

    public HashMap<String, Long> countService();

}
