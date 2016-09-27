package eu.domibus.connector.gui.config;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;

public class DomibusConnectorConfigUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 191909279237339065L;
	
	public DomibusConnectorConfigUI(){
		if(ConnectorProperties.CONNECTOR_PROPERTIES_FILE.exists()){
			try {
				ConnectorProperties.loadConnectorProperties();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
			JFrame.setDefaultLookAndFeelDecorated(true);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(DomibusConnectorConfigUI.this, 
		        		"All changes since the last save will be discarded. \n Do you really want to exit?", "Exit", 
		        		JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION){
		            System.exit(0);
		        }
		    }
		});
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(700, 650));
        setTitle("DomibusConnector Configuration UI");

        setState(Frame.NORMAL);
        getContentPane().add(new ConfigMenu(this), BorderLayout.PAGE_START);
        getContentPane().add(new ConfigTab(), BorderLayout.CENTER);
        getContentPane().add(new ConfigButtonBar(), BorderLayout.PAGE_END);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        if(!ConnectorProperties.CONNECTOR_PROPERTIES_FILE.exists()){
        	JOptionPane.showMessageDialog(this, "No configuration found. Please edit or import configuration and restart domibusConnector.");
        }
	}

}
