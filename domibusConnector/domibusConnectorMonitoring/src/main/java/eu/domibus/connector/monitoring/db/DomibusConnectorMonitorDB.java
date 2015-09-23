package eu.domibus.connector.monitoring.db;

import eu.domibus.connector.monitoring.DomibusConnectorMonitor;
import eu.domibus.connector.monitoring.IDomibusConnectorMonitor;

public class DomibusConnectorMonitorDB implements IDomibusConnectorMonitor {

    private DomibusConnectorMonitor domibusConnectorMonitor;

    @Override
    public Long getCheckOutgoingRepeatInterval() {
        return domibusConnectorMonitor.getCheckOutgoingRepeatInterval();
    }

    @Override
    public Long getLastCalledIncoming() {
        return domibusConnectorMonitor.getLastCalledIncoming();
    }

    @Override
    public Long getLastCalledOutgoing() {
        return domibusConnectorMonitor.getLastCalledOutgoing();
    }

    @Override
    public Long getLastCalledCheckEvidencesTimeout() {
        return domibusConnectorMonitor.getLastCalledCheckEvidencesTimeout();
    }

    @Override
    public String getJobStatusIncoming() {
        return domibusConnectorMonitor.getStatusIncoming();
    }

    @Override
    public String getJobStatusOutgoing() {
        return domibusConnectorMonitor.getStatusOutgoing();
    }

    @Override
    public String getJobStatusEvidencesTimeout() {
        return domibusConnectorMonitor.getStatusEvidencesTimeout();
    }

    @Override
    public Integer getRejectedConnectorMessagesCount() {
        return domibusConnectorMonitor.getRejectedConnectorMessagesCount();
    }

    //
    // @Override
    // public Integer getNoReceiptMessagesGateway() {
    // return domibusConnectorMonitor.getNoReceiptMessagesGateway();
    // }
    //
    // @Override
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
