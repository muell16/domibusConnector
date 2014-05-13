package eu.ecodex.connector.controller.jmx;

import java.util.Date;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(objectName = "ECodexConnector:name=ECodexConnectorJMXMonitor", description = "MBean for monitoring the ECodexConnector")
public class ECodexConnectorJMXMonitor {

    private Long timerPeriod;
    private Date lastCalledOutgoingMessagesPending;
    private Date lastCalledIncomingMessagesPending;
    private Date lastCalledEvidenceTimeoutCheck;

    @ManagedAttribute(description = "The time period between the calls of the timer jobs in MS")
    public Long getTimerPeriod() {
        return timerPeriod;
    }

    public void setTimerPeriod(Long timerPeriod) {
        this.timerPeriod = timerPeriod;
    }

    @ManagedAttribute(description = "timestamp when the last call of the timer job for check the national backend for pending outgoing messages happened")
    public Date getLastCalledOutgoingMessagesPending() {
        return lastCalledOutgoingMessagesPending;
    }

    public void setLastCalledOutgoingMessagesPending(Date lastCalledOutgoingMessagesPending) {
        this.lastCalledOutgoingMessagesPending = lastCalledOutgoingMessagesPending;
    }

    @ManagedAttribute(description = "timestamp when the last call of the timer job for check the gateway backend for pending incoming messages happened")
    public Date getLastCalledIncomingMessagesPending() {
        return lastCalledIncomingMessagesPending;
    }

    public void setLastCalledIncomingMessagesPending(Date lastCalledIncomingMessagesPending) {
        this.lastCalledIncomingMessagesPending = lastCalledIncomingMessagesPending;
    }

    @ManagedAttribute(description = "timestamp when the last call of the timer job for check messages without response when the timeout is active and set happened")
    public Date getLastCalledEvidenceTimeoutCheck() {
        return lastCalledEvidenceTimeoutCheck;
    }

    public void setLastCalledEvidenceTimeoutCheck(Date lastCalledEvidenceTimeoutCheck) {
        this.lastCalledEvidenceTimeoutCheck = lastCalledEvidenceTimeoutCheck;
    }

}
