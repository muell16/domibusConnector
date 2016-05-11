package eu.domibus.connector.gui.main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import eu.domibus.connector.gui.DomibusConnectorUI;
import eu.domibus.connector.gui.main.tab.ReceivedMessagesTab;
import eu.domibus.connector.gui.main.tab.SendNewMessageTab;
import eu.domibus.connector.gui.main.tab.SentMessagesTab;

public class DomibusConnectorMainTab extends JTabbedPane {

	public DomibusConnectorMainTab(){
		
		this.add("Received messages", new ReceivedMessagesTab());
		this.add("Sent messages", new SentMessagesTab());
		this.add("Send new message", new SendNewMessageTab());
	}
}
