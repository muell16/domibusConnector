package eu.domibus.connector.gui.main;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import eu.domibus.connector.gui.main.tab.ReceivedMessagesTab;
import eu.domibus.connector.gui.main.tab.SendNewMessageTab;
import eu.domibus.connector.gui.main.tab.SentMessagesTab;

public class DomibusConnectorMainTab extends JTabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3277796843774847324L;

	public DomibusConnectorMainTab(){
		
		final ReceivedMessagesTab receivedMessages = new ReceivedMessagesTab();
		this.add("Received messages", receivedMessages);
		final SentMessagesTab sentMessages = new SentMessagesTab();
		this.add("Sent messages", sentMessages);
		this.add("Send new message", new SendNewMessageTab());
		this.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				receivedMessages.refresh();
				sentMessages.refresh();
			}
		});
	}
}
