package org.holodeck.ebms3.consumers.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;
import org.holodeck.common.soap.Util;
import org.holodeck.ebms3.consumers.EbConsumer;
import org.holodeck.ebms3.module.Constants;
import org.holodeck.ebms3.module.MsgInfo;
import org.holodeck.ebms3.module.PartInfo;

/**
 * @author Hamid Ben Malek
 */
public class SaveToFolder implements EbConsumer {
	static Logger logger = Logger.getLogger(SaveToFolder.class);
	protected Map<String, String> parameters;

	public void setParameters(Map<String, String> properties) {
		this.parameters = properties;
	}

	public void push(MsgInfo msgInfo, MessageContext outMsgCtx) {
		System.out
				.println("======================= SaveToFolder Consumer ===================");
		MessageContext msgCtx = MessageContext.getCurrentMessageContext();
		String directory = getSaveLocation(msgInfo.getMpc());
		writeAttachments(msgCtx, msgInfo.getParts(), directory);
		writeEnvelope(msgCtx, directory);
		writeSoapHeader(msgCtx, directory);
		System.out
				.println("=================================================================");
	}

	public void pull(MsgInfo msgInfo, MessageContext outMsgCtx) {
	}

	private String getSaveLocation(String mpc) {
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
			return dir;
		String receivedMsgsFolder = org.holodeck.ebms3.module.Constants
				.getReceivedFolder();
		String path = receivedMsgsFolder + File.separator + dir;
		boolean b = new File(path).mkdirs();
		if (!b)
			System.out.println("Unable to create direcoty " + path);
		return path;
	}

	private void writeEnvelope(MessageContext msgCtx, String location) {
		try {
			String file = location + File.separator + "envelope.xml";
			Util.prettyPrint(msgCtx.getEnvelope(), file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void writeAttachments(MessageContext msgCtx, List<PartInfo> parts,
			String location) {
		if (msgCtx == null || parts == null || parts.size() <= 0)
			return;
		List<String> cids = new ArrayList<String>();
		List<String> cts = new ArrayList<String>();
		for (PartInfo part : parts) {
			cids.add(part.getCid());
			cts.add(part.getMimeType());
		}
		Util.writeAttachments(msgCtx, cids, cts, new File(location));
	}

	private void writeSoapHeader(MessageContext msgCtx, String location) {
		OMElement header = (OMElement) msgCtx
				.getProperty(Constants.IN_SOAP_HEADER);
		if (header == null)
			return;
		try {
			
			String file = location + File.separator + "SOAP-Header.xml";
			Util.prettyPrint(header, file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String getDate() {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMM.dd'@'HH.mm.ss");
		return sdf.format(now.getTime());
	}
}