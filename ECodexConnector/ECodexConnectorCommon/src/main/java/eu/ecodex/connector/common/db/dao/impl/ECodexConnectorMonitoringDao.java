package eu.ecodex.connector.common.db.dao.impl;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class ECodexConnectorMonitoringDao extends JdbcDaoSupport {

    public long selectTimerIntervalForJob(String jobName) {
        String sql = "select repeat_interval from econ_qrtz_simple_triggers where trigger_name = ?";

        return getJdbcTemplate().queryForLong(sql, jobName);
    }

    public long selectLastCalledTrigger(String jobName) {
        String sql = "select PREV_FIRE_TIME from ECON_QRTZ_TRIGGERS where trigger_name = ?";

        return getJdbcTemplate().queryForLong(sql, jobName);
    }

    public String selectStatusTrigger(String jobName) {
        String sql = "select TRIGGER_STATE from ECON_QRTZ_TRIGGERS where trigger_name = ?";
        String[] parameter = new String[1];
        parameter[0] = jobName;
        return getJdbcTemplate().queryForObject(sql, parameter, String.class);
    }

    public Integer countRejectedMessagesConnector() {
        String sql = "select count(*) from ECODEX_MESSAGES where rejected != null";

        return getJdbcTemplate().queryForInt(sql);
    }

    public Integer countNoReceiptMessagesGateway() {
        String sql = "select count(*) from TB_RECEIPT_TRACKING where status = 'NO_RECEIPT'";

        return getJdbcTemplate().queryForInt(sql);
    }

}
