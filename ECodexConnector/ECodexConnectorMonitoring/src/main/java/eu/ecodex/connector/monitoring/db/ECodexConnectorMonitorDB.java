package eu.ecodex.connector.monitoring.db;

import eu.ecodex.connector.monitoring.ECodexConnectorMonitor;
import eu.ecodex.connector.monitoring.IECodexConnectorMonitor;

public class ECodexConnectorMonitorDB implements IECodexConnectorMonitor {

    private ECodexConnectorMonitor ecodexConnectorMonitor;

    @Override
    public Long getCheckOutgoingRepeatInterval() {
        return ecodexConnectorMonitor.getCheckOutgoingRepeatInterval();
    }

    @Override
    public Long getLastCalledIncoming() {
        return ecodexConnectorMonitor.getLastCalledIncoming();
    }

    @Override
    public Long getLastCalledOutgoing() {
        return ecodexConnectorMonitor.getLastCalledOutgoing();
    }

    @Override
    public Long getLastCalledCheckEvidencesTimeout() {
        return ecodexConnectorMonitor.getLastCalledCheckEvidencesTimeout();
    }

    @Override
    public String getJobStatusIncoming() {
        return ecodexConnectorMonitor.getStatusIncoming();
    }

    @Override
    public String getJobStatusOutgoing() {
        return ecodexConnectorMonitor.getStatusOutgoing();
    }

    @Override
    public String getJobStatusEvidencesTimeout() {
        return ecodexConnectorMonitor.getStatusEvidencesTimeout();
    }

    @Override
    public Integer getRejectedConnectorMessagesCount() {
        return ecodexConnectorMonitor.getRejectedConnectorMessagesCount();
    }

    @Override
    public Integer getNoReceiptMessagesGateway() {
        return ecodexConnectorMonitor.getNoReceiptMessagesGateway();
    }

    public void setEcodexConnectorMonitor(ECodexConnectorMonitor ecodexConnectorMonitor) {
        this.ecodexConnectorMonitor = ecodexConnectorMonitor;
    }

    public ECodexConnectorMonitor getEcodexConnectorMonitor() {
        return ecodexConnectorMonitor;
    }

}
