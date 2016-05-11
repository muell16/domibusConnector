package eu.domibus.connector.gui.config.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;

public class ConfigSaveCloseListener {

	public static final String SAVE = "Save properties";
	public static final String SAVEEXIT = "Save properties and Exit";
	public static final String EXIT = "Exit without saving";
	
	public static void actionPerformed(ActionEvent e, Component parent) {
		if (e.getActionCommand().equals(SAVE)) {
			if(ConnectorProperties.storeConnectorProperties()){
				JOptionPane.showMessageDialog(parent, "The properties are successfully stored and the property file is updated.");
			}else{
				JOptionPane.showMessageDialog(parent, "The properties could not be stored!", "Store properties failed!", JOptionPane.ERROR_MESSAGE);
			}
			
		} else if (e.getActionCommand().equals(SAVEEXIT)) {
			if(ConnectorProperties.storeConnectorProperties()){
				System.exit(0);
			}else{
				JOptionPane.showMessageDialog(parent, "The properties could not be stored!", "Store properties failed!", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getActionCommand().equals(EXIT)) {
			if (JOptionPane.showConfirmDialog(parent, 
	        		"All changes since the last save will be discarded. \n Do you really want to exit?", "Exit", 
	        		JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION){
	            System.exit(0);
	        }
		}
	}

}
