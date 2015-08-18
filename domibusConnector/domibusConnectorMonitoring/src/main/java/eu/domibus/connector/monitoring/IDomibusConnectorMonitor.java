package eu.domibus.connector.monitoring;

public interface IDomibusConnectorMonitor {

    public Long getCheckOutgoingRepeatInterval();

    public Long getLastCalledIncoming();

    public Long getLastCalledOutgoing();

    public Long getLastCalledCheckEvidencesTimeout();

    public String getJobStatusIncoming();

    public String getJobStatusOutgoing();

    public String getJobStatusEvidencesTimeout();

    public Integer getRejectedConnectorMessagesCount();

    public Integer getNoReceiptMessagesGateway();

    public Integer getPendingMessagesGateway();

}