package eu.domibus.connector.gui.main.tab;

import java.util.List;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.main.data.Message;
import eu.domibus.connector.gui.main.reader.MessagesReader;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableConstants;

public class SentMessagesTab extends MessagesTab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1221226573691412944L;
	static final String STATUS_NEW = "NEW";
	static final String STATUS_READY = "READY TO BE SENT";
	static final String STATUS_PROCESSING = "PROCESSING";
	static final String STATUS_SENT = "SENT";
	static final String STATUS_FAILED = "FAILED";
	
	public SentMessagesTab() {
		super();
	}

	@Override
	public List<Message> loadMessages() throws Exception {
		return MessagesReader.readMessages(ConnectorProperties.OTHER_OUTGOING_MSG_DIR_KEY, ConnectorProperties.outgoingMessagesDirectory);
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

	@Override
	public int getMessageType() {
		return DomibusConnectorRunnableConstants.MESSAGE_TYPE_OUTGOING;
	}

	@Override
	public String getMessageDatetime(Message msg) {
		return msg.getMessageProperties().getMessageSentDatetime();
	}
}
