package eu.ecodex.connector.monitoring.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import eu.ecodex.connector.monitoring.ECodexConnectorMonitor;
import eu.ecodex.connector.monitoring.IECodexConnectorMonitor;

@Consumes("application/json")
@Produces("application/json")
public class ECodexConnectorMonitorREST implements IECodexConnectorMonitor {

    private ECodexConnectorMonitor ecodexConnectorMonitor;

    @Override
    @POST
    @Path("/checkOutgoingRepeatInterval/")
    public Long getCheckOutgoingRepeatInterval() {
        return ecodexConnectorMonitor.getCheckOutgoingRepeatInterval();
    }

    public ECodexConnectorMonitor getEcodexConnectorMonitor() {
        return ecodexConnectorMonitor;
    }

    public void setEcodexConnectorMonitor(ECodexConnectorMonitor ecodexConnectorMonitor) {
        this.ecodexConnectorMonitor = ecodexConnectorMonitor;
    }

    // @POST
    // @Path("/getTimerPeriod/")
    // public Long getTimerPeriod() {
    // return ecodexMonitor.getTimerPeriod();
    // }
    //
    // @POST
    // @Path("/getLastCalledOutgoingMessagesPending/")
    // public Date getLastCalledOutgoingMessagesPending() {
    // return ecodexMonitor.getLastCalledOutgoingMessagesPending();
    // }
    //
    // @POST
    // @Path("/getLastCalledIncomingMessagesPending/")
    // public Date getLastCalledIncomingMessagesPending() {
    // return ecodexMonitor.getLastCalledIncomingMessagesPending();
    // }
    //
    // @POST
    // @Path("/getLastCalledEvidenceTimeoutCheck/")
    // public Date getLastCalledEvidenceTimeoutCheck() {
    // return ecodexMonitor.getLastCalledEvidenceTimeoutCheck();
    // }

}
