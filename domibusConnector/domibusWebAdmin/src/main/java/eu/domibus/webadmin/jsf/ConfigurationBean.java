package eu.domibus.webadmin.jsf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import eu.domibus.webadmin.commons.JmxConnector;
import eu.domibus.webadmin.commons.WebAdminProperties;
import eu.domibus.webadmin.dao.IDomibusWebAdminUserDao;

@Controller("configurationBean")
@Scope("session")
public class ConfigurationBean implements Serializable {

    private static final long serialVersionUID = -6978169110805373376L;

    private String monitoringType;

    private boolean dbSelected;
    private boolean jmxSelected;
    private boolean restSelected;

    private boolean testDisplay;
    private boolean saveMonitoringDisplay;
    private boolean saveJobDisplay;
    private boolean saveUserDisplay;

    private String monitoringTestMessage;
    private String monitoringTestStatus;
    private boolean chooseMonitoringType = false;

    private String user = "";
    private String password = "";
    private String passwordConfirm = "";
    private String userAction = "";

    private String loggedInUser;

    @Autowired
    private WebAdminProperties webAdminProperties;

    @Autowired
    private IDomibusWebAdminUserDao domibusWebAdminUserDao;


    @PostConstruct
    public void init() {
        webAdminProperties.loadProperties();
        if (monitoringType == null) {
            monitoringType = webAdminProperties.getMonitoringType();
        }
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
    }

    public String configure() {
        chooseMonitoringType = false;
        testDisplay = false;
        saveMonitoringDisplay = false;
        saveJobDisplay = false;
        saveUserDisplay = false;
        if (monitoringType != null && monitoringType.equals("DB")) {
            dbSelected = true;
            jmxSelected = false;
            restSelected = false;
            webAdminProperties.setMonitoringType(monitoringType);
        } else if (monitoringType != null && monitoringType.equals("JMX")) {
            jmxSelected = true;
            dbSelected = false;
            restSelected = false;
            webAdminProperties.setMonitoringType(monitoringType);
        } else if (monitoringType != null && monitoringType.equals("REST")) {
            restSelected = true;
            dbSelected = false;
            jmxSelected = false;
            webAdminProperties.setMonitoringType(monitoringType);
        } else {
            chooseMonitoringType = false;
        }

        return "/pages/configuration.xhtml";
    }

    public String testMonitoring() {
        testDisplay = true;
        chooseMonitoringType = false;
        saveMonitoringDisplay = false;
        saveUserDisplay = false;
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
            } else if (dbSelected) {
                monitoringTestMessage = "OK";
                monitoringTestStatus = "Connected to: " + webAdminProperties.getConnectorDatabaseUrl();
            } else {
                chooseMonitoringType = true;
                testDisplay = false;
            }

        } catch (IOException e) {
            monitoringTestMessage = "Error while connecting: " + e.getMessage();
            monitoringTestStatus = "ERROR";
        }

        return "/pages/configuration.xhtml";
    }

    public String saveMonitoringConfiguration() {

        saveMonitoringDisplay = true;
        chooseMonitoringType = false;
        saveUserDisplay = false;
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
        } else {
            saveMonitoringDisplay = false;
            chooseMonitoringType = true;
        }

        return "/pages/configuration.xhtml";
    }

    public String saveJobConfiguration() {

        saveMonitoringDisplay = false;
        saveUserDisplay = false;
        saveJobDisplay = true;
        webAdminProperties.saveProperty("monitoring.log.write",
                String.valueOf(webAdminProperties.isMonitoringLogWrite()));
        // webAdminProperties.saveProperty("mail.notification",
        // String.valueOf(webAdminProperties.isMailNotification()));
        // webAdminProperties.saveProperty("mail.notification.receivers",
        // webAdminProperties.getMailNotificationList());
        webAdminProperties.saveProperty("monitoring.timer.interval",
                String.valueOf(webAdminProperties.getMonitoringTimerInterval()));

        return "/pages/configuration.xhtml";
    }

    public String addUser() {
        try {

            if (domibusWebAdminUserDao.checkIfUserExists(user)) {
                userAction = "User already exists!";
                saveUserDisplay = true;
                return "/pages/configuration.xhtml";
            }

            domibusWebAdminUserDao.insertUser(user, password);
            userAction = "User successfully added!";
            saveUserDisplay = true;
            return "/pages/configuration.xhtml";

        } catch (Exception e) {
            saveUserDisplay = true;
            userAction = "Error while inserting user: " + e;
            return "/pages/configuration.xhtml";
        }

    }

    public String deleteUser() {
        try {

            if (loggedInUser.equals(user)) {
                userAction = "Cannot delete logged in user!";
                saveUserDisplay = true;
                return "/pages/configuration.xhtml";
            }

            if (!domibusWebAdminUserDao.checkIfUserExists(user)) {
                userAction = "User does not exist!";
                saveUserDisplay = true;
                return "/pages/configuration.xhtml";
            }

            domibusWebAdminUserDao.deleteUser(user);
            userAction = "User successfully deleted!";
            saveUserDisplay = true;
            return "/pages/configuration.xhtml";

        } catch (Exception e) {
            saveUserDisplay = true;
            userAction = "Error while deleting user: " + e;
            return "/pages/configuration.xhtml";
        }

    }

    public String updateUserPassword() {
        try {
            if (!loggedInUser.equals(user)) {
                userAction = "You can only change the logged in user's password!";
                saveUserDisplay = true;
                return "/pages/configuration.xhtml";
            }

            domibusWebAdminUserDao.updateUserPassword(user, password);
            userAction = "Password successfully changed!";
            saveUserDisplay = true;
            return "/pages/configuration.xhtml";
        } catch (Exception e) {
            saveUserDisplay = true;
            userAction = "Error while deleting user: " + e;
            return "/pages/configuration.xhtml";
        }
    }

    public String restart() {
//        webAdminProperties.getCtx().refresh();
//        HttpSession session = Util.getSession();
//        session.invalidate();

//    	applicationContext.refresh();
        return "login";
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSaveUserDisplay() {
        return saveUserDisplay;
    }

    public void setSaveUserDisplay(boolean saveUserDisplay) {
        this.saveUserDisplay = saveUserDisplay;
    }

    public String getUserAction() {
        return userAction;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }

    public IDomibusWebAdminUserDao getDomibusWebAdminUserDao() {
        return domibusWebAdminUserDao;
    }

    public void setDomibusWebAdminUserDao(
            IDomibusWebAdminUserDao domibusWebAdminUserDao) {
        this.domibusWebAdminUserDao = domibusWebAdminUserDao;
    }

    public boolean isChooseMonitoringType() {
        return chooseMonitoringType;
    }

    public void setChooseMonitoringType(boolean chooseMonitoringType) {
        this.chooseMonitoringType = chooseMonitoringType;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getLoggedInUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
        //return loggedInUser;
    }



}
