package eu.domibus.connector.gui.main;

import javax.swing.JTabbedPane;

import eu.domibus.connector.gui.main.tab.ReceivedMessagesTab;
import eu.domibus.connector.gui.main.tab.SendNewMessageTab;
import eu.domibus.connector.gui.main.tab.SentMessagesTab;

public class DomibusConnectorMainTab extends JTabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3277796843774847324L;

	public DomibusConnectorMainTab(){
		
		this.add("Received messages", new ReceivedMessagesTab());
		this.add("Sent messages", new SentMessagesTab());
		this.add("Send new message", new SendNewMessageTab());
	}
}
