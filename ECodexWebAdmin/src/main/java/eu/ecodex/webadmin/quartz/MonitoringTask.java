package eu.ecodex.webadmin.quartz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SimpleTriggerBean;
import org.springframework.web.context.support.XmlWebApplicationContext;

import eu.ecodex.webadmin.blogic.connector.monitoring.IConnectorMonitoringService;
import eu.ecodex.webadmin.commons.SendMail;
import eu.ecodex.webadmin.commons.WebAdminProperties;

public class MonitoringTask implements ApplicationContextAware, InitializingBean {

    protected final Log logger = LogFactory.getLog(getClass());
    private XmlWebApplicationContext ctx;
    private IConnectorMonitoringService connectorMonitoringService;
    private WebAdminProperties webAdminProperties;
    private boolean errorFound = false;

    public void monitoring() {
        connectorMonitoringService.generateMonitoringReport(false);
        errorFound = false;
        String mailReport = "Summary of monitored items in status WARNING/ERROR: \n\n";

        if (!"OK".equals(connectorMonitoringService.getConnectionStatus())) {
            mailReport += "Connections status: " + connectorMonitoringService.getConnectionStatus() + "\n";
            mailReport += "Connection error message: " + connectorMonitoringService.getConnectionMessage() + "\n";
            errorFound = true;
        }

        if (!"OK".equals(connectorMonitoringService.getJobStatusIncoming())) {
            mailReport += "Check incoming jobs: " + connectorMonitoringService.getJobStatusIncoming() + "\n";
            errorFound = true;
        }
        if (!"OK".equals(connectorMonitoringService.getJobStatusOutgoing())) {
            mailReport += "Check outgoing jobs: " + connectorMonitoringService.getJobStatusOutgoing() + "\n";
            errorFound = true;
        }

        if (!"OK".equals(connectorMonitoringService.getNoReceiptMessagesGatewayStatus())) {
            mailReport += "Check AS4 messages pending: " + connectorMonitoringService.getNoReceiptMessagesGateway()
                    + "\n";
            errorFound = true;
        }

        if (errorFound) {
            logger.error(mailReport);

            if (webAdminProperties.isMailNotification()) {
                mail(mailReport);
            }
        }

    }

    public String mail(String text) {

        SendMail sendmail = new SendMail();
        sendmail.setEmailFromAddress("betrieb-ecodex@brz.gv.at");
        String[] emailList = webAdminProperties.getMailNotificationList().split(";");
        sendmail.setEmailList(emailList);
        sendmail.setNameFrom("Betrieb ECodex");
        sendmail.setSmtpHostName(webAdminProperties.getSmtpHostName());
        // try {
        // sendmail.postMail(text, "ECodex monitoring warning");
        // } catch (MessagingException e) {
        // logger.error(e);
        // }

        return "/pages/configuration.xhtml";
    }

    public IConnectorMonitoringService getConnectorMonitoringService() {
        return connectorMonitoringService;
    }

    public void setConnectorMonitoringService(IConnectorMonitoringService connectorMonitoringService) {
        this.connectorMonitoringService = connectorMonitoringService;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ctx = (XmlWebApplicationContext) context;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SimpleTriggerBean simpleTriggerBean = (SimpleTriggerBean) ctx.getBean("simpleMonitoringTrigger");
        if (webAdminProperties.getMonitoringTimerInterval() != null
                && webAdminProperties.getMonitoringTimerInterval() > Long.valueOf(60000)) {
            simpleTriggerBean.setRepeatInterval(webAdminProperties.getMonitoringTimerInterval());
        }

    }

    public WebAdminProperties getWebAdminProperties() {
        return webAdminProperties;
    }

    public void setWebAdminProperties(WebAdminProperties webAdminProperties) {
        this.webAdminProperties = webAdminProperties;
    }

}
