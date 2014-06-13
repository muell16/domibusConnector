package eu.ecodex.connector.monitoring.db;

import eu.ecodex.connector.monitoring.ECodexConnectorMonitor;
import eu.ecodex.connector.monitoring.IECodexConnectorMonitor;

public class ECodexConnectorMonitorDB implements IECodexConnectorMonitor {

    private ECodexConnectorMonitor ecodexConnectorMonitor;

    @Override
    public Long getCheckOutgoingRepeatInterval() {
        return ecodexConnectorMonitor.getCheckOutgoingRepeatInterval();
    }

    public ECodexConnectorMonitor getEcodexConnectorMonitor() {
        return ecodexConnectorMonitor;
    }

    public void setEcodexConnectorMonitor(ECodexConnectorMonitor ecodexConnectorMonitor) {
        this.ecodexConnectorMonitor = ecodexConnectorMonitor;
    }

}
