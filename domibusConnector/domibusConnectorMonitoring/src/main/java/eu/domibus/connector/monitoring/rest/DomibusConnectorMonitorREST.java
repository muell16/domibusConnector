package eu.domibus.connector.monitoring.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import eu.domibus.connector.monitoring.DomibusConnectorMonitor;
import eu.domibus.connector.monitoring.IDomibusConnectorMonitor;

@Consumes("application/json")
@Produces("application/json")
public class DomibusConnectorMonitorREST implements IDomibusConnectorMonitor {

    private DomibusConnectorMonitor domibusConnectorMonitor;

    @Override
    @POST
    @Path("/getOutgoingRepeatInterval/")
    public Long getCheckOutgoingRepeatInterval() {
        return domibusConnectorMonitor.getCheckOutgoingRepeatInterval();
    }

    @Override
    @POST
    @Path("/getLastCalledIncoming/")
    public Long getLastCalledIncoming() {
        return domibusConnectorMonitor.getLastCalledIncoming();
    }

    @Override
    @POST
    @Path("/getLastCalledOutgoing/")
    public Long getLastCalledOutgoing() {
        return domibusConnectorMonitor.getLastCalledOutgoing();
    }

    @Override
    @POST
    @Path("/getLastCalledCheckEvidencesTimeout/")
    public Long getLastCalledCheckEvidencesTimeout() {
        return domibusConnectorMonitor.getLastCalledCheckEvidencesTimeout();
    }

    @Override
    @POST
    @Path("/getJobStatusIncoming/")
    public String getJobStatusIncoming() {
        return domibusConnectorMonitor.getStatusIncoming();
    }

    @Override
    @POST
    @Path("/getJobStatusOutgoing/")
    public String getJobStatusOutgoing() {
        return domibusConnectorMonitor.getStatusOutgoing();
    }

    @Override
    @POST
    @Path("/getJobStatusEvidencesTimeout/")
    public String getJobStatusEvidencesTimeout() {
        return domibusConnectorMonitor.getStatusEvidencesTimeout();
    }

    @Override
    @POST
    @Path("/getRejectedConnectorMessagesCount/")
    public Integer getRejectedConnectorMessagesCount() {
        return domibusConnectorMonitor.getRejectedConnectorMessagesCount();
    }

    //
    // @Override
    // @POST
    // @Path("/getNoReceiptMessagesGateway/")
    // public Integer getNoReceiptMessagesGateway() {
    // return domibusConnectorMonitor.getNoReceiptMessagesGateway();
    // }
    //
    // @Override
    // @POST
    // @Path("/getPendingGatewayMessagesCount/")
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
