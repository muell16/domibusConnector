/*
 * 
 */
package org.holodeck.backend.service.helper;

import java.io.File;

import org.apache.axis2.client.async.AxisCallback;
import org.apache.log4j.Logger;
import org.holodeck.backend.service.exception.SendMessageServiceException;
import org.holodeck.common.soap.Util;
import org.holodeck.ebms3.module.Constants;
import org.holodeck.ebms3.persistent.UserMsgToPush;
import org.holodeck.ebms3.submit.MsgInfoSet;
import org.holodeck.ebms3.submit.SubmitUtil;
import org.springframework.stereotype.Service;

import backend.ecodex.org.Code;

/**
 * The Class SendMessageHelper.
 */
@Service
public class SendMessageHelper {
	
	/** The Constant log. */
	private final static Logger log = Logger.getLogger(SendMessageHelper.class);

	/**
	 * Send.
	 *
	 * @param message the message
	 * @throws SendMessageServiceException the send message service exception
	 */
	public void send(UserMsgToPush message) throws SendMessageServiceException {
		AxisCallback cb = null;
		if (message.getCallbackClass() != null && !message.getCallbackClass().trim().equals("")) {
			try {
				cb = (AxisCallback) Util.createInstance(message.getCallbackClass());
			} catch (Exception e) {
				SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
						"Error while creating AxisCallback", e, Code.ERROR_SEND_002);
				throw sendMessageServiceException;
			}
		}
		MsgInfoSet metadata = message.getMsgInfoSetBean();
		log.debug("SenderWorker: about to send to " + message.getToURL());

		try{
			message.send(metadata, cb);
		} catch (RuntimeException e) {
			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"Error while sending message", e, Code.ERROR_SEND_003);
			throw sendMessageServiceException;
		} catch (Exception e) {
			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"Error while sending message", e, Code.ERROR_SEND_003);
			throw sendMessageServiceException;
		}
	}

	/**
	 * Submit from folder.
	 *
	 * @param folder the folder
	 * @return the user msg to push
	 * @throws SendMessageServiceException the send message service exception
	 */
	public UserMsgToPush submitFromFolder(File folder) throws SendMessageServiceException {
		if (folder == null || !folder.exists()){
			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"Temporal directory not found", Code.ERROR_SEND_002);
			throw sendMessageServiceException;
		}

		MsgInfoSet mis = null;
		try {
			mis = readMeta(folder);
		} catch (Exception e) {
			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"Error reading metadata.xml from temporal directory", e, Code.ERROR_SEND_002);
			throw sendMessageServiceException;
		}
		if (mis == null) {
			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"Error reading metadata.xml from temporal directory", Code.ERROR_SEND_002);
			throw sendMessageServiceException;
		}
		String bodyPayload = mis.getBodyPayload();
		log.debug("[ SendMessageHelper ] is scanning message folder " + folder.getName());
		log.debug("body payload is " + bodyPayload);
		File[] files = folder.listFiles();
		if (files == null || files.length == 0) {
			SendMessageServiceException sendMessageServiceException = new SendMessageServiceException(
					"No files found in the temporal directory", Code.ERROR_SEND_002);
			throw sendMessageServiceException;
		}
		UserMsgToPush userMsgToPush = new UserMsgToPush(folder, mis);
		Constants.store.save(userMsgToPush);
		log.debug("UserMsgToPush was submitted to database");

		return userMsgToPush;
	}

	/**
	 * Rename metadata.
	 *
	 * @param folder the folder
	 * @return true, if successful
	 */
	private static boolean renameMetadata(File folder) {
		if (folder == null)
			return false;
		File meta = new File(folder.getAbsolutePath() + File.separator + "metadata.xml");
		File metaRenamed = new File(folder.getAbsolutePath() + File.separator + "metadata.xml.processed");
		return meta.renameTo(metaRenamed);
	}

	/**
	 * Read meta.
	 *
	 * @param folder the folder
	 * @return the msg info set
	 */
	private static synchronized MsgInfoSet readMeta(File folder) {
		if (folder == null || !folder.exists())
			return null;
		File meta = new File(folder.getAbsolutePath() + File.separator + "metadata.xml");
		if (!meta.exists())
			return null;
		renameMetadata(folder);
		meta = new File(folder.getAbsolutePath() + File.separator + "metadata.xml.processed");
		MsgInfoSet mis = MsgInfoSet.read(meta);
		mis.setLegNumber(SubmitUtil.getLegNumber(mis));
		return mis;
	}
}
