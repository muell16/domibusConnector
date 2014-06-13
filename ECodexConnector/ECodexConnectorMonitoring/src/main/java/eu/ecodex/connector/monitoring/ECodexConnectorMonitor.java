package eu.ecodex.connector.monitoring;

import eu.ecodex.connector.monitoring.db.ECodexConnectorMonitoringDao;

public class ECodexConnectorMonitor {

    private static final String CHECK_OUTGOING_TRIGGER_NAME = "checkOutgoingTrigger";
    private static final String CHECK_INCOMING_TRIGGER_NAME = "checkIncomingTrigger";
    private static final String CHECK_EVIDENCES_TIMEOUT_TRIGGER_NAME = "checkEvidencesTimeoutTrigger";

    private ECodexConnectorMonitoringDao monitoringDao;

    public Long getCheckOutgoingRepeatInterval() {
        // return System.currentTimeMillis();
        return monitoringDao.selectTimerIntervalForJob(CHECK_OUTGOING_TRIGGER_NAME);
    }

    public void setMonitoringDao(ECodexConnectorMonitoringDao monitoringDao) {
        this.monitoringDao = monitoringDao;
    }
}
