package eu.domibus.connector.common.db.dao.impl;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import eu.domibus.connector.common.db.dao.DomibusConnectorConnectorMonitoringDao;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository("monitoringDao")
public class DomibusConnectorConnectorMonitoringDaoImpl extends JdbcDaoSupport implements DomibusConnectorConnectorMonitoringDao {

    public DomibusConnectorConnectorMonitoringDaoImpl(DataSource ds) {
        super.setDataSource(ds);
    }

    /* (non-Javadoc)
     * @see eu.domibus.connector.common.db.dao.impl.DomibusConnectorConnectorMonitoringDao#selectTimerIntervalForJob(java.lang.String)
     */
    @Override
    public long selectTimerIntervalForJob(String jobName) {
        String sql = "select repeat_interval from dcon_qrtz_simple_triggers where trigger_name = ?";

        return getJdbcTemplate().queryForObject(sql, Long.class, jobName);
    }

    /* (non-Javadoc)
     * @see eu.domibus.connector.common.db.dao.impl.DomibusConnectorConnectorMonitoringDao#selectLastCalledTrigger(java.lang.String)
     */
    @Override
    public long selectLastCalledTrigger(String jobName) {
        String sql = "select PREV_FIRE_TIME from DCON_QRTZ_TRIGGERS where trigger_name = ?";

        return getJdbcTemplate().queryForObject(sql, Long.class, jobName);
    }

    /* (non-Javadoc)
     * @see eu.domibus.connector.common.db.dao.impl.DomibusConnectorConnectorMonitoringDao#selectStatusTrigger(java.lang.String)
     */
    @Override
    public String selectStatusTrigger(String jobName) {
        String sql = "select TRIGGER_STATE from DCON_QRTZ_TRIGGERS where trigger_name = ?";
        String[] parameter = new String[1];
        parameter[0] = jobName;
        return getJdbcTemplate().queryForObject(sql, parameter, String.class);
    }

    /* (non-Javadoc)
     * @see eu.domibus.connector.common.db.dao.impl.DomibusConnectorConnectorMonitoringDao#countRejectedMessagesConnector()
     */
    @Override
    public Integer countRejectedMessagesConnector() {
        String sql = "select count(*) from DOMIBUS_CONNECTOR_MESSAGES where rejected != null";

        return getJdbcTemplate().queryForObject(sql, Integer.class);
    }

    //
    // public Integer countNoReceiptMessagesGateway() {
    // String sql =
    // "select count(*) from TB_RECEIPT_TRACKING where status = 'NO_RECEIPT'";
    //
    // return getJdbcTemplate().queryForInt(sql);
    // }
    //
    // public Integer countPendingMessagesGateway() {
    // String sql =
    // "select count(*) from TB_RECEIPT_TRACKING where status = 'IN_PROCESS'";
    //
    // return getJdbcTemplate().queryForInt(sql);
    // }

    /* (non-Javadoc)
     * @see eu.domibus.connector.common.db.dao.impl.DomibusConnectorConnectorMonitoringDao#countPendingMessagesConnector()
     */
    @Override
    public Integer countPendingMessagesConnector() {
        String sql = "select count(*) from DOMIBUS_CONNECTOR_MESSAGES where confirmed is null and rejected is null";

        return getJdbcTemplate().queryForObject(sql, Integer.class);
    }

}
