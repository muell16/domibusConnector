package eu.domibus.connector.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import eu.domibus.connector.gui.config.listener.ConfigSaveCloseListener;

public class DomibusConnectorMainMenu extends JMenuBar implements ActionListener {

	public DomibusConnectorMainMenu(){
		JMenu fileMenu = buildFileMenu();
		this.add(fileMenu);
		
	}
	
	private JMenu buildFileMenu() {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

//		JMenuItem saveItem = new JMenuItem(ConfigSaveCloseListener.SAVE);
//		saveItem.setMnemonic(KeyEvent.VK_S);
//		saveItem.addActionListener(this);
//		fileMenu.add(saveItem);
//
//		JMenuItem saveExitItem = new JMenuItem(ConfigSaveCloseListener.SAVEEXIT);
//		saveExitItem.setMnemonic(KeyEvent.VK_X);
//		saveExitItem.addActionListener(this);
//		fileMenu.add(saveExitItem);

		JMenuItem exitItem = new JMenuItem("Shutdown domibusConnector");
		exitItem.setMnemonic(KeyEvent.VK_E);
		exitItem.addActionListener(this);
		fileMenu.add(exitItem);
		return fileMenu;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Shutdown domibusConnector")) {
			if (JOptionPane.showConfirmDialog(this, 
					"This will also shut down the domibusConnector itself. \n Continue?", "Exit", 
	        		JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION){
	            System.exit(0);
	        }
		}
		
	}

}
