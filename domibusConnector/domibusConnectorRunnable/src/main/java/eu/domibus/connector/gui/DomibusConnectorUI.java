package eu.domibus.connector.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.main.DomibusConnectorMainMenu;
import eu.domibus.connector.gui.main.DomibusConnectorMainTab;

public class DomibusConnectorUI extends JFrame {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6655274520853778448L;

	public void init(){
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
		        if (JOptionPane.showConfirmDialog(DomibusConnectorUI.this, 
		        		"This will also shut down the domibusConnector itself. \n Continue?", "Exit", 
		        		JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION){
		            System.exit(0);
		        }
		    }
		});
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(new Dimension(800, 650));
        setTitle("DomibusConnector");

        setState(Frame.NORMAL);
        getContentPane().add(new DomibusConnectorMainMenu(), BorderLayout.NORTH);
        getContentPane().add(new DomibusConnectorMainTab(), BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
	}
}
