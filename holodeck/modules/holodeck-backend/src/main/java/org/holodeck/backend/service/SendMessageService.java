/*
 * 
 */
package org.holodeck.backend.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.holodeck.backend.service.exception.SendMessageServiceException;
import org.holodeck.backend.service.helper.SendMessageHelper;
import org.holodeck.backend.util.Converter;
import org.holodeck.backend.util.IOUtils;
import org.holodeck.backend.validator.SendMessageValidator;
import org.holodeck.backend.validator.SendMessageWithReferenceValidator;
import org.holodeck.ebms3.persistent.UserMsgToPush;
import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.ebms3.submit.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.ecodex.org.Code;

/**
 * The Class SendMessageService.
 */
@Service
public class SendMessageService {
	
	/** The Constant log. */
	private final static Logger log = Logger.getLogger(SendMessageService.class);

	/** The send message helper. */
	@Autowired
	private SendMessageHelper sendMessageHelper;

	/** The send message validator. */
	@Autowired
	private SendMessageValidator sendMessageValidator;

	/** The send message with reference validator. */
	@Autowired
	private SendMessageWithReferenceValidator sendMessageWithReferenceValidator;

	/**
	 * Send message.
	 *
	 * @param messaging the messaging
	 * @param sendRequest the send request
	 * @throws SendMessageServiceException the send message service exception
	 */
	public backend.ecodex.org.SendResponse sendMessage(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging,
			backend.ecodex.org.SendRequest sendRequest) throws SendMessageServiceException
	{
		log.debug("Called SendMessageService.sendMessage");
		
	    log.log(org.holodeck.logging.level.Message.MESSAGE, org.holodeck.backend.util.Converter.convertUserMessageToMessageInfo(messaging.getMessaging().getUserMessage()[0], "", "SendMessageService", "sendMessage", org.holodeck.logging.persistent.LoggerMessage.MESSAGE_SENT_INIT_STATUS));

		sendMessageValidator.validate(messaging, sendRequest);

		MsgInfoSet msgInfoSet = Converter.convertUserMessageToMsgInfoSet(messaging.getMessaging().getUserMessage()[0]);

		String action = messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getAction().toString();
		String fromPartyid = messaging.getMessaging().getUserMessage()[0].getPartyInfo().getFrom().getPartyId()[0].getNonEmptyString();
		String fromPartyidType = null;
//		String fromPartyidType = messaging.getMessaging().getUserMessage()[0].getPartyInfo().getFrom().getPartyId()[0].getType().getNonEmptyString();
		String toPartyid = messaging.getMessaging().getUserMessage()[0].getPartyInfo().getTo().getPartyId()[0].getNonEmptyString();
		String toPartyidType = null;
//		String toPartyidType = messaging.getMessaging().getUserMessage()[0].getPartyInfo().getTo().getPartyId()[0].getType().getNonEmptyString();
		String service = messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getService().getNonEmptyString();
		
		msgInfoSet.setPmode(org.holodeck.ebms3.module.Configuration.getPMode(action, service, fromPartyid, fromPartyidType, toPartyid, toPartyidType));
		
		File tempDir = org.holodeck.backend.util.IOUtils.createTempDir();
		try {
			int counter = 1;
			
			List<Payload> payloads = new ArrayList<Payload>();
			for (javax.activation.DataHandler dataHandler : sendRequest.getPayload()) {
				Payload p = new Payload();
				
				String payloadFileName = MessageFormat.format(
						org.holodeck.backend.module.Constants.PAYLOAD_FILE_NAME_FORMAT, counter);
				IOUtils.write(IOUtils.toByteArray(dataHandler),
						new FileOutputStream(new File(tempDir, payloadFileName)));
				
				p.setCid(payloadFileName);
				p.setPayload(payloadFileName);
				
				String description = null;
				try {
					description = messaging.getMessaging().getUserMessage()[0].getPayloadInfo().getPartInfo()[counter - 1]
							.getDescription().getNonEmptyString();
				} catch (Exception e) {
				}				
				p.setDescription(description);
				
				payloads.add(p);
				counter++;
			}
			msgInfoSet.getPayloads().setPayloads(payloads);

			File metadataFile = new File(tempDir, org.holodeck.backend.module.Constants.METADATA_FILE_NAME);
			msgInfoSet.writeToFile(metadataFile.getAbsolutePath());
		} catch (Exception e) {
			log.error("Error sending message", e);
			
		    log.log(org.holodeck.logging.level.Message.MESSAGE, org.holodeck.backend.util.Converter.convertUserMessageToMessageInfo(messaging.getMessaging().getUserMessage()[0], "", "SendMessageService", "sendMessage", org.holodeck.logging.persistent.LoggerMessage.MESSAGE_SENT_KO_STATUS));

			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"Error writing data into temporal directory[" + tempDir + "]", e, Code.ERROR_SEND_002);
			throw sendMessageServiceException;
		}

		UserMsgToPush userMsgToPush = sendMessageHelper.submitFromFolder(tempDir);

		String messageId = sendMessageHelper.send(userMsgToPush);

		try {
			FileUtils.deleteDirectory(tempDir);
		} catch (IOException e) {
			log.error("Error deleting temporal directory[" + tempDir + "]", e);
		}
		
	    log.log(org.holodeck.logging.level.Message.MESSAGE, org.holodeck.backend.util.Converter.convertUserMessageToMessageInfo(messaging.getMessaging().getUserMessage()[0], messageId, "SendMessageService", "sendMessage", org.holodeck.logging.persistent.LoggerMessage.MESSAGE_SENT_OK_STATUS));
	    
	    backend.ecodex.org.SendResponse sendResponse = new backend.ecodex.org.SendResponse();
	    sendResponse.setMessageID(messageId);
	    
	    return sendResponse;
	}

	/**
	 * Send message with reference.
	 *
	 * @param messaging the messaging
	 * @param sendRequestURL the send request url
	 * @throws SendMessageServiceException the send message service exception
	 */
	public backend.ecodex.org.SendResponse sendMessageWithReference(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging,
			backend.ecodex.org.SendRequestURL sendRequestURL) throws SendMessageServiceException
	{
		log.debug("Called SendMessageService.sendMessageWithReference");
		
	    log.log(org.holodeck.logging.level.Message.MESSAGE, org.holodeck.backend.util.Converter.convertUserMessageToMessageInfo(messaging.getMessaging().getUserMessage()[0], "", "SendMessageService", "sendMessageWithReference", org.holodeck.logging.persistent.LoggerMessage.MESSAGE_SENT_INIT_STATUS));

		sendMessageWithReferenceValidator.validate(messaging, sendRequestURL);

		MsgInfoSet msgInfoSet = Converter.convertUserMessageToMsgInfoSet(messaging.getMessaging().getUserMessage()[0]);

		String action = messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getAction().toString();
		String fromPartyid = messaging.getMessaging().getUserMessage()[0].getPartyInfo().getFrom().getPartyId()[0].getNonEmptyString();
		String fromPartyidType = null;
//		String fromPartyidType = messaging.getMessaging().getUserMessage()[0].getPartyInfo().getFrom().getPartyId()[0].getType().getNonEmptyString();
		String toPartyid = messaging.getMessaging().getUserMessage()[0].getPartyInfo().getTo().getPartyId()[0].getNonEmptyString();
		String toPartyidType = null;
//		String toPartyidType = messaging.getMessaging().getUserMessage()[0].getPartyInfo().getTo().getPartyId()[0].getType().getNonEmptyString();
		String service = messaging.getMessaging().getUserMessage()[0].getCollaborationInfo().getService().getNonEmptyString();
		
		msgInfoSet.setPmode(org.holodeck.ebms3.module.Configuration.getPMode(action, service, fromPartyid, fromPartyidType, toPartyid, toPartyidType));

		File tempDir = org.holodeck.backend.util.IOUtils.createTempDir();
		try {
			int counter = 1;
			for (String payload : sendRequestURL.getPayload()) {
				String payloadFileName = MessageFormat.format(
						org.holodeck.backend.module.Constants.PAYLOAD_FILE_NAME_FORMAT, counter);

				InputStream is = null;

				try {
					URL url = new URL(payload);
					is = url.openStream();
					IOUtils.copy(is, new FileOutputStream(new File(tempDir,
							payloadFileName)));
				} catch (MalformedURLException e) {
					log.error("Payload url " + counter +  " is invalid");

					SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
							"Payload url " + counter +  " is invalid", Code.ERROR_SEND_004);
					throw sendMessageServiceException;
				} catch (IOException e) {
					log.error("Payload url " + counter +  " is invalid");

					SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
							"Payload url " + counter +  " is invalid", Code.ERROR_SEND_004);
					throw sendMessageServiceException;
				}

				msgInfoSet.getPayloads().addPayload(payloadFileName, payloadFileName);
				counter++;
			}

			File metadataFile = new File(tempDir, org.holodeck.backend.module.Constants.METADATA_FILE_NAME);
			msgInfoSet.writeToFile(metadataFile.getAbsolutePath());
		} catch (Exception e) {
			log.error("Error sending message", e);
			
		    log.log(org.holodeck.logging.level.Message.MESSAGE, org.holodeck.backend.util.Converter.convertUserMessageToMessageInfo(messaging.getMessaging().getUserMessage()[0], "", "SendMessageService", "sendMessageWithReference", org.holodeck.logging.persistent.LoggerMessage.MESSAGE_SENT_KO_STATUS));

			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"Error writing data into temporal directory[" + tempDir + "]", e, Code.ERROR_SEND_002);
			throw sendMessageServiceException;
		}

		UserMsgToPush userMsgToPush = sendMessageHelper.submitFromFolder(tempDir);

		String messageId = sendMessageHelper.send(userMsgToPush);

		try {
			FileUtils.deleteDirectory(tempDir);
		} catch (IOException e) {
			log.error("Error deleting temporal directory[" + tempDir + "]", e);
		}
		
	    log.log(org.holodeck.logging.level.Message.MESSAGE, org.holodeck.backend.util.Converter.convertUserMessageToMessageInfo(messaging.getMessaging().getUserMessage()[0], messageId, "SendMessageService", "sendMessageWithReference", org.holodeck.logging.persistent.LoggerMessage.MESSAGE_SENT_OK_STATUS));
	    
	    backend.ecodex.org.SendResponse sendResponse = new backend.ecodex.org.SendResponse();
	    sendResponse.setMessageID(messageId);
	    
	    return sendResponse;
	}
}
