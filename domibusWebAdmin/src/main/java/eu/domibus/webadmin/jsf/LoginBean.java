package eu.domibus.webadmin.jsf;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.management.MBeanServerConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import eu.domibus.webadmin.blogic.connector.monitoring.IConnectorMonitoringService;
import eu.domibus.webadmin.commons.Util;
import eu.domibus.webadmin.dao.IDomibusWebAdminUserDao;

@Controller("loginBean")
@Scope("session")
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

    @Autowired
    private AuthenticationManager authenticationManager;

//    @PostConstruct
//    public void init() {
//    	logger.error("#############INIT BEAN############");
//    }
    public String loginProject() throws ServletException, IOException {
        logger.debug("loginProject: called");

        boolean result;
        try {

            result = domibusWebAdminUserDao.login(uname, password);

            logger.debug("loginProject: result is [{}] (true means user exists with uname and password in db)", result);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(uname, password);
            Authentication auth = authenticationManager.authenticate(token);

            //authenticationManager.aut
            //SecurityContextHolder.getContext().setAuthentication(token);
            logger.trace("loginProject: spring auth is: [{}]", auth);

            if (auth.isAuthenticated()) {
                connectorMonitoringService.generateMonitoringReport(true);
                // get Http Session and store username
                HttpSession session = Util.getSession();
                session.setAttribute("username", uname);
//                  configurationBean.setLoggedInUser(uname);                  
                loggedIn = true;

                SecurityContextHolder.getContext().setAuthentication(auth);

                return "main.xhtml";
            }

            result = domibusWebAdminUserDao.login(uname, password);

            if (result) {

                connectorMonitoringService.generateMonitoringReport(true);
                // get Http Session and store username
                HttpSession session = Util.getSession();
                session.setAttribute("username", uname);
                //configurationBean.setLoggedInUser(uname);
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

        } catch (BadCredentialsException badCred) {
            logger.warn("loginProject: bad Credentials exception occured", badCred);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Login!", "Please Try Again!"));

            loggedIn = false;
            return "/pages/login.xhtml";

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

        SecurityContextHolder.getContext().setAuthentication(null);

        return "/pages/login.xhtml";
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
