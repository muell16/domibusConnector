package eu.ecodex.connector.monitoring.jmx;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import eu.ecodex.connector.monitoring.ECodexConnectorMonitor;
import eu.ecodex.connector.monitoring.IECodexConnectorMonitor;

@ManagedResource(objectName = "ECodexConnector:name=ECodexConnectorJMXMonitor", description = "MBean for monitoring the ECodexConnector")
public class ECodexConnectorMonitorJMX implements IECodexConnectorMonitor {

    private ECodexConnectorMonitor ecodexConnectorMonitor;

    @Override
    @ManagedAttribute(description = "The time period between two calls of the timer job CheckOutgoing")
    public Long getCheckOutgoingRepeatInterval() {
        return ecodexConnectorMonitor.getCheckOutgoingRepeatInterval();
    }

    @Override
    @ManagedAttribute(description = "The last time the job checkIncomingTrigger was called")
    public Long getLastCalledIncoming() {
        return ecodexConnectorMonitor.getLastCalledIncoming();
    }

    @Override
    @ManagedAttribute(description = "The last time the job checkOutgoingTrigger was called")
    public Long getLastCalledOutgoing() {
        return ecodexConnectorMonitor.getLastCalledOutgoing();
    }

    @Override
    @ManagedAttribute(description = "The last time the job checkEvidencesTimeoutTrigger was called")
    public Long getLastCalledCheckEvidencesTimeout() {
        return ecodexConnectorMonitor.getLastCalledCheckEvidencesTimeout();
    }

    @Override
    @ManagedAttribute(description = "The status of the job checkIncomingTrigger")
    public String getJobStatusIncoming() {
        return ecodexConnectorMonitor.getStatusIncoming();
    }

    @Override
    @ManagedAttribute(description = "The status of the job checkOutgoingTrigger")
    public String getJobStatusOutgoing() {
        return ecodexConnectorMonitor.getStatusOutgoing();
    }

    @Override
    @ManagedAttribute(description = "The status of the job checkEvidencesTimeoutTrigger")
    public String getJobStatusEvidencesTimeout() {
        return ecodexConnectorMonitor.getStatusEvidencesTimeout();
    }

    @Override
    @ManagedAttribute(description = "Count of rejected Connector messages")
    public Integer getRejectedConnectorMessagesCount() {
        return ecodexConnectorMonitor.getRejectedConnectorMessagesCount();
    }

    @Override
    @ManagedAttribute(description = "Count of gateway messages in state NO_RECEIPT")
    public Integer getNoReceiptMessagesGateway() {
        return ecodexConnectorMonitor.getNoReceiptMessagesGateway();
    }

    @Override
    @ManagedAttribute(description = "Count of pending gateway messages")
    public Integer getPendingMessagesGateway() {
        return ecodexConnectorMonitor.getPendingMessagesGateway();
    }

    public ECodexConnectorMonitor getEcodexConnectorMonitor() {
        return ecodexConnectorMonitor;
    }

    public void setEcodexConnectorMonitor(ECodexConnectorMonitor ecodexConnectorMonitor) {
        this.ecodexConnectorMonitor = ecodexConnectorMonitor;
    }
}
