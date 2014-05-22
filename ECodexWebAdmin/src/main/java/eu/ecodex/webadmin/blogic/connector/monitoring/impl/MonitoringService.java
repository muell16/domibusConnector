package eu.ecodex.webadmin.blogic.connector.monitoring.impl;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.ecodex.webadmin.blogic.connector.monitoring.IMonitoringService;
import eu.ecodex.webadmin.commons.JmxConnector;

public class MonitoringService implements IMonitoringService, Serializable {

    private static final long serialVersionUID = 6778604093699596922L;

    protected final Log logger = LogFactory.getLog(getClass());
    private MBeanServerConnection mbsc;

    private String lastCalledEvidenceTimeoutCheckDate;
    private String lastCalledEvidenceTimeoutCheckMessage;
    private String lastCalledIncomingMessagesPendingDate;
    private String lastCalledIncomingMessagesPendingMessage;
    private String lastCalledOutgoingMessagesPendingDate;
    private String lastCalledOutgoingMessagesPendingMessage;

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ecodex.webadmin.blogic.connector.monitoring.impl.IMonitoringService
     * #generateMontoringReport()
     */
    @Override
    public void generateMonitoringReport() {

        try {
            mbsc = JmxConnector.getJmxServerConnection();

            ObjectName jmxMonitor = new ObjectName("ECodexConnector:name=ECodexConnectorJMXMonitor");
            Date lcet = (Date) mbsc.getAttribute(jmxMonitor, "LastCalledEvidenceTimeoutCheck");
            Date lcimp = (Date) mbsc.getAttribute(jmxMonitor, "LastCalledIncomingMessagesPending");
            Date lcomp = (Date) mbsc.getAttribute(jmxMonitor, "LastCalledOutgoingMessagesPending");

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            // Refresh rate for the monitoring is one Minute
            // If the dates are not refreshed for at least two minutes, it is
            // assumed as an error state
            Calendar cCheck = Calendar.getInstance();
            cCheck = Calendar.getInstance();
            cCheck.add(Calendar.MINUTE, -2);
            Date dCheck = cCheck.getTime();

            if (dCheck.after(lcet)) {
                lastCalledEvidenceTimeoutCheckMessage = "ERROR";
            } else {
                lastCalledEvidenceTimeoutCheckMessage = "OK";
            }

            if (dCheck.after(lcimp)) {
                lastCalledIncomingMessagesPendingMessage = "ERROR";
            } else {
                lastCalledIncomingMessagesPendingMessage = "OK";
            }

            if (dCheck.after(lcomp)) {
                lastCalledOutgoingMessagesPendingMessage = "ERROR";
            } else {
                lastCalledOutgoingMessagesPendingMessage = "OK";
            }
            lastCalledEvidenceTimeoutCheckDate = df.format(lcet);
            lastCalledIncomingMessagesPendingDate = df.format(lcimp);
            lastCalledOutgoingMessagesPendingDate = df.format(lcomp);
            // return "/pages/monitoring.xml";
        } catch (Exception e) {
            logger.error(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error!", "See Log!"));

            // return "pages/monitoring.xhtml";
        }

    }

    public MBeanServerConnection getMbsc() {
        return mbsc;
    }

    public void setMbsc(MBeanServerConnection mbsc) {
        this.mbsc = mbsc;
    }

    public String getLastCalledEvidenceTimeoutCheckDate() {
        return lastCalledEvidenceTimeoutCheckDate;
    }

    public void setLastCalledEvidenceTimeoutCheckDate(String lastCalledEvidenceTimeoutCheckDate) {
        this.lastCalledEvidenceTimeoutCheckDate = lastCalledEvidenceTimeoutCheckDate;
    }

    public String getLastCalledEvidenceTimeoutCheckMessage() {
        return lastCalledEvidenceTimeoutCheckMessage;
    }

    public void setLastCalledEvidenceTimeoutCheckMessage(String lastCalledEvidenceTimeoutCheckMessage) {
        this.lastCalledEvidenceTimeoutCheckMessage = lastCalledEvidenceTimeoutCheckMessage;
    }

    public String getLastCalledIncomingMessagesPendingDate() {
        return lastCalledIncomingMessagesPendingDate;
    }

    public void setLastCalledIncomingMessagesPendingDate(String lastCalledIncomingMessagesPendingDate) {
        this.lastCalledIncomingMessagesPendingDate = lastCalledIncomingMessagesPendingDate;
    }

    public String getLastCalledIncomingMessagesPendingMessage() {
        return lastCalledIncomingMessagesPendingMessage;
    }

    public void setLastCalledIncomingMessagesPendingMessage(String lastCalledIncomingMessagesPendingMessage) {
        this.lastCalledIncomingMessagesPendingMessage = lastCalledIncomingMessagesPendingMessage;
    }

    public String getLastCalledOutgoingMessagesPendingDate() {
        return lastCalledOutgoingMessagesPendingDate;
    }

    public void setLastCalledOutgoingMessagesPendingDate(String lastCalledOutgoingMessagesPendingDate) {
        this.lastCalledOutgoingMessagesPendingDate = lastCalledOutgoingMessagesPendingDate;
    }

    public String getLastCalledOutgoingMessagesPendingMessage() {
        return lastCalledOutgoingMessagesPendingMessage;
    }

    public void setLastCalledOutgoingMessagesPendingMessage(String lastCalledOutgoingMessagesPendingMessage) {
        this.lastCalledOutgoingMessagesPendingMessage = lastCalledOutgoingMessagesPendingMessage;
    }

}
