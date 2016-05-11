package eu.domibus.connector.gui.main.tab;

import java.util.List;

import javax.swing.JFrame;

import eu.domibus.connector.gui.main.data.Message;
import eu.domibus.connector.gui.main.reader.SentMessagesReader;

public class SentMessagesTab extends MessagesTab {

	

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
}
