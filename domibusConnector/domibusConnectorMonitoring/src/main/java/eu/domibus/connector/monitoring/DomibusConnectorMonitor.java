package eu.domibus.connector.monitoring;

//import eu.domibus.connector.common.db.dao.DomibusConnectorConnectorMonitoringDao;

public class DomibusConnectorMonitor {

    private static final String CHECK_OUTGOING_TRIGGER_NAME = "checkOutgoingTrigger";
    private static final String CHECK_INCOMING_TRIGGER_NAME = "checkIncomingTrigger";
    private static final String CHECK_EVIDENCES_TIMEOUT_TRIGGER_NAME = "checkEvidencesTimeoutTrigger";

//    private DomibusConnectorConnectorMonitoringDao monitoringDao;

    public Long getCheckOutgoingRepeatInterval() {
        //return monitoringDao.selectTimerIntervalForJob(CHECK_OUTGOING_TRIGGER_NAME);
        return null;
    }

    public Long getLastCalledIncoming() {
        //return monitoringDao.selectLastCalledTrigger(CHECK_INCOMING_TRIGGER_NAME);
        return null;
    }

    public Long getLastCalledOutgoing() {
        //return monitoringDao.selectLastCalledTrigger(CHECK_OUTGOING_TRIGGER_NAME);
        return null;
    }

    public Long getLastCalledCheckEvidencesTimeout() {
        //return monitoringDao.selectLastCalledTrigger(CHECK_EVIDENCES_TIMEOUT_TRIGGER_NAME);
        return null;
    }

    public String getStatusIncoming() {
        //return monitoringDao.selectStatusTrigger(CHECK_INCOMING_TRIGGER_NAME);
        return null;
    }

    public String getStatusOutgoing() {
//        return monitoringDao.selectStatusTrigger(CHECK_OUTGOING_TRIGGER_NAME);
        return null;
    }

    public String getStatusEvidencesTimeout() {
//        return monitoringDao.selectStatusTrigger(CHECK_EVIDENCES_TIMEOUT_TRIGGER_NAME);
        return null;
    }

    public Integer getRejectedConnectorMessagesCount() {
//        return monitoringDao.countRejectedMessagesConnector();
        return null;
    }

    //
    // public Integer getNoReceiptMessagesGateway() {
    // return monitoringDao.countNoReceiptMessagesGateway();
    // }
    //
    // public Integer getPendingMessagesGateway() {
    // return monitoringDao.countPendingMessagesGateway();
    // }

//    public void setMonitoringDao(DomibusConnectorConnectorMonitoringDao monitoringDao) {
//        this.monitoringDao = monitoringDao;
//    }

}
