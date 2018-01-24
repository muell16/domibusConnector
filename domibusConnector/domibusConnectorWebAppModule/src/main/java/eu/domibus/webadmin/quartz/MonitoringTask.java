package eu.domibus.webadmin.quartz;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import eu.domibus.webadmin.blogic.connector.monitoring.IConnectorMonitoringService;
import eu.domibus.webadmin.commons.SendMail;
import eu.domibus.webadmin.commons.WebAdminProperties;

@Component
public class MonitoringTask implements ApplicationContextAware, InitializingBean {

    protected final Log logger = LogFactory.getLog(getClass());
    private ConfigurableWebApplicationContext ctx;
    private IConnectorMonitoringService connectorMonitoringService;
    private WebAdminProperties webAdminProperties;
    private boolean errorFound = false;

    public void monitoring() {
        connectorMonitoringService.generateMonitoringReport(false);
        errorFound = false;
        String mailReport = "Summary of monitored items in status WARNING/ERROR: \n\n";

        if (connectorMonitoringService.getConnectionStatus() != null && !"OK".equals(connectorMonitoringService.getConnectionStatus())) {
            mailReport += "Connections status: " + connectorMonitoringService.getConnectionStatus() + "\n";
            mailReport += "Connection error message: " + connectorMonitoringService.getConnectionMessage() + "\n";
            errorFound = true;
        }

        if (connectorMonitoringService.getJobStatusIncoming() != null && "ERROR".equals(connectorMonitoringService.getJobStatusIncoming())) {
            mailReport += "Check incoming jobs: " + connectorMonitoringService.getJobStatusIncoming() + "\n";
            errorFound = true;
        }
        if (connectorMonitoringService.getJobStatusOutgoing() != null && "ERROR".equals(connectorMonitoringService.getJobStatusOutgoing())) {
            mailReport += "Check outgoing jobs: " + connectorMonitoringService.getJobStatusOutgoing() + "\n";
            errorFound = true;
        }

        if (errorFound) {

            if (webAdminProperties.isMonitoringLogWrite()) {
                logger.error(mailReport);

                if (webAdminProperties.isMailNotification()) {
                    mail(mailReport);
                }
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
        try {
            sendmail.postMail(text, "ECodex monitoring warning");
        } catch (MessagingException e) {
            logger.error(e);
        }

        return "configuration.xhtml";
    }

    public IConnectorMonitoringService getConnectorMonitoringService() {
        return connectorMonitoringService;
    }

    public void setConnectorMonitoringService(IConnectorMonitoringService connectorMonitoringService) {
        this.connectorMonitoringService = connectorMonitoringService;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ctx = (ConfigurableWebApplicationContext) context;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        //SimpleTriggerImpl simpleTriggerBean = (SimpleTriggerImpl) ctx.getBean("simpleMonitoringTrigger");
//        if (webAdminProperties.getMonitoringTimerInterval() != null
//                && webAdminProperties.getMonitoringTimerInterval() > Long.valueOf(60000)) {
//            simpleTriggerBean.setRepeatInterval(webAdminProperties.getMonitoringTimerInterval());
//        }

    }

    public WebAdminProperties getWebAdminProperties() {
        return webAdminProperties;
    }

    public void setWebAdminProperties(WebAdminProperties webAdminProperties) {
        this.webAdminProperties = webAdminProperties;
    }

}
