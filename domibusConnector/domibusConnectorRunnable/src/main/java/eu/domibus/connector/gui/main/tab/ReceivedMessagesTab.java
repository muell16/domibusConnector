package eu.domibus.connector.gui.main.tab;

import java.util.List;

import eu.domibus.connector.gui.main.data.Message;
import eu.domibus.connector.gui.main.reader.ReceivedMessagesReader;

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
		return ReceivedMessagesReader.readMessages();
	}

	@Override
	public String getTableHeader5() {
		return "Received";
	}


	@Override
	public String getMessageStatus(Message msg) {
		return "STORED";
	}
}
