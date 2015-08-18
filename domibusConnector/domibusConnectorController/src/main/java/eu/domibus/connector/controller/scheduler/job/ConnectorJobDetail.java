package eu.domibus.connector.controller.scheduler.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import eu.domibus.connector.controller.DomibusConnectorController;
import eu.domibus.connector.controller.context.ApplicationContextProvider;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;

public class ConnectorJobDetail extends QuartzJobBean {

    static Logger LOGGER = LoggerFactory.getLogger(ConnectorJobDetail.class);

    private String jobBeanName;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();

        DomibusConnectorController job = (DomibusConnectorController) ctx.getBean(jobBeanName);

        try {
            job.execute();
        } catch (DomibusConnectorControllerException e) {
            e.printStackTrace();
        }

    }

    public void setJobBeanName(String jobBeanName) {
        this.jobBeanName = jobBeanName;
    }

}
