package eu.domibus.connector.common.db.dao.impl;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class DomibusConnectorConnectorMonitoringDao extends JdbcDaoSupport {

    public long selectTimerIntervalForJob(String jobName) {
        String sql = "select repeat_interval from dcon_qrtz_simple_triggers where trigger_name = ?";

        return getJdbcTemplate().queryForLong(sql, jobName);
    }

    public long selectLastCalledTrigger(String jobName) {
        String sql = "select PREV_FIRE_TIME from DCON_QRTZ_TRIGGERS where trigger_name = ?";

        return getJdbcTemplate().queryForLong(sql, jobName);
    }

    public String selectStatusTrigger(String jobName) {
        String sql = "select TRIGGER_STATE from DCON_QRTZ_TRIGGERS where trigger_name = ?";
        String[] parameter = new String[1];
        parameter[0] = jobName;
        return getJdbcTemplate().queryForObject(sql, parameter, String.class);
    }

    public Integer countRejectedMessagesConnector() {
        String sql = "select count(*) from DOMIBUS_CONNECTOR_MESSAGES where rejected != null";

        return getJdbcTemplate().queryForInt(sql);
    }

    public Integer countNoReceiptMessagesGateway() {
        String sql = "select count(*) from TB_RECEIPT_TRACKING where status = 'NO_RECEIPT'";

        return getJdbcTemplate().queryForInt(sql);
    }

    public Integer countPendingMessagesGateway() {
        String sql = "select count(*) from TB_RECEIPT_TRACKING where status = 'IN_PROCESS'";

        return getJdbcTemplate().queryForInt(sql);
    }

    public Integer countPendingMessagesConnector() {
        String sql = "select count(*) from DOMIBUS_CONNECTOR_MESSAGES where confirmed is null and rejected is null";

        return getJdbcTemplate().queryForInt(sql);
    }

}
