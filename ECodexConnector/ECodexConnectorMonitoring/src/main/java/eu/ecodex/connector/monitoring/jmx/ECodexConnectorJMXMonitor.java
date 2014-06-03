package eu.ecodex.connector.monitoring.jmx;

import java.util.Date;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import eu.ecodex.connector.monitoring.ECodexConnectorMonitor;

@ManagedResource(objectName = "ECodexConnector:name=ECodexConnectorJMXMonitor", description = "MBean for monitoring the ECodexConnector")
public class ECodexConnectorJMXMonitor {

    private ECodexConnectorMonitor ecodexMonitor;

    @ManagedAttribute(description = "The time period between the calls of the timer jobs in MS")
    public Long getTimerPeriod() {
        return ecodexMonitor.getTimerPeriod();
    }

    @ManagedAttribute(description = "timestamp when the last call of the timer job for check the national backend for pending outgoing messages happened")
    public Date getLastCalledOutgoingMessagesPending() {
        return ecodexMonitor.getLastCalledOutgoingMessagesPending();
    }

    @ManagedAttribute(description = "timestamp when the last call of the timer job for check the gateway backend for pending incoming messages happened")
    public Date getLastCalledIncomingMessagesPending() {
        return ecodexMonitor.getLastCalledIncomingMessagesPending();
    }

    @ManagedAttribute(description = "timestamp when the last call of the timer job for check messages without response when the timeout is active and set happened")
    public Date getLastCalledEvidenceTimeoutCheck() {
        return ecodexMonitor.getLastCalledEvidenceTimeoutCheck();
    }

    public void setEcodexMonitor(ECodexConnectorMonitor ecodexMonitor) {
        this.ecodexMonitor = ecodexMonitor;
    }

}
