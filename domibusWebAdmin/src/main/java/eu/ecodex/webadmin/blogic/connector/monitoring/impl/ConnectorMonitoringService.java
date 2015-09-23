package eu.ecodex.webadmin.blogic.connector.monitoring.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.common.db.dao.DomibusConnectorConnectorMonitoringDao;
import eu.ecodex.webadmin.blogic.connector.monitoring.IConnectorMonitoringService;
import eu.ecodex.webadmin.commons.DBUtil;
import eu.ecodex.webadmin.commons.JmxConnector;
import eu.ecodex.webadmin.commons.WebAdminProperties;

public class ConnectorMonitoringService implements IConnectorMonitoringService, Serializable {

    private static final long serialVersionUID = -5017789529520475342L;

    protected final Log logger = LogFactory.getLog(getClass());
    private MBeanServerConnection mbsc;
    private String connectionMessage;
    private String connectionStatus;
    private String connectionConnectorDB;
    private String connectionGatewayDB;
    private String connectionStatusConnectorDB;
    private String connectionStatusGatewayDB;
    private String monitoringType;
    private Long checkOutgoingRepeatInterval;
    private String jobStatusEvidencesTimeout;
    private String jobStatusIncoming;
    private String jobStatusOutgoing;
    private Date lastCalledCheckEvidencesTimeout;
    private Date lastCalledIncoming;
    private Date lastCalledOutgoing;
    private Integer rejectedConnectorMessagesCount;
    private String rejectedConnectorMessagesCountStatus;
    private boolean useMonitorServer = false;
    private DomibusConnectorConnectorMonitoringDao monitoringDao;
    private DBUtil dbUtil;

    private WebAdminProperties webAdminProperties;

    private static final String CHECK_OUTGOING_TRIGGER_NAME = "checkOutgoingTrigger";
    private static final String CHECK_INCOMING_TRIGGER_NAME = "checkIncomingTrigger";
    private static final String CHECK_EVIDENCES_TIMEOUT_TRIGGER_NAME = "checkEvidencesTimeoutTrigger";

    private enum monitoring {
        JMX, REST, DB
    }

    public void refreshMonitoringServer() {
        generateMonitoringReport(true);
    }

    @Override
    public void generateMonitoringReport(boolean reconnect) {
        if (reconnect) {
           webAdminProperties.loadProperties();
        }
        monitoringType = checkMonitoringType(webAdminProperties.getMonitoringType());
        if (monitoringType.equals(monitoring.JMX.toString())) {
            connectionMessage = "Connected to: " + "service:jmx:rmi:///jndi/rmi:/"
                    + webAdminProperties.getJmxServerAddress() + "/:" + webAdminProperties.getJmxServerPort()
                    + "/jmxrmi";
            connectionStatus = "OK";
            queryJMXServer(reconnect);
            useMonitorServer = true;
        } else if (monitoringType.equals(monitoring.REST.toString())) {
            connectionStatus = "OK";
            connectionMessage = "Connected to: " + webAdminProperties.getRestConnectorString();
            queryRestServer();
            useMonitorServer = true;
        } else {
            queryDB();
            useMonitorServer = false;
        }

        if ("ERROR".equals(connectionStatus)) {
            queryDB();
        }

        if ("WAITING".equals(jobStatusEvidencesTimeout)) {
            jobStatusEvidencesTimeout = "OK";
        }
        if ("WAITING".equals(jobStatusIncoming)) {
            jobStatusIncoming = "OK";
        }
        if ("WAITING".equals(jobStatusOutgoing)) {
            jobStatusOutgoing = "OK";
        }

        connectionStatusConnectorDB = "OK";
        connectionStatusGatewayDB = "OK";
        connectionConnectorDB = webAdminProperties.getConnectorDatabaseUrl();
//        if (!dbUtil.testConnectorDbConnection()) {
//            connectionConnectorDB = dbUtil.getConnectorErrorMessage();
//            connectionStatusConnectorDB = "ERROR";
//        }
//        if (!dbUtil.testGatewayDbConnection()) {
//            connectionGatewayDB = dbUtil.getGatewayErrorMessage();
//            connectionStatusGatewayDB = "ERROR";
//        }

    }

