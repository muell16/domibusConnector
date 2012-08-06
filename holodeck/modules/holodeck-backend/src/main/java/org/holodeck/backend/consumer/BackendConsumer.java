/*
 * 
 */
package org.holodeck.backend.consumer;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.axiom.attachments.Attachments;
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;
import org.holodeck.backend.db.dao.MessageDAO;
import org.holodeck.backend.db.model.Message;
import org.holodeck.backend.db.model.Payload;
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

		saveMessage(msgInfo, directory);

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
	private void saveMessage(MsgInfo msgInfo, File directory){
		Message message = new Message();
		message.setDate(new Date());
		message.setUid(msgInfo.getMessageId());
		message.setPmode(msgInfo.getPmode());
		message.setDirectory(directory.getAbsolutePath());

		List<Payload> payloads = new ArrayList<Payload>();

		int counter = 1;

		for (PartInfo part : msgInfo.getParts()) {
			if (part.getCid() != null
					&& org.holodeck.backend.module.Constants.MESSAGING_FILE_NAME.equalsIgnoreCase(part.getCid())) {
				continue;
			}

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
			System.out.println("Unable to create direcoty " + path);
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

		String cid = null;

		for (PartInfo part : parts) {
			if (part.getCid() != null
					&& org.holodeck.backend.module.Constants.MESSAGING_FILE_NAME.equalsIgnoreCase(part.getCid())) {
				cid = part.getCid();
			}
		}

		if (cid == null)
			return;

		String soapPartCid = atts.getSOAPPartContentID();

		if (cid != null && !cid.equals(soapPartCid)) {
			DataHandler dh = atts.getDataHandler(cid);
			String name = org.holodeck.backend.module.Constants.MESSAGING_FILE_NAME;
			File att = new File(directory, name);
			Util.writeDataHandlerToFile(dh, att);
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
			if (part.getCid() != null
					&& org.holodeck.backend.module.Constants.MESSAGING_FILE_NAME.equalsIgnoreCase(part.getCid())) {
				continue;
			}

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
