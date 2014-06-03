package eu.ecodex.connector.monitoring;

import java.util.Date;

public class ECodexConnectorMonitor {

    private Long timerPeriod;
    private Date lastCalledOutgoingMessagesPending;
    private Date lastCalledIncomingMessagesPending;
    private Date lastCalledEvidenceTimeoutCheck;

    public Long getTimerPeriod() {
        return timerPeriod;
    }

    public void setTimerPeriod(Long timerPeriod) {
        this.timerPeriod = timerPeriod;
    }

    public Date getLastCalledOutgoingMessagesPending() {
        return lastCalledOutgoingMessagesPending;
    }

    public void setLastCalledOutgoingMessagesPending(Date lastCalledOutgoingMessagesPending) {
        this.lastCalledOutgoingMessagesPending = lastCalledOutgoingMessagesPending;
    }

    public Date getLastCalledIncomingMessagesPending() {
        return lastCalledIncomingMessagesPending;
    }

    public void setLastCalledIncomingMessagesPending(Date lastCalledIncomingMessagesPending) {
        this.lastCalledIncomingMessagesPending = lastCalledIncomingMessagesPending;
    }

    public Date getLastCalledEvidenceTimeoutCheck() {
        return lastCalledEvidenceTimeoutCheck;
    }

    public void setLastCalledEvidenceTimeoutCheck(Date lastCalledEvidenceTimeoutCheck) {
        this.lastCalledEvidenceTimeoutCheck = lastCalledEvidenceTimeoutCheck;
    }
}
