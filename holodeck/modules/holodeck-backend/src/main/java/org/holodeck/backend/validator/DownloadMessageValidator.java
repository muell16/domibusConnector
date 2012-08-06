/*
 * 
 */
package org.holodeck.backend.validator;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.holodeck.backend.util.StringUtils;
import org.apache.log4j.Logger;
import org.holodeck.backend.db.dao.MessageDAO;
import org.holodeck.backend.db.model.Message;
import org.holodeck.backend.db.model.Payload;
import org.holodeck.backend.service.exception.DownloadMessageServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.ecodex.org.Code;

/**
 * The Class DownloadMessageValidator.
 */
@Service
public class DownloadMessageValidator {
	
	/** The Constant log. */
	private final static Logger log = Logger.getLogger(DownloadMessageValidator.class);

	/** The message dao. */
	@Autowired
	private MessageDAO messageDAO;

	/**
	 * Validate.
	 *
	 * @param downloadMessageRequest the download message request
	 * @throws DownloadMessageServiceException the download message service exception
	 */
	public void validate(backend.ecodex.org.DownloadMessageRequest downloadMessageRequest) throws DownloadMessageServiceException{
		log.debug("Validating DownloadMessage");

		Message message = null;

		if(StringUtils.isNotEmpty(downloadMessageRequest.getMessageID()) && StringUtils.isNumeric(downloadMessageRequest.getMessageID())){
			message = messageDAO.findById(Integer.parseInt(downloadMessageRequest.getMessageID()));
		}
		else{
			message = messageDAO.getFirstNotDownloadedSortedByDate();
		}

		if(message == null || message.getDeleted()){
			log.error("Error downloading message: message not found");

			DownloadMessageServiceException downloadMessageServiceException = new DownloadMessageServiceException(
					"Error downloading message: message not found", Code.ERROR_DOWNLOAD_001);
			throw downloadMessageServiceException;
		}

		if(message.getDownloaded()){
			log.error("Error downloading message[ " + message.getIdMessage() + " ]: message already downloaded");

			DownloadMessageServiceException downloadMessageServiceException = new DownloadMessageServiceException(
					"Error downloading message: message already downloaded", Code.ERROR_DOWNLOAD_002);
			throw downloadMessageServiceException;
		}

		File messageFile = new File(message.getDirectory(), org.holodeck.backend.module.Constants.MESSAGING_FILE_NAME);

		if(messageFile==null || !messageFile.exists()){
			log.error("Error loading message file of message[ " + message.getIdMessage() + " ]");

			DownloadMessageServiceException downloadMessageServiceException = new DownloadMessageServiceException(
					"Error loading message file", Code.ERROR_DOWNLOAD_003);
			throw downloadMessageServiceException;
		}

		for(Payload payload: message.getPayloads()){
			File file = new File(message.getDirectory(), payload.getFileName());

			if(file==null || !file.exists()){
				log.error("Error loading file [" + payload.getFileName() + "] of message[ " + message.getIdMessage() + " ]");

				DownloadMessageServiceException downloadMessageServiceException = new DownloadMessageServiceException(
						"Error loading file [" + payload.getFileName() + "] of message[ " + message.getIdMessage() + " ]", Code.ERROR_DOWNLOAD_003);
				throw downloadMessageServiceException;
			}
		}
	}
}
