package eu.ecodex.connector.monitoring;

import eu.domibus.connector.common.db.dao.impl.DomibusConnectorConnectorMonitoringDao;

public class ECodexConnectorMonitor {

    private static final String CHECK_OUTGOING_TRIGGER_NAME = "checkOutgoingTrigger";
    private static final String CHECK_INCOMING_TRIGGER_NAME = "checkIncomingTrigger";
    private static final String CHECK_EVIDENCES_TIMEOUT_TRIGGER_NAME = "checkEvidencesTimeoutTrigger";

    private DomibusConnectorConnectorMonitoringDao monitoringDao;

    public Long getCheckOutgoingRepeatInterval() {
        return monitoringDao.selectTimerIntervalForJob(CHECK_OUTGOING_TRIGGER_NAME);
    }

    public Long getLastCalledIncoming() {
        return monitoringDao.selectLastCalledTrigger(CHECK_INCOMING_TRIGGER_NAME);
    }

    public Long getLastCalledOutgoing() {
        return monitoringDao.selectLastCalledTrigger(CHECK_OUTGOING_TRIGGER_NAME);
    }

    public Long getLastCalledCheckEvidencesTimeout() {
        return monitoringDao.selectLastCalledTrigger(CHECK_EVIDENCES_TIMEOUT_TRIGGER_NAME);
    }

    public String getStatusIncoming() {
        return monitoringDao.selectStatusTrigger(CHECK_INCOMING_TRIGGER_NAME);
    }

    public String getStatusOutgoing() {
        return monitoringDao.selectStatusTrigger(CHECK_OUTGOING_TRIGGER_NAME);
    }

    public String getStatusEvidencesTimeout() {
        return monitoringDao.selectStatusTrigger(CHECK_EVIDENCES_TIMEOUT_TRIGGER_NAME);
    }

    public Integer getRejectedConnectorMessagesCount() {
        return monitoringDao.countRejectedMessagesConnector();
    }

    public Integer getNoReceiptMessagesGateway() {
        return monitoringDao.countNoReceiptMessagesGateway();
    }

    public Integer getPendingMessagesGateway() {
        return monitoringDao.countPendingMessagesGateway();
    }

    public void setMonitoringDao(DomibusConnectorConnectorMonitoringDao monitoringDao) {
        this.monitoringDao = monitoringDao;
    }

}
