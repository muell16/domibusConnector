/*
 * 
 */
package org.holodeck.backend.job;

import org.apache.log4j.Logger;
import org.holodeck.backend.service.JobService;
import org.holodeck.backend.service.exception.JobServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The Class DeleteMessagesJob.
 */
@Service(value="deleteMessagesJob")
public class DeleteMessagesJob {
	
	/** The log. */
	private Logger log = Logger.getLogger(DeleteMessagesJob.class);

	/** The job service. */
	@Autowired
	private JobService jobService;

	/**
	 * Execute.
	 */
	public void execute(){
		log.debug("Executing DeleteMessagesJob");

		try{
			jobService.deleteOldMessages();

			log.debug("Executed DeleteMessagesJob");
		}
		catch(JobServiceException e){
			log.error("Error executing DeleteMessagesJob", e);
		}
	}
}
