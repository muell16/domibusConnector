package eu.domibus.connector.monitoring.jmx;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import eu.domibus.connector.monitoring.DomibusConnectorMonitor;
import eu.domibus.connector.monitoring.IDomibusConnectorMonitor;

@ManagedResource(objectName = "DomibusConnector:name=DomibusConnectorJMXMonitor", description = "MBean for monitoring the DomibusConnector")
public class DomibusConnectorMonitorJMX implements IDomibusConnectorMonitor {

    private DomibusConnectorMonitor domibusConnectorMonitor;

    @Override
    @ManagedAttribute(description = "The time period between two calls of the timer job CheckOutgoing")
    public Long getCheckOutgoingRepeatInterval() {
        return domibusConnectorMonitor.getCheckOutgoingRepeatInterval();
    }

    @Override
    @ManagedAttribute(description = "The last time the job checkIncomingTrigger was called")
    public Long getLastCalledIncoming() {
        return domibusConnectorMonitor.getLastCalledIncoming();
    }

    @Override
    @ManagedAttribute(description = "The last time the job checkOutgoingTrigger was called")
    public Long getLastCalledOutgoing() {
        return domibusConnectorMonitor.getLastCalledOutgoing();
    }

    @Override
    @ManagedAttribute(description = "The last time the job checkEvidencesTimeoutTrigger was called")
    public Long getLastCalledCheckEvidencesTimeout() {
        return domibusConnectorMonitor.getLastCalledCheckEvidencesTimeout();
    }

    @Override
    @ManagedAttribute(description = "The status of the job checkIncomingTrigger")
    public String getJobStatusIncoming() {
        return domibusConnectorMonitor.getStatusIncoming();
    }

    @Override
    @ManagedAttribute(description = "The status of the job checkOutgoingTrigger")
    public String getJobStatusOutgoing() {
        return domibusConnectorMonitor.getStatusOutgoing();
    }

    @Override
    @ManagedAttribute(description = "The status of the job checkEvidencesTimeoutTrigger")
    public String getJobStatusEvidencesTimeout() {
        return domibusConnectorMonitor.getStatusEvidencesTimeout();
    }

    @Override
    @ManagedAttribute(description = "Count of rejected Connector messages")
    public Integer getRejectedConnectorMessagesCount() {
        return domibusConnectorMonitor.getRejectedConnectorMessagesCount();
    }

    //
    // @Override
    // @ManagedAttribute(description =
    // "Count of gateway messages in state NO_RECEIPT")
    // public Integer getNoReceiptMessagesGateway() {
    // return domibusConnectorMonitor.getNoReceiptMessagesGateway();
    // }
    //
    // @Override
    // @ManagedAttribute(description = "Count of pending gateway messages")
    // public Integer getPendingMessagesGateway() {
    // return domibusConnectorMonitor.getPendingMessagesGateway();
    // }

    public DomibusConnectorMonitor getDomibusConnectorMonitor() {
        return domibusConnectorMonitor;
    }

    public void setDomibusConnectorMonitor(DomibusConnectorMonitor domibusConnectorMonitor) {
        this.domibusConnectorMonitor = domibusConnectorMonitor;
    }

}
