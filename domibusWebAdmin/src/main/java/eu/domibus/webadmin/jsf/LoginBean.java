package eu.domibus.webadmin.jsf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.management.MBeanServerConnection;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.webadmin.blogic.connector.monitoring.IConnectorMonitoringService;
import eu.domibus.webadmin.commons.Util;
import eu.domibus.webadmin.dao.IDomibusWebAdminUserDao;

@Controller //("loginBean")
@Scope("session")
//@Component("loginBean")
public class LoginBean implements Serializable {

    protected final Logger logger = LoggerFactory.getLogger(LoginBean.class);
    private static final long serialVersionUID = 1L;
    private String password;
    private String message, uname;
    private boolean loggedIn = false;
    @Autowired
    private IDomibusWebAdminUserDao domibusWebAdminUserDao;
    private MBeanServerConnection mbsc;
    private String connectedToDb;
    @Autowired
    private IConnectorMonitoringService connectorMonitoringService;
    @Autowired
    private ConfigurationBean configurationBean;

//    @PostConstruct
//    public void init() {
//    	logger.error("#############INIT BEAN############");
//    }
    
    public String loginProject() {
        boolean result;
        try {
            result = domibusWebAdminUserDao.login(uname, password);

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
                return "/pages/login.xhtml";
            }

        } catch (Exception e) {
            logger.error("loginProject: exception occured", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error!", "See Log!"));
            loggedIn = false;
            return "/pages/login.xhtml";
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

    public Logger getLogger() {
        return logger;
    }

    public String getConnectedToDb() {
        return connectedToDb;
    }

    public void setConnectedToDb(String connectedToDb) {
        this.connectedToDb = connectedToDb;
    }
    
	public IDomibusWebAdminUserDao getDomibusWebAdminUserDao() {
		return domibusWebAdminUserDao;
	}

	public void setDomibusWebAdminUserDao(
			IDomibusWebAdminUserDao domibusWebAdminUserDao) {
		this.domibusWebAdminUserDao = domibusWebAdminUserDao;
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