/*
 * 
 */
package org.holodeck.backend.service;

import java.io.File;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.log4j.Logger;
import org.holodeck.backend.db.dao.MessageDAO;
import org.holodeck.backend.db.model.Message;
import org.holodeck.backend.db.model.Payload;
import org.holodeck.backend.service.exception.DownloadMessageServiceException;
import org.holodeck.backend.util.StringUtils;
import org.holodeck.backend.validator.DownloadMessageValidator;
import org.holodeck.backend.validator.ListPendingMessagesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class DownloadMessageService.
 */
@Service
public class DownloadMessageService {
	
	/** The Constant log. */
	private final static Logger log = Logger.getLogger(DownloadMessageService.class);

	/** The message dao. */
	@Autowired
	private MessageDAO messageDAO;

	/** The download message validator. */
	@Autowired
	private DownloadMessageValidator downloadMessageValidator;

	/** The list pending messages validator. */
	@Autowired
	private ListPendingMessagesValidator listPendingMessagesValidator;

	/**
	 * List pending messages.
	 *
	 * @param listPendingMessagesRequest the list pending messages request
	 * @return the backend.ecodex.org. list pending messages response
	 * @throws DownloadMessageServiceException the download message service exception
	 */
	public backend.ecodex.org.ListPendingMessagesResponse listPendingMessages(backend.ecodex.org.ListPendingMessagesRequest listPendingMessagesRequest)
			throws DownloadMessageServiceException
	{
		log.debug("Called SendMessageService.listPendingMessages");

		listPendingMessagesValidator.validate(listPendingMessagesRequest);

		List<Message> messages = messageDAO.findNotDownloadedSortedByMessageDate();

		backend.ecodex.org.ListPendingMessagesResponse listPendingMessagesResponse = org.holodeck.backend.util.Converter
				.convertMessageListToListPendingMessagesResponse(messages);

		return listPendingMessagesResponse;
	}

	/**
	 * Download message.
	 *
	 * @param downloadMessageResponse the download message response
	 * @param downloadMessageRequest the download message request
	 * @return the org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704. messaging e
	 * @throws DownloadMessageServiceException the download message service exception
	 */
	@Transactional
	public org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE downloadMessage(
			backend.ecodex.org.DownloadMessageResponse downloadMessageResponse,
			backend.ecodex.org.DownloadMessageRequest downloadMessageRequest)
			throws DownloadMessageServiceException
	{
		log.debug("Called SendMessageService.sendMessageWithReference");

		downloadMessageValidator.validate(downloadMessageRequest);

		Message message = null;

		if(StringUtils.isNotEmpty(downloadMessageRequest.getMessageID()) && StringUtils.isNumeric(downloadMessageRequest.getMessageID())){
			message = messageDAO.findById(Integer.parseInt(downloadMessageRequest.getMessageID()));
		}
		else{
			message = messageDAO.getFirstNotDownloadedSortedByMessageDate();
		}

		File messageFile = new File(message.getDirectory(), org.holodeck.backend.module.Constants.MESSAGING_FILE_NAME);

		org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messagingResponse = org.holodeck.backend.util.Converter.convertFileToMessagingE(messageFile);

		for(Payload payload: message.getPayloads()){
			DataHandler repositoryItem = new DataHandler(new FileDataSource(new File(message.getDirectory(), payload.getFileName())));

			downloadMessageResponse.addPayload(repositoryItem);
		}

		//Set downloaded flag
		{
			message.setDownloaded(true);

			messageDAO.save(message);
		}

		return messagingResponse;
	}
	
	@Transactional
	public void deleteMessage(backend.ecodex.org.DownloadMessageRequest downloadMessageRequest){
		Message message = null;
		if(StringUtils.isNotEmpty(downloadMessageRequest.getMessageID()) && StringUtils.isNumeric(downloadMessageRequest.getMessageID())){
			message = messageDAO.findById(Integer.parseInt(downloadMessageRequest.getMessageID()));
		}
		else{
			message = messageDAO.getFirstNotDownloadedSortedByMessageDate();
		}
		
		if(message!=null){
			message.setDownloaded(true);

			messageDAO.save(message);
		}		
	}
}