    private void queryJMXServer(boolean reconnect) {

        try {
            mbsc = JmxConnector.getJmxServerConnection(webAdminProperties.getJmxServerAddress(),
                    webAdminProperties.getJmxServerPort(), reconnect);
            // Query for the EcodexConnector to check if there are the required
            // mbeans
            ObjectName jmxMonitor = new ObjectName("ECodexConnector:name=ECodexConnectorJMXMonitor");

            checkOutgoingRepeatInterval = (Long) mbsc.getAttribute(jmxMonitor, "CheckOutgoingRepeatInterval");
            jobStatusEvidencesTimeout = (String) mbsc.getAttribute(jmxMonitor, "JobStatusEvidencesTimeout");
            jobStatusIncoming = (String) mbsc.getAttribute(jmxMonitor, "JobStatusIncoming");
            jobStatusOutgoing = (String) mbsc.getAttribute(jmxMonitor, "JobStatusOutgoing");
            Long lastCalledCheckEvidencesTimeoutMillis = (Long) mbsc.getAttribute(jmxMonitor,
                    "LastCalledCheckEvidencesTimeout");
            lastCalledCheckEvidencesTimeout = new Date(lastCalledCheckEvidencesTimeoutMillis);
            Long lastCalledIncomingMillis = (Long) mbsc.getAttribute(jmxMonitor, "LastCalledIncoming");
            lastCalledIncoming = new Date(lastCalledIncomingMillis);
            Long lastCalledOutgoingMillis = (Long) mbsc.getAttribute(jmxMonitor, "LastCalledOutgoing");
            lastCalledOutgoing = new Date(lastCalledOutgoingMillis);

        } catch (Exception e) {
            logger.error(e.getMessage());
            connectionMessage = "Error while connecting to: " + webAdminProperties.getJmxServerAddress() + ":"
                    + webAdminProperties.getJmxServerPort() + " " + e.getMessage();
            connectionStatus = "ERROR";
        }

    }

    private void queryRestServer() {

        String restUrl = webAdminProperties.getRestConnectorString();

        try {
            checkOutgoingRepeatInterval = Long.valueOf(queryRestWebService(restUrl + "getOutgoingRepeatInterval/"));
            jobStatusEvidencesTimeout = queryRestWebService(restUrl + "getJobStatusEvidencesTimeout/");
            jobStatusIncoming = queryRestWebService(restUrl + "getJobStatusIncoming/");
            jobStatusOutgoing = queryRestWebService(restUrl + "getJobStatusOutgoing/");
            lastCalledCheckEvidencesTimeout = new Date(Long.valueOf(queryRestWebService(restUrl
                    + "getLastCalledCheckEvidencesTimeout/")));
            lastCalledIncoming = new Date(Long.valueOf(queryRestWebService(restUrl + "getLastCalledIncoming/")));
            lastCalledOutgoing = new Date(Long.valueOf(queryRestWebService(restUrl + "getLastCalledOutgoing/")));

        } catch (IOException e) {
            logger.error(e.getMessage());
            connectionMessage = "Error while connecting to: " + webAdminProperties.getRestConnectorString() + " "
                    + e.getMessage();
            connectionStatus = "ERROR";
        }

    }

    private String queryRestWebService(String urlParam) throws IOException {
        URL url = new URL(urlParam);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String result = "";
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        String input = "{}";

        OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();

        if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED
                && conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output = "";

        while ((output = br.readLine()) != null) {

            result += output;
        }

        conn.disconnect();
        return result;

    }

    @Transactional(readOnly=true, value="transactionManager")
    private void queryDB() {

        checkOutgoingRepeatInterval = monitoringDao.selectTimerIntervalForJob(CHECK_OUTGOING_TRIGGER_NAME);
        jobStatusIncoming = monitoringDao.selectStatusTrigger(CHECK_INCOMING_TRIGGER_NAME);
        jobStatusOutgoing = monitoringDao.selectStatusTrigger(CHECK_OUTGOING_TRIGGER_NAME);
        jobStatusEvidencesTimeout = monitoringDao.selectStatusTrigger(CHECK_EVIDENCES_TIMEOUT_TRIGGER_NAME);
        lastCalledCheckEvidencesTimeout = new Date(
                monitoringDao.selectLastCalledTrigger(CHECK_EVIDENCES_TIMEOUT_TRIGGER_NAME));
        lastCalledIncoming = new Date(monitoringDao.selectLastCalledTrigger(CHECK_INCOMING_TRIGGER_NAME));
        lastCalledOutgoing = new Date(monitoringDao.selectLastCalledTrigger(CHECK_OUTGOING_TRIGGER_NAME));
       
    }

    public WebAdminProperties getWebAdminProperties() {
        return webAdminProperties;
    }

    public void setWebAdminProperties(WebAdminProperties webAdminProperties) {
        this.webAdminProperties = webAdminProperties;
    }

    private String checkMonitoringType(String monitoringType) {
        if (monitoringType == null || monitoringType.isEmpty()) {
            return monitoring.DB.toString();
        } else {
            for (monitoring m : monitoring.values()) {
                if (m.name().equals(monitoringType)) {
                    return m.toString();
                }
            }

            return monitoring.DB.toString();
        }
    }

    public Long getCheckOutgoingRepeatInterval() {
        return checkOutgoingRepeatInterval;
    }

    public void setCheckOutgoingRepeatInterval(Long checkOutgoingRepeatInterval) {
        this.checkOutgoingRepeatInterval = checkOutgoingRepeatInterval;
    }

    public String getJobStatusEvidencesTimeout() {
        return jobStatusEvidencesTimeout;
    }

    public void setJobStatusEvidencesTimeout(String jobStatusEvidencesTimeout) {
        this.jobStatusEvidencesTimeout = jobStatusEvidencesTimeout;
    }

    @Override
    public String getJobStatusIncoming() {
        return jobStatusIncoming;
    }

    public void setJobStatusIncoming(String jobStatusIncoming) {
        this.jobStatusIncoming = jobStatusIncoming;
    }

    @Override
    public String getJobStatusOutgoing() {
        return jobStatusOutgoing;
    }

    public void setJobStatusOutgoing(String jobStatusOutgoing) {
        this.jobStatusOutgoing = jobStatusOutgoing;
    }

    public Integer getRejectedConnectorMessagesCount() {
        return rejectedConnectorMessagesCount;
    }

    public void setRejectedConnectorMessagesCount(Integer rejectedConnectorMessagesCount) {
        this.rejectedConnectorMessagesCount = rejectedConnectorMessagesCount;
    }

    @Override
    public String getConnectionMessage() {
        return connectionMessage;
    }

    public void setConnectionMessage(String connectionMessage) {
        this.connectionMessage = connectionMessage;
    }

    @Override
    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public Date getLastCalledCheckEvidencesTimeout() {
        return lastCalledCheckEvidencesTimeout;
    }

    public void setLastCalledCheckEvidencesTimeout(Date lastCalledCheckEvidencesTimeout) {
        this.lastCalledCheckEvidencesTimeout = lastCalledCheckEvidencesTimeout;
    }

    public Date getLastCalledIncoming() {
        return lastCalledIncoming;
    }

    public void setLastCalledIncoming(Date lastCalledIncoming) {
        this.lastCalledIncoming = lastCalledIncoming;
    }

    public Date getLastCalledOutgoing() {
        return lastCalledOutgoing;
    }

    public void setLastCalledOutgoing(Date lastCalledOutgoing) {
        this.lastCalledOutgoing = lastCalledOutgoing;
    }

    public String getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(String monitoringType) {
        this.monitoringType = monitoringType;
    }

    public boolean isUseMonitorServer() {
        return useMonitorServer;
    }

    public void setUseMonitorServer(boolean useMonitorServer) {
        this.useMonitorServer = useMonitorServer;
    }


    public String getRejectedConnectorMessagesCountStatus() {
        return rejectedConnectorMessagesCountStatus;
    }

    public void setRejectedConnectorMessagesCountStatus(String rejectedConnectorMessagesCountStatus) {
        this.rejectedConnectorMessagesCountStatus = rejectedConnectorMessagesCountStatus;
    }

    public DBUtil getDbUtil() {
        return dbUtil;
    }

    public void setDbUtil(DBUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    public String getConnectionStatusConnectorDB() {
        return connectionStatusConnectorDB;
    }

    public void setConnectionStatusConnectorDB(String connectionStatusConnectorDB) {
        this.connectionStatusConnectorDB = connectionStatusConnectorDB;
    }

    public String getConnectionStatusGatewayDB() {
        return connectionStatusGatewayDB;
    }

    public void setConnectionStatusGatewayDB(String connectionStatusGatewayDB) {
        this.connectionStatusGatewayDB = connectionStatusGatewayDB;
    }

    public String getConnectionConnectorDB() {
        return connectionConnectorDB;
    }

    public void setConnectionConnectorDB(String connectionConnectorDB) {
        this.connectionConnectorDB = connectionConnectorDB;
    }

    public String getConnectionGatewayDB() {
        return connectionGatewayDB;
    }

    public void setConnectionGatewayDB(String connectionGatewayDB) {
        this.connectionGatewayDB = connectionGatewayDB;
    }

    public DomibusConnectorConnectorMonitoringDao getMonitoringDao() {
        return monitoringDao;
    }

    public void setMonitoringDao(DomibusConnectorConnectorMonitoringDao monitoringDao) {
        this.monitoringDao = monitoringDao;
    }

}
