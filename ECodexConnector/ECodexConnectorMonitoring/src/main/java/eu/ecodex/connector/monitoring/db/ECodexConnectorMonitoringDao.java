package eu.ecodex.connector.monitoring.db;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class ECodexConnectorMonitoringDao extends JdbcDaoSupport {

    public long selectTimerIntervalForJob(String jobName) {
        String sql = "select repeat_interval from econ_qrtz_simple_triggers where trigger_name = ?";

        return getJdbcTemplate().queryForLong(sql, jobName);
    }

}
