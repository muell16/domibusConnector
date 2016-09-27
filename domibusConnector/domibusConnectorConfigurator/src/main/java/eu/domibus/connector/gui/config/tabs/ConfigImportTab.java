package eu.domibus.connector.gui.config.tabs;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import eu.domibus.connector.gui.config.DomibusConnectorConfigUI;
import eu.domibus.connector.gui.config.properties.ConnectorProperties;

public class ConfigImportTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2028192076301203873L;
	JFileChooser fc;
	JTextArea log;
	File chosenConfigFile;
	JFrame parent;

	public ConfigImportTab(JFrame parent) {
		this.parent = parent;
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JPanel textPanel = createTextPanel();
		JPanel filePanel = createFilePanel();
		JPanel importPanel = createImportPanel();

		c.gridx = 0;
		c.gridy = 0;
		gridPanel.add(textPanel, c);
		c.gridx = 0;
		c.gridy = 1;
		gridPanel.add(filePanel, c);
		c.fill=GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 3;
		gridPanel.add(importPanel, c);
		
		add(gridPanel);
	}

	private JPanel createImportPanel() {
		JPanel importPanel = createEmptyPanel();
		
		JButton importButton = new JButton("Import properties");
		importButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chosenConfigFile==null){
					JOptionPane.showMessageDialog(ConfigImportTab.this, "No file selected. Please select the property File first.");
					return;
				}else if(!chosenConfigFile.exists()){
					JOptionPane.showMessageDialog(ConfigImportTab.this, "The selected property file does not exist.");
					return;
				}
				
				if (JOptionPane.showConfirmDialog(ConfigImportTab.this, 
		        		"All current properties will be overridden and File will be replaced. \n Do you want to continue?", "Exit", 
		        		JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION){
		            importProperties();
		        }
				
				
			}

			private void importProperties() {
				File configDir = ConnectorProperties.CONNECTOR_PROPERTIES_DIR;
				if(!configDir.exists()){
					configDir.mkdirs();
				}
				
				File configFile = ConnectorProperties.CONNECTOR_PROPERTIES_FILE;
				if(!configFile.exists()){
					try {
						configFile.createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
				try {
					Files.copy(chosenConfigFile.toPath(), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try{
					ConnectorProperties.loadConnectorProperties();
					JOptionPane.showMessageDialog(ConfigImportTab.this, "The property file is successfully imported. \n The properties are reloaded.");
				}catch (Exception e){
					JOptionPane.showMessageDialog(ConfigImportTab.this, "The properties could not be reloaded!", "Import properties failed!", JOptionPane.ERROR_MESSAGE);
				}
				
//				ConnectorProperties.loadConnectorProperties();
				JFrame f1 = (JFrame) SwingUtilities.windowForComponent(ConfigImportTab.this);
				f1.setVisible(false);
				f1.dispose();
				
				parent.setVisible(false);
				parent.dispose();
				new DomibusConnectorConfigUI();
				
			}
		});
		
		importPanel.add(importButton);
		
		return importPanel;
	}
	

	private JPanel createFilePanel() {
		JPanel filePanel = createEmptyPanel();

		fc = new JFileChooser();

		JButton openButton = new JButton(UIManager.getIcon("FileView.directoryIcon"));
		openButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				    int returnVal = fc.showOpenDialog(ConfigImportTab.this);

		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		            	chosenConfigFile = fc.getSelectedFile();
		            	log.setText(chosenConfigFile.getAbsolutePath().replace('\\', '/'));
		            }
		   
			}
		});
		
		log = new JTextArea(1,20);
        log.setEditable(false);
		
		filePanel.add(openButton);
		filePanel.add(log);

		return filePanel;
	}

	private JPanel createTextPanel() {
		JPanel textPanel = createEmptyPanel();

		JLabel importLabel = new JLabel(
				"If there are already existing properties that are configured, you can import them into this connector instance.");

		importLabel.setVisible(true);

		textPanel.add(importLabel);
		return textPanel;
	}
	
	private JPanel createEmptyPanel(){
		FlowLayout panelLayout = new FlowLayout();
		panelLayout.setAlignment(FlowLayout.LEFT);
		
		JPanel panel = new JPanel();
		
		
		panel.setLayout(panelLayout);
		return panel;
	}

}
