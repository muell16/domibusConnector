package eu.ecodex.webadmin.jsf;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.ecodex.webadmin.commons.JmxConnector;
import eu.ecodex.webadmin.commons.Util;
import eu.ecodex.webadmin.commons.WebAdminProperties;
import eu.ecodex.webadmin.dao.impl.ECodexWebAdminUserDao;

public class LoginBean implements Serializable {

    protected final Log logger = LogFactory.getLog(getClass());
    private static final long serialVersionUID = 1L;
    private String password;
    private String message, uname;
    private boolean loggedIn = false;
    private ECodexWebAdminUserDao eCodexWebAdminUserDao;
    private MBeanServerConnection mbsc;
    private String connectionStatusJmx;
    private String connectionMessageJmx;
    private String connectedToDb;
    private WebAdminProperties webAdminProperties;

    public String loginProject() {
        boolean result;
        try {
            result = eCodexWebAdminUserDao.login(uname, password);

            if (result) {

                connectJmxServerConnection(false);
                // get Http Session and store username
                HttpSession session = Util.getSession();
                session.setAttribute("username", uname);
                loggedIn = true;
                connectedToDb = "Connected to: " + webAdminProperties.getConnectorDatabaseUrl();
                return "main";
            } else {

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Login!", "Please Try Again!"));

                // invalidate session, and redirect to other pages

                // message = "Invalid Login. Please Try Again!";
                loggedIn = false;
                return "login";
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error!", "See Log!"));
            loggedIn = false;
            return "login";
        }
    }

    public String logout() {
        HttpSession session = Util.getSession();
        session.invalidate();
        return "login";
    }

    public String reconnectJmxServerConnection() {
        return connectJmxServerConnection(true);
    }

    public String connectJmxServerConnection(boolean reconnect) {

        try {
            mbsc = JmxConnector.getJmxServerConnection(webAdminProperties.getJmxServerAddress(),
                    webAdminProperties.getJmxServerPort(), false);
            // Query for the EcodexConnector to check if there are the required
            // mbeans
            ObjectName jmxMonitor = new ObjectName("ECodexConnector:name=ECodexConnectorJMXMonitor");
            mbsc.getAttribute(jmxMonitor, "LastCalledEvidenceTimeoutCheck");
            connectionMessageJmx = "Connected to: " + "service:jmx:rmi:///jndi/rmi:/"
                    + webAdminProperties.getJmxServerAddress() + "/:" + webAdminProperties.getJmxServerPort()
                    + "/jmxrmi";
            connectionStatusJmx = "OK";
            return "/pages/main.xhtml";
        } catch (Exception e) {
            logger.error(e.getMessage());
            connectionMessageJmx = "Error while connecting to: " + webAdminProperties.getJmxServerAddress() + ":"
                    + webAdminProperties.getJmxServerPort() + " " + e.getMessage();
            connectionStatusJmx = "ERROR";
            return "/pages/main.xhtml";
        }

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public ECodexWebAdminUserDao geteCodexWebAdminUserDao() {
        return eCodexWebAdminUserDao;
    }

    public void seteCodexWebAdminUserDao(ECodexWebAdminUserDao eCodexWebAdminUserDao) {
        this.eCodexWebAdminUserDao = eCodexWebAdminUserDao;
    }

    public MBeanServerConnection getMbsc() {
        return mbsc;
    }

    public void setMbsc(MBeanServerConnection mbsc) {
        this.mbsc = mbsc;
    }

    public WebAdminProperties getWebAdminProperties() {
        return webAdminProperties;
    }

    public void setWebAdminProperties(WebAdminProperties webAdminProperties) {
        this.webAdminProperties = webAdminProperties;
    }

    public Log getLogger() {
        return logger;
    }

    public String getConnectedToDb() {
        return connectedToDb;
    }

    public void setConnectedToDb(String connectedToDb) {
        this.connectedToDb = connectedToDb;
    }

    public String getConnectionMessageJmx() {
        return connectionMessageJmx;
    }

    public void setConnectionMessageJmx(String connectionMessageJmx) {
        this.connectionMessageJmx = connectionMessageJmx;
    }

    public String getConnectionStatusJmx() {
        return connectionStatusJmx;
    }

    public void setConnectionStatusJmx(String connectionStatusJmx) {
        this.connectionStatusJmx = connectionStatusJmx;
    }

}