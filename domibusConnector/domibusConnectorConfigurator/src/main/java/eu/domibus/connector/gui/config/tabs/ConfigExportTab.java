package eu.domibus.connector.gui.config.tabs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;

public class ConfigExportTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3727473921936128057L;
	JFileChooser fc;
	JTextArea log;
	File chosenExportDir;
	JFrame parent;
	
	public ConfigExportTab(final JFrame parent){
		this.parent = parent;
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JPanel textPanel = createFileChooserPanel();
		
		c.gridx = 0;
		c.gridy = 0;
		gridPanel.add(textPanel, c);
		
		JButton exportButton = new JButton("Export");
		exportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					FileUtils.copyFileToDirectory(ConnectorProperties.CONNECTOR_PROPERTIES_FILE,chosenExportDir);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(ConfigExportTab.this, "Properties File could not be exported.\n "+e1.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
				}
				
				JOptionPane.showMessageDialog(ConfigExportTab.this, 
		        		"Configured properties successfully exported to "+chosenExportDir.getAbsolutePath(), "Export success", 
		        		JOptionPane.INFORMATION_MESSAGE);
					parent.dispose();
					parent.setVisible(false);
		        
			}
		});

		c.fill=GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 1;
		gridPanel.add(exportButton, c);
		
		
		
		add(gridPanel);
	}
	
	private JPanel createFileChooserPanel(){
		JPanel fileChooserPanel = new JPanel();
		final JFileChooser exportDirFc = new JFileChooser();
		exportDirFc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		final JTextField exportDir = new JTextField(35);
		final JButton exportDirPath = ConfigTabHelper.addFileChooserRow(fileChooserPanel, "Please select export folder:", 
				null, exportDir);
		exportDirPath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = exportDirFc.showOpenDialog(ConfigExportTab.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					exportDir.setText(exportDirFc.getSelectedFile().getAbsolutePath().replace('\\', '/'));
					chosenExportDir = exportDirFc.getSelectedFile();
				}

			}
		});
		exportDir.setEditable(false);
		exportDirFc.setEnabled(false);
		
		return fileChooserPanel;
	}
}
