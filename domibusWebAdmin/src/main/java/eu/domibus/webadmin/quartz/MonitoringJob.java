package eu.domibus.webadmin.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class MonitoringJob extends QuartzJobBean {
    private MonitoringTask monitoringTask;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        monitoringTask.monitoring();
    }

    public void setMonitoringTask(MonitoringTask monitoringTask) {
        this.monitoringTask = monitoringTask;
    }

}
