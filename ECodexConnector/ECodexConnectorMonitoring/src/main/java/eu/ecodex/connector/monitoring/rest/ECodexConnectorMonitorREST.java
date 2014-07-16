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
    @Path("/getOutgoingRepeatInterval/")
    public Long getCheckOutgoingRepeatInterval() {
        return ecodexConnectorMonitor.getCheckOutgoingRepeatInterval();
    }

    @Override
    @POST
    @Path("/getLastCalledIncoming/")
    public Long getLastCalledIncoming() {
        return ecodexConnectorMonitor.getLastCalledIncoming();
    }

    @Override
    @POST
    @Path("/getLastCalledOutgoing/")
    public Long getLastCalledOutgoing() {
        return ecodexConnectorMonitor.getLastCalledOutgoing();
    }

    @Override
    @POST
    @Path("/getLastCalledCheckEvidencesTimeout/")
    public Long getLastCalledCheckEvidencesTimeout() {
        return ecodexConnectorMonitor.getLastCalledCheckEvidencesTimeout();
    }

    @Override
    @POST
    @Path("/getJobStatusIncoming/")
    public String getJobStatusIncoming() {
        return ecodexConnectorMonitor.getStatusIncoming();
    }

    @Override
    @POST
    @Path("/getJobStatusOutgoing/")
    public String getJobStatusOutgoing() {
        return ecodexConnectorMonitor.getStatusOutgoing();
    }

    @Override
    @POST
    @Path("/getJobStatusEvidencesTimeout/")
    public String getJobStatusEvidencesTimeout() {
        return ecodexConnectorMonitor.getStatusEvidencesTimeout();
    }

    @Override
    @POST
    @Path("/getRejectedConnectorMessagesCount/")
    public Integer getRejectedConnectorMessagesCount() {
        return ecodexConnectorMonitor.getRejectedConnectorMessagesCount();
    }

    @Override
    @POST
    @Path("/getNoReceiptMessagesGateway/")
    public Integer getNoReceiptMessagesGateway() {
        return ecodexConnectorMonitor.getNoReceiptMessagesGateway();
    }

    public ECodexConnectorMonitor getEcodexConnectorMonitor() {
        return ecodexConnectorMonitor;
    }

    public void setEcodexConnectorMonitor(ECodexConnectorMonitor ecodexConnectorMonitor) {
        this.ecodexConnectorMonitor = ecodexConnectorMonitor;
    }

}
