package eu.domibus.connector.gui.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import eu.domibus.connector.gui.config.listener.ConfigSaveCloseListener;
import eu.domibus.connector.gui.config.tabs.ConfigExportTab;
import eu.domibus.connector.gui.config.tabs.ConfigImportTab;

public class ConfigMenu extends JMenuBar implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3965149286811111188L;

	public ConfigMenu(final JFrame parent) {
		JMenu fileMenu = buildFileMenu();

		this.add(fileMenu);
		
		JMenu toolsMenu = buildToolsMenu(parent);
		
		this.add(toolsMenu);

		this.setVisible(true);
	}

	private JMenu buildToolsMenu(final JFrame parent) {
		JMenu toolsMenu = new JMenu("Tools");
		toolsMenu.setMnemonic(KeyEvent.VK_T);
		
		JMenuItem importProperties = new JMenuItem("Import Properties");
		importProperties.setMnemonic(KeyEvent.VK_I);
		importProperties.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame importFrame = new JFrame("Import configured porperties");
				importFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				importFrame.setContentPane(new ConfigImportTab(parent));
				importFrame.pack();
				importFrame.setVisible(true);
			}
		});
		
		toolsMenu.add(importProperties);
		
		JMenuItem exportProperties = new JMenuItem("Export Properties");
		exportProperties.setMnemonic(KeyEvent.VK_E);
		exportProperties.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame exportFrame = new JFrame("Export configured porperties");
				exportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				exportFrame.setContentPane(new ConfigExportTab(exportFrame));
				exportFrame.pack();
				exportFrame.setVisible(true);
			}
		});
		
		toolsMenu.add(exportProperties);
		
		return toolsMenu;
	}

	private JMenu buildFileMenu() {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		JMenuItem saveItem = new JMenuItem(ConfigSaveCloseListener.SAVE);
		saveItem.setMnemonic(KeyEvent.VK_S);
		saveItem.addActionListener(this);
		fileMenu.add(saveItem);

		JMenuItem saveExitItem = new JMenuItem(ConfigSaveCloseListener.SAVEEXIT);
		saveExitItem.setMnemonic(KeyEvent.VK_X);
		saveExitItem.addActionListener(this);
		fileMenu.add(saveExitItem);

		JMenuItem exitItem = new JMenuItem(ConfigSaveCloseListener.EXIT);
		exitItem.setMnemonic(KeyEvent.VK_E);
		exitItem.addActionListener(this);
		fileMenu.add(exitItem);
		return fileMenu;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ConfigSaveCloseListener.actionPerformed(e, this);
		
	}
}
