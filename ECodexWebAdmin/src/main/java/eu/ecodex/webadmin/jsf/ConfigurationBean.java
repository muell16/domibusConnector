package eu.ecodex.webadmin.jsf;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import eu.ecodex.webadmin.commons.JmxConnector;
import eu.ecodex.webadmin.commons.WebAdminProperties;

public class ConfigurationBean {

    private String monitoringType;

    private boolean dbSelected;
    private boolean jmxSelected;
    private boolean restSelected;

    private boolean testDisplay;
    private boolean saveMonitoringDisplay;
    private boolean saveJobDisplay;

    private String monitoringTestMessage;
    private String monitoringTestStatus;

    private WebAdminProperties webAdminProperties;

    public String configure() {
        testDisplay = false;
        saveMonitoringDisplay = false;
        saveJobDisplay = false;
        if (monitoringType != null && monitoringType.equals("DB")) {
            dbSelected = true;
            jmxSelected = false;
            restSelected = false;

        } else if (monitoringType != null && monitoringType.equals("JMX")) {
            jmxSelected = true;
            dbSelected = false;
            restSelected = false;
        } else if (monitoringType != null && monitoringType.equals("REST")) {
            restSelected = true;
            dbSelected = false;
            jmxSelected = false;
        }

        return "/pages/configuration.xhtml";
    }

    public String test() {
        testDisplay = true;
        saveMonitoringDisplay = false;
        saveJobDisplay = false;
        try {
            if (jmxSelected) {

                JmxConnector.getJmxServerConnection(webAdminProperties.getJmxServerAddress(),
                        webAdminProperties.getJmxServerPort(), true);

                monitoringTestMessage = "OK";
                monitoringTestStatus = "Connected to: " + webAdminProperties.getJmxConnectorString();

            } else if (restSelected) {
                String restUrl = webAdminProperties.getRestConnectorString();
                URL url = new URL(restUrl + "getOutgoingRepeatInterval/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

                monitoringTestMessage = "OK";
                monitoringTestStatus = "Connected to: " + webAdminProperties.getRestConnectorString();
            }

            else if (dbSelected) {
                monitoringTestMessage = "OK";
                monitoringTestStatus = "Connected to: " + webAdminProperties.getConnectorDatabaseUrl();
            }

        } catch (IOException e) {
            monitoringTestMessage = "Error while connecting: " + e.getMessage();
            monitoringTestStatus = "ERROR";
        }

        return "/pages/configuration.xhtml";
    }

    public String saveMonitoringConfiguration() {

        saveMonitoringDisplay = true;
        saveJobDisplay = false;

        if (jmxSelected) {
            webAdminProperties.saveProperty("monitoring.type", monitoringType);
            webAdminProperties.saveProperty("jmx.server.address", webAdminProperties.getJmxServerAddress());
            webAdminProperties.saveProperty("jmx.server.port", webAdminProperties.getJmxServerPort());
        } else if (restSelected) {
            webAdminProperties.saveProperty("monitoring.type", monitoringType);
            webAdminProperties.saveProperty("rest.server.address", webAdminProperties.getRestServerAddress());
            webAdminProperties.saveProperty("rest.server.port", webAdminProperties.getRestServerPort());
            webAdminProperties.saveProperty("rest.webcontext", webAdminProperties.getRestWebContext());
        } else if (dbSelected) {
            webAdminProperties.saveProperty("monitoring.type", monitoringType);
        }

        return "/pages/configuration.xhtml";
    }

    public String saveJobConfiguration() {

        saveMonitoringDisplay = false;
        saveJobDisplay = true;

        webAdminProperties.saveProperty("mail.notification", String.valueOf(webAdminProperties.isMailNotification()));
        webAdminProperties.saveProperty("mail.notification.receivers", webAdminProperties.getMailNotificationList());
        webAdminProperties.saveProperty("monitoring.timer.interval",
                String.valueOf(webAdminProperties.getMonitoringTimerInterval()));

        return "/pages/configuration.xhtml";
    }

    public boolean isDbSelected() {
        return dbSelected;
    }

    public void setDbSelected(boolean dbSelected) {
        this.dbSelected = dbSelected;
    }

    public boolean isJmxSelected() {
        return jmxSelected;
    }

    public void setJmxSelected(boolean jmxSelected) {
        this.jmxSelected = jmxSelected;
    }

    public boolean isRestSelected() {
        return restSelected;
    }

    public void setRestSelected(boolean restSelected) {
        this.restSelected = restSelected;
    }

    public WebAdminProperties getWebAdminProperties() {
        return webAdminProperties;
    }

    public void setWebAdminProperties(WebAdminProperties webAdminProperties) {
        this.webAdminProperties = webAdminProperties;
    }

    public String getMonitoringTestMessage() {
        return monitoringTestMessage;
    }

    public void setMonitoringTestMessage(String monitoringTestMessage) {
        this.monitoringTestMessage = monitoringTestMessage;
    }

    public String getMonitoringTestStatus() {
        return monitoringTestStatus;
    }

    public void setMonitoringTestStatus(String monitoringTestStatus) {
        this.monitoringTestStatus = monitoringTestStatus;
    }

    public String getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(String monitoringType) {
        this.monitoringType = monitoringType;
    }

    public boolean isTestDisplay() {
        return testDisplay;
    }

    public void setTestDisplay(boolean testDisplay) {
        this.testDisplay = testDisplay;
    }

    public boolean isSaveMonitoringDisplay() {
        return saveMonitoringDisplay;
    }

    public void setSaveMonitoringDisplay(boolean saveMonitoringDisplay) {
        this.saveMonitoringDisplay = saveMonitoringDisplay;
    }

    public boolean isSaveJobDisplay() {
        return saveJobDisplay;
    }

    public void setSaveJobDisplay(boolean saveJobDisplay) {
        this.saveJobDisplay = saveJobDisplay;
    }

}
