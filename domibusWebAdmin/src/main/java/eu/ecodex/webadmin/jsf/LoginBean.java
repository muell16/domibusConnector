package eu.ecodex.webadmin.jsf;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.management.MBeanServerConnection;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.ecodex.webadmin.blogic.connector.monitoring.IConnectorMonitoringService;
import eu.ecodex.webadmin.commons.Util;
import eu.ecodex.webadmin.dao.IECodexWebAdminUserDao;

public class LoginBean implements Serializable {

    protected final Log logger = LogFactory.getLog(getClass());
    private static final long serialVersionUID = 1L;
    private String password;
    private String message, uname;
    private boolean loggedIn = false;
    private IECodexWebAdminUserDao eCodexWebAdminUserDao;
    private MBeanServerConnection mbsc;
    private String connectedToDb;
    private IConnectorMonitoringService connectorMonitoringService;
    private ConfigurationBean configurationBean;

    public String loginProject() {
        boolean result;
        try {
            result = eCodexWebAdminUserDao.login(uname, password);

            if (result) {

                connectorMonitoringService.generateMonitoringReport(true);
                // get Http Session and store username
                HttpSession session = Util.getSession();
                session.setAttribute("username", uname);
                configurationBean.setLoggedInUser(uname);
                loggedIn = true;
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

    public MBeanServerConnection getMbsc() {
        return mbsc;
    }

    public void setMbsc(MBeanServerConnection mbsc) {
        this.mbsc = mbsc;
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

    public IECodexWebAdminUserDao geteCodexWebAdminUserDao() {
        return eCodexWebAdminUserDao;
    }

    public void seteCodexWebAdminUserDao(IECodexWebAdminUserDao eCodexWebAdminUserDao) {
        this.eCodexWebAdminUserDao = eCodexWebAdminUserDao;
    }

    public IConnectorMonitoringService getConnectorMonitoringService() {
        return connectorMonitoringService;
    }

    public void setConnectorMonitoringService(IConnectorMonitoringService connectorMonitoringService) {
        this.connectorMonitoringService = connectorMonitoringService;
    }

    public ConfigurationBean getConfigurationBean() {
        return configurationBean;
    }

    public void setConfigurationBean(ConfigurationBean configurationBean) {
        this.configurationBean = configurationBean;
    }

}