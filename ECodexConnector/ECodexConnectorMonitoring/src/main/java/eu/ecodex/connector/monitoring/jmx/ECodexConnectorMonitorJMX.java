package eu.ecodex.connector.monitoring.jmx;

import java.util.Date;

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

    @ManagedAttribute(description = "timestamp when the last call of the timer job for check the national backend for pending outgoing messages happened")
    public Date getLastCalledOutgoingMessagesPending() {
        return null;
    }

    @ManagedAttribute(description = "timestamp when the last call of the timer job for check the gateway backend for pending incoming messages happened")
    public Date getLastCalledIncomingMessagesPending() {
        return null;
    }

    @ManagedAttribute(description = "timestamp when the last call of the timer job for check messages without response when the timeout is active and set happened")
    public Date getLastCalledEvidenceTimeoutCheck() {
        return null;
    }

    public ECodexConnectorMonitor getEcodexConnectorMonitor() {
        return ecodexConnectorMonitor;
    }

    public void setEcodexConnectorMonitor(ECodexConnectorMonitor ecodexConnectorMonitor) {
        this.ecodexConnectorMonitor = ecodexConnectorMonitor;
    }
}
