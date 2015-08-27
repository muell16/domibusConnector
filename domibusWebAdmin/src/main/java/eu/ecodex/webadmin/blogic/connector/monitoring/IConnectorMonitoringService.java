package eu.ecodex.webadmin.blogic.connector.monitoring;

public interface IConnectorMonitoringService {

    public void generateMonitoringReport(boolean reconnect);

    public String getJobStatusIncoming();

    public String getConnectionStatus();

    public String getConnectionMessage();

    public String getJobStatusOutgoing();

    public String getNoReceiptMessagesGatewayStatus();

    public Integer getNoReceiptMessagesGateway();

}