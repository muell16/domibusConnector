package eu.domibus.connector.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import eu.domibus.connector.gui.config.ConfigButtonBar;
import eu.domibus.connector.gui.config.ConfigMenu;
import eu.domibus.connector.gui.config.ConfigTab;
import eu.domibus.connector.gui.config.DomibusConnectorConfigUI;

public class DomibusConnectorUI extends JFrame {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6655274520853778448L;

	public void init(){
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
		        		"All changes since the last save will be discarded. \n Do you really want to exit?", "Exit", 
		        		JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION){
		            System.exit(0);
		        }
		    }
		});
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(new Dimension(600, 650));
        setTitle("DomibusConnector");

        setState(Frame.NORMAL);
//        getContentPane().add(new ConfigMenu(this), BorderLayout.PAGE_START);
//        getContentPane().add(new ConfigTab(), BorderLayout.CENTER);
//        getContentPane().add(new ConfigButtonBar(), BorderLayout.PAGE_END);
        setVisible(true);
	}
}
