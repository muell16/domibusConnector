package eu.domibus.connector.common.db.dao;

public interface DomibusConnectorConnectorMonitoringDao {

    public long selectTimerIntervalForJob(String jobName);

    public long selectLastCalledTrigger(String jobName);

    public String selectStatusTrigger(String jobName);

    public Integer countRejectedMessagesConnector();

    public Integer countPendingMessagesConnector();

}