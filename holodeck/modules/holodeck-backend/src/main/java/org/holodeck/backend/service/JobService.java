/*
 * 
 */
package org.holodeck.backend.service;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.holodeck.backend.db.dao.MessageDAO;
import org.holodeck.backend.db.model.Message;
import org.holodeck.backend.module.Constants;
import org.holodeck.backend.service.exception.JobServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class JobService.
 */
@Service
public class JobService {
	
	/** The Constant log. */
	private final static Logger log = Logger.getLogger(JobService.class);

	/** The message dao. */
	@Autowired
	private MessageDAO messageDAO;

	/**
	 * Delete old messages.
	 *
	 * @throws JobServiceException the job service exception
	 */
	@Transactional
	public void deleteOldMessages() throws JobServiceException
	{
		log.debug("Starting SendMessageService.deleteOldMessages");

		int messagesTimeLiveInDays = Constants.getMessagesTimeLive();

		List<Message> messagesToDelete = messageDAO.findNotDeleted(messagesTimeLiveInDays);

		for(Message message: messagesToDelete){
			File directory = new File(message.getDirectory());

			boolean deleted = org.holodeck.backend.util.IOUtils.removeDirectory(directory);

			if(deleted || (directory!=null && !directory.exists())){
				message.setDeleted(true);

				messageDAO.save(message);

				log.debug("Deleted Message[" + message.getIdMessage() + "]");
			}
			else{
				log.error("Direcotry of Message[" + message.getIdMessage() + "] cannot be deleted");
			}
		}

		log.debug("Finished SendMessageService.deleteOldMessages");
	}
}
