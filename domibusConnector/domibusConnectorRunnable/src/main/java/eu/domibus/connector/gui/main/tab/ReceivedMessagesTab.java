package eu.domibus.connector.gui.main.tab;

import java.util.List;

import javax.swing.JFrame;

import eu.domibus.connector.gui.main.data.Message;
import eu.domibus.connector.gui.main.reader.ReceivedMessagesReader;

public class ReceivedMessagesTab extends MessagesTab {

	

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
}
