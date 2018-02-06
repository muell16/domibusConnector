package eu.domibus.connector.controller.scheduler.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import eu.domibus.connector.controller.process.CheckEvidencesTimeoutProcessorImpl;
import eu.domibus.connector.controller.spring.QuartzContext;
import org.springframework.context.annotation.Configuration;
import eu.domibus.connector.controller.process.CheckEvidencesTimeoutProcessor;

@Configuration("checkEvidencesTimeoutJobConfiguration")
@ConditionalOnProperty(name = "connector.use.evidences.timeout", havingValue="true")
public class CheckEvidencesTimeoutJob implements Job {

	@Autowired
	private CheckEvidencesTimeoutProcessor processor;
	
	@Value("${connector.check.messages.period.ms}")
    private long repeatInterval;
	

    @Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		processor.checkEvidencesTimeout();
	}
	
	@Bean(name = "checkEvidencesTimeoutJob")
	public JobDetailFactoryBean checkEvidencesTimeoutJob() {
		return QuartzContext.createJobDetail(this.getClass());
	}
	 
	@Bean(name = "checkEvidencesTimeoutTrigger")
	public SimpleTriggerFactoryBean checkEvidencesTimeoutTrigger(@Qualifier("checkEvidencesTimeoutJob") JobDetailFactoryBean jdfb ) {
		return QuartzContext.createTrigger(jdfb.getObject(), repeatInterval);
	}

}
