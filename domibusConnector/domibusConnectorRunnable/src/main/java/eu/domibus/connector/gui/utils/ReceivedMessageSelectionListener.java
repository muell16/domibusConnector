package eu.domibus.connector.gui.utils;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ReceivedMessageSelectionListener implements ListSelectionListener {

	@Override
	public void valueChanged(ListSelectionEvent e) {
		System.out.println(e.toString());

	}

}
