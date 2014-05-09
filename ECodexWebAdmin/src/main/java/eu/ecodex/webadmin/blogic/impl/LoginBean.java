package eu.ecodex.webadmin.blogic.impl;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import eu.ecodex.webadmin.commons.Util;
import eu.ecodex.webadmin.dao.impl.UserDao;

public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String password;
    private String message, uname;
    private boolean loggedIn = false;

    public String loginProject() {
        boolean result = UserDao.login(uname, password);
        if (result) {
            // get Http Session and store username
            HttpSession session = Util.getSession();
            session.setAttribute("username", uname);
            loggedIn = true;
            return "home";
        } else {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Login!", "Please Try Again!"));

            // invalidate session, and redirect to other pages

            // message = "Invalid Login. Please Try Again!";
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
}