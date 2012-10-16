/*
 * 
 */
package org.holodeck.backend.consumer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.axiom.attachments.Attachments;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;
import org.holodeck.backend.db.dao.MessageDAO;
import org.holodeck.backend.db.model.Message;
import org.holodeck.backend.db.model.Payload;
import org.holodeck.backend.util.IOUtils;
import org.holodeck.common.soap.Util;
import org.holodeck.ebms3.consumers.EbConsumer;
import org.holodeck.ebms3.module.MsgInfo;
import org.holodeck.ebms3.module.PartInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class BackendConsumer.
 */
public class BackendConsumer extends org.holodeck.backend.spring.BackendSpringBeanAutowiringSupport implements EbConsumer {
	
	/** The log. */
	private static Logger log = Logger.getLogger(BackendConsumer.class);

	/** The parameters. */
	protected Map<String, String> parameters;

	/** The message dao. */
	@Autowired
	private MessageDAO messageDAO;


	/* (non-Javadoc)
	 * @see org.holodeck.ebms3.consumers.EbConsumer#setParameters(java.util.Map)
	 */
	public void setParameters(Map<String, String> properties) {
		this.parameters = properties;
	}

	/* (non-Javadoc)
	 * @see org.holodeck.ebms3.consumers.EbConsumer#push(org.holodeck.ebms3.module.MsgInfo, org.apache.axis2.context.MessageContext)
	 */
	public void push(MsgInfo msgInfo, MessageContext outMsgCtx) {
		log.debug("Saving push message");

		init();

		MessageContext msgCtx = MessageContext.getCurrentMessageContext();
		File directory = getSaveLocation(msgInfo.getMpc());
		writeAttachments(msgCtx, msgInfo.getParts(), directory);
		writeMessaging(msgCtx, msgInfo.getParts(), directory);

		Message message = saveMessage(msgInfo, directory);

		File messageFile = new File(message.getDirectory(), org.holodeck.backend.module.Constants.MESSAGING_FILE_NAME);

		org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE messaging = org.holodeck.backend.util.Converter.convertFileToMessagingE(messageFile);
		
	    log.log(org.holodeck.logging.level.Message.MESSAGE, org.holodeck.backend.util.Converter.convertUserMessageToMessageInfo(messaging.getMessaging().getUserMessage()[0], Integer.toString(message.getIdMessage()), "BackendConsumer", "push", org.holodeck.logging.persistent.LoggerMessage.MESSAGE_RECEIVED_STATUS));

		log.debug("Saved push message");
	}

	/* (non-Javadoc)
	 * @see org.holodeck.ebms3.consumers.EbConsumer#pull(org.holodeck.ebms3.module.MsgInfo, org.apache.axis2.context.MessageContext)
	 */
	public void pull(MsgInfo msgInfo, MessageContext outMsgCtx) {
	}

	/**
	 * Save message.
	 *
	 * @param msgInfo the msg info
	 * @param directory the directory
	 */
	private Message saveMessage(MsgInfo msgInfo, File directory){
		Message message = new Message();
		message.setMessageDate(new Date());
		message.setMessageUID(msgInfo.getMessageId());
		message.setPmode(msgInfo.getPmode());
		message.setDirectory(directory.getAbsolutePath());

		List<Payload> payloads = new ArrayList<Payload>();

		int counter = 1;

		for (PartInfo part : msgInfo.getParts()) {
			Payload payload = new Payload();

			String payloadFileName = MessageFormat.format(
					org.holodeck.backend.module.Constants.PAYLOAD_FILE_NAME_FORMAT, counter);

			payload.setFileName(payloadFileName);
			payload.setMessage(message);

			payloads.add(payload);

			counter++;
		}
		message.setPayloads(payloads);

		messageDAO.save(message);
		
		return message;
	}

	/**
	 * Gets the save location.
	 *
	 * @param mpc the mpc
	 * @return the save location
	 */
	private File getSaveLocation(String mpc) {
		String dir = parameters.get("directory");
		if (mpc == null || mpc.trim().equals(""))
			mpc = "default";
		if (mpc.startsWith("mpc://"))
			mpc = mpc.substring(6);
		if (mpc.startsWith("http://"))
			mpc = mpc.substring(7);
		mpc = mpc.replaceAll(":", "-");
		mpc = mpc.replaceAll("/", "_");
		if (dir == null || dir.trim().equals(""))
			dir = "Messages_" + mpc + File.separator + "Msg_" + getDate();
		else
			dir = dir + "_" + mpc + File.separator + "Msg_" + getDate();
		File location = new File(dir);
		if (location.exists() && location.isDirectory())
			return location;

		String receivedMsgsFolder = org.holodeck.backend.module.Constants.getMessagesFolder();

		String path = receivedMsgsFolder + File.separator + dir;
		File saveFolder = new File(path);
		if (!saveFolder.mkdirs()) {
			log.error("Unable to create direcoty " + path);
		}
		return saveFolder;
	}

	/**
	 * Write messaging.
	 *
	 * @param msgCtx the msg ctx
	 * @param parts the parts
	 * @param directory the directory
	 */
	private void writeMessaging(MessageContext msgCtx, List<PartInfo> parts, File directory) {
		if (msgCtx == null || parts == null || parts.size() <= 0)
			return;

		Attachments atts = msgCtx.getAttachmentMap();

		if (atts == null)
			return;

		java.util.Iterator<OMElement> iterator = msgCtx.getEnvelope().getHeader().getChildren();
		
		org.apache.axiom.om.OMElement messaging = null;
		
		while(iterator.hasNext()){
			org.apache.axiom.om.OMElement omElement = iterator.next();
			
			if(omElement.getLocalName().equalsIgnoreCase(org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessagingE.MY_QNAME.getLocalPart())){
				messaging = omElement;
			}
		}

		try {
			IOUtils.write(messaging.toString(), new FileOutputStream(new File(directory, org.holodeck.backend.module.Constants.MESSAGING_FILE_NAME)));
		} catch (Exception e) {
			log.error("Unable to write messaging file", e);
		}
		
	}

	/**
	 * Write attachments.
	 *
	 * @param msgCtx the msg ctx
	 * @param parts the parts
	 * @param directory the directory
	 */
	private void writeAttachments(MessageContext msgCtx, List<PartInfo> parts, File directory) {
		if (msgCtx == null || parts == null || parts.size() <= 0)
			return;

		Attachments atts = msgCtx.getAttachmentMap();

		int counter = 1;

		for (PartInfo part : parts) {
			DataHandler dh = atts.getDataHandler(part.getCid());

			String payloadFileName = MessageFormat.format(
					org.holodeck.backend.module.Constants.PAYLOAD_FILE_NAME_FORMAT, counter);

			File att = new File(directory, payloadFileName);
			Util.writeDataHandlerToFile(dh, att);

			counter++;
		}
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	private String getDate() {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(org.holodeck.backend.module.Constants.PATTERN_DATE_FORMAT);
		return sdf.format(now.getTime());
	}
}
