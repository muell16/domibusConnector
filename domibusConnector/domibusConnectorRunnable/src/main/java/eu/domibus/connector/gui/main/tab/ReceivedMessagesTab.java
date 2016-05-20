package eu.domibus.connector.gui.main.tab;

import java.util.List;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.main.data.Message;
import eu.domibus.connector.gui.main.reader.MessagesReader;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableConstants;

public class ReceivedMessagesTab extends MessagesTab {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3111650760607967459L;


	public ReceivedMessagesTab() {
		super();
	}

	@Override
	public List<Message> loadMessages() throws Exception {
		return MessagesReader.readMessages(ConnectorProperties.OTHER_INCOMING_MSG_DIR_KEY, ConnectorProperties.incomingMessagesDirectory);
	}

	@Override
	public String getTableHeader5() {
		return "Received";
	}


	@Override
	public String getMessageStatus(Message msg) {
		return "STORED";
	}

	@Override
	public int getMessageType() {
		return DomibusConnectorRunnableConstants.MESSAGE_TYPE_INCOMING;
	}

	@Override
	public String getMessageDatetime(Message msg) {
		return msg.getMessageProperties().getMessageReceivedDatetime();
	}
}
