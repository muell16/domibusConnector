package eu.ecodex.connector.monitoring.rest;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import eu.ecodex.connector.monitoring.ECodexConnectorMonitor;

@Consumes("application/json")
@Produces("application/json")
public class ECodexConnectorRestMonitor {

    private ECodexConnectorMonitor ecodexMonitor;

    @POST
    @Path("/getTimerPeriod/")
    public Long getTimerPeriod() {
        return ecodexMonitor.getTimerPeriod();
    }

    @POST
    @Path("/getLastCalledOutgoingMessagesPending/")
    public Date getLastCalledOutgoingMessagesPending() {
        return ecodexMonitor.getLastCalledOutgoingMessagesPending();
    }

    @POST
    @Path("/getLastCalledIncomingMessagesPending/")
    public Date getLastCalledIncomingMessagesPending() {
        return ecodexMonitor.getLastCalledIncomingMessagesPending();
    }

    @POST
    @Path("/getLastCalledEvidenceTimeoutCheck/")
    public Date getLastCalledEvidenceTimeoutCheck() {
        return ecodexMonitor.getLastCalledEvidenceTimeoutCheck();
    }

    public void setEcodexMonitor(ECodexConnectorMonitor ecodexMonitor) {
        this.ecodexMonitor = ecodexMonitor;
    }

}
