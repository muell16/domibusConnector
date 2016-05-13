package eu.domibus.connector.gui.main.tab;

import java.util.List;

import eu.domibus.connector.gui.main.data.Message;
import eu.domibus.connector.gui.main.reader.SentMessagesReader;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableConstants;

public class SentMessagesTab extends MessagesTab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1221226573691412944L;
	private static final String STATUS_NEW = "NEW";
	private static final String STATUS_READY = "READY TO BE SENT";
	private static final String STATUS_PROCESSING = "PROCESSING";
	private static final String STATUS_SENT = "SENT";
	private static final String STATUS_FAILED = "FAILED";
	
	public SentMessagesTab() {
		super();
	}

	@Override
	public List<Message> loadMessages() throws Exception {
		return SentMessagesReader.readMessages();
	}

	@Override
	public String getTableHeader5() {
		return "Sent";
	}

	@Override
	public String getMessageStatus(Message msg) {
		if(msg.getMessageDir().getName().contains("_")){
		String postfix = msg.getMessageDir().getName().substring(msg.getMessageDir().getName().lastIndexOf("_"));
		switch(postfix){
		case DomibusConnectorRunnableConstants.MESSAGE_NEW_FOLDER_POSTFIX: return STATUS_NEW;
		case DomibusConnectorRunnableConstants.MESSAGE_READY_FOLDER_POSTFIX: return STATUS_READY;
		case DomibusConnectorRunnableConstants.MESSAGE_PROCESSING_FOLDER_POSTFIX: return STATUS_PROCESSING;
		case DomibusConnectorRunnableConstants.MESSAGE_SENT_FOLDER_POSTFIX: return STATUS_SENT;
		case DomibusConnectorRunnableConstants.MESSAGE_FAILED_FOLDER_POSTFIX: return STATUS_FAILED;
		default:return "unknown";
		}
		}
		return "unknown";
	}
}
