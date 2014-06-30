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

import eu.ecodex.connector.common.db.dao.impl.ECodexConnectorMonitoringDao;
import eu.ecodex.webadmin.blogic.connector.monitoring.IConnectorMonitoringService;
import eu.ecodex.webadmin.commons.JmxConnector;
import eu.ecodex.webadmin.commons.WebAdminProperties;

public class ConnectorMonitoringService implements IConnectorMonitoringService, Serializable {

    private static final long serialVersionUID = -5017789529520475342L;

    protected final Log logger = LogFactory.getLog(getClass());
    private MBeanServerConnection mbsc;
    private String connectionMessage;
    private String connectionStatus;
    private String connectionDB;
    private String monitoringType;
    private Long checkOutgoingRepeatInterval;
    private String jobStatusEvidencesTimeout;
    private String jobStatusIncoming;
    private String jobStatusOutgoing;
    private Date lastCalledCheckEvidencesTimeout;
    private Date lastCalledIncoming;
    private Date lastCalledOutgoing;
    private Integer noReceiptMessagesGateway;
    private String noReceiptMessagesGatewayStatus;
    private Integer rejectedConnectorMessagesCount;
    private String rejectedConnectorMessagesCountStatus;
    private boolean useMonitorServer = false;
    private ECodexConnectorMonitoringDao monitoringDao;

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

        if ("WAITING".equals(jobStatusEvidencesTimeout)) {
            jobStatusEvidencesTimeout = "OK";
        }
        if ("WAITING".equals(jobStatusIncoming)) {
            jobStatusIncoming = "OK";
        }
        if ("WAITING".equals(jobStatusOutgoing)) {
            jobStatusOutgoing = "OK";
        }

        connectionDB = "Connected to: " + webAdminProperties.getConnectorDatabaseUrl();
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
            noReceiptMessagesGateway = (Integer) mbsc.getAttribute(jmxMonitor, "NoReceiptMessagesGateway");
            if (noReceiptMessagesGateway > 0) {
                noReceiptMessagesGatewayStatus = "WARNING";
            } else {
                noReceiptMessagesGatewayStatus = "OK";
            }
            rejectedConnectorMessagesCount = (Integer) mbsc.getAttribute(jmxMonitor, "RejectedConnectorMessagesCount");
            if (rejectedConnectorMessagesCount > 0) {
                rejectedConnectorMessagesCountStatus = "WARNING";
            } else {
                rejectedConnectorMessagesCountStatus = "OK";
            }

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
            noReceiptMessagesGateway = Integer.valueOf(queryRestWebService(restUrl + "getNoReceiptMessagesGateway/"));
            if (noReceiptMessagesGateway > 0) {
                noReceiptMessagesGatewayStatus = "WARNING";
            } else {
                noReceiptMessagesGatewayStatus = "OK";
            }
            rejectedConnectorMessagesCount = Integer.valueOf(queryRestWebService(restUrl
                    + "getRejectedConnectorMessagesCount/"));
            if (rejectedConnectorMessagesCount > 0) {
                rejectedConnectorMessagesCountStatus = "WARNING";
            } else {
                rejectedConnectorMessagesCountStatus = "OK";
            }

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

    private void queryDB() {

        checkOutgoingRepeatInterval = monitoringDao.selectTimerIntervalForJob(CHECK_OUTGOING_TRIGGER_NAME);
        jobStatusIncoming = monitoringDao.selectStatusTrigger(CHECK_INCOMING_TRIGGER_NAME);
        jobStatusOutgoing = monitoringDao.selectStatusTrigger(CHECK_OUTGOING_TRIGGER_NAME);
        jobStatusEvidencesTimeout = monitoringDao.selectStatusTrigger(CHECK_EVIDENCES_TIMEOUT_TRIGGER_NAME);
        lastCalledCheckEvidencesTimeout = new Date(
                monitoringDao.selectLastCalledTrigger(CHECK_EVIDENCES_TIMEOUT_TRIGGER_NAME));
        lastCalledIncoming = new Date(monitoringDao.selectLastCalledTrigger(CHECK_INCOMING_TRIGGER_NAME));
        lastCalledOutgoing = new Date(monitoringDao.selectLastCalledTrigger(CHECK_OUTGOING_TRIGGER_NAME));
        noReceiptMessagesGateway = Integer.valueOf(monitoringDao.countNoReceiptMessagesGateway());
        if (noReceiptMessagesGateway > 0) {
            noReceiptMessagesGatewayStatus = "WARNING";
        } else {
            noReceiptMessagesGatewayStatus = "OK";
        }
        rejectedConnectorMessagesCount = Integer.valueOf(monitoringDao.countRejectedMessagesConnector());
        if (rejectedConnectorMessagesCount > 0) {
            rejectedConnectorMessagesCountStatus = "WARNING";
        } else {
            rejectedConnectorMessagesCountStatus = "OK";
        }

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

    public String getJobStatusIncoming() {
        return jobStatusIncoming;
    }

    public void setJobStatusIncoming(String jobStatusIncoming) {
        this.jobStatusIncoming = jobStatusIncoming;
    }

    public String getJobStatusOutgoing() {
        return jobStatusOutgoing;
    }

    public void setJobStatusOutgoing(String jobStatusOutgoing) {
        this.jobStatusOutgoing = jobStatusOutgoing;
    }

    public Integer getNoReceiptMessagesGateway() {
        return noReceiptMessagesGateway;
    }

    public void setNoReceiptMessagesGateway(Integer noReceiptMessagesGateway) {
        this.noReceiptMessagesGateway = noReceiptMessagesGateway;
    }

    public Integer getRejectedConnectorMessagesCount() {
        return rejectedConnectorMessagesCount;
    }

    public void setRejectedConnectorMessagesCount(Integer rejectedConnectorMessagesCount) {
        this.rejectedConnectorMessagesCount = rejectedConnectorMessagesCount;
    }

    public String getConnectionMessage() {
        return connectionMessage;
    }

    public void setConnectionMessage(String connectionMessage) {
        this.connectionMessage = connectionMessage;
    }

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

    public String getConnectionDB() {
        return connectionDB;
    }

    public void setConnectionDB(String connectionDB) {
        this.connectionDB = connectionDB;
    }

    public boolean isUseMonitorServer() {
        return useMonitorServer;
    }

    public void setUseMonitorServer(boolean useMonitorServer) {
        this.useMonitorServer = useMonitorServer;
    }

    public String getNoReceiptMessagesGatewayStatus() {
        return noReceiptMessagesGatewayStatus;
    }

    public void setNoReceiptMessagesGatewayStatus(String noReceiptMessagesGatewayStatus) {
        this.noReceiptMessagesGatewayStatus = noReceiptMessagesGatewayStatus;
    }

    public String getRejectedConnectorMessagesCountStatus() {
        return rejectedConnectorMessagesCountStatus;
    }

    public void setRejectedConnectorMessagesCountStatus(String rejectedConnectorMessagesCountStatus) {
        this.rejectedConnectorMessagesCountStatus = rejectedConnectorMessagesCountStatus;
    }

    public ECodexConnectorMonitoringDao getMonitoringDao() {
        return monitoringDao;
    }

    public void setMonitoringDao(ECodexConnectorMonitoringDao monitoringDao) {
        this.monitoringDao = monitoringDao;
    }

}
