package eu.domibus.connector.gui.config.tabs;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.layout.SpringUtilities;

public class ConfigOtherTab extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8898156391956995907L;

	public ConfigOtherTab(){
		JPanel helpPanel = ConfigTabHelper.buildHelpPanel("Other Configuration Help", "OtherConfigurationHelp.htm");
		BorderLayout mgr = new BorderLayout();
		setLayout(mgr);
		add(helpPanel, BorderLayout.EAST);
		
		JPanel disp = new JPanel();
		
		JPanel frameworkPanel = buildFrameworkPanel();
		
		disp.add(frameworkPanel);
		
		JPanel standalonePanel = buildStandalonePanel();
		
		disp.add(standalonePanel);
		
		add(disp);
	}

	private JPanel buildStandalonePanel() {
		JPanel standalonePanel = new JPanel(new SpringLayout());
		
		JLabel header = new JLabel("Standalone connector settings:");
		header.setFont(header.getFont().deriveFont(Font.BOLD));
		standalonePanel.add(header);
		standalonePanel.add(new JLabel(""));
		standalonePanel.add(new JLabel(""));
		
		final JFileChooser incomingMsgDirFc = new JFileChooser();
		incomingMsgDirFc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		incomingMsgDirFc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		final JTextField incomingMsgDir = new JTextField(35);
		final JButton incomingMsgDirPath = ConfigTabHelper.addFileChooserRow(standalonePanel, ConnectorProperties.OTHER_INCOMING_MSG_DIR_LABEL, 
				ConnectorProperties.incomingMessagesDirectory, incomingMsgDir);
		incomingMsgDirPath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				    int returnVal = incomingMsgDirFc.showOpenDialog(ConfigOtherTab.this);

		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		            	ConnectorProperties.incomingMessagesDirectory = incomingMsgDirFc.getSelectedFile().getAbsolutePath().replace("\\", "//");
		            	incomingMsgDir.setText(incomingMsgDirFc.getSelectedFile().getAbsolutePath().replace("\\", "//"));
		            }
		   
			}
		});
		incomingMsgDir.setEditable(false);
		incomingMsgDirFc.setEnabled(false);

		final JFileChooser outgoingMsgDirFc = new JFileChooser();
		outgoingMsgDirFc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		outgoingMsgDirFc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		final JTextField outgoingMsgDir = new JTextField(35);
		final JButton outgoingMsgDirPath = ConfigTabHelper.addFileChooserRow(standalonePanel, ConnectorProperties.OTHER_OUTGOING_MSG_DIR_LABEL, 
				ConnectorProperties.outgoingMessagesDirectory, outgoingMsgDir);
		outgoingMsgDirPath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				    int returnVal = outgoingMsgDirFc.showOpenDialog(ConfigOtherTab.this);

		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		            	ConnectorProperties.outgoingMessagesDirectory = outgoingMsgDirFc.getSelectedFile().getAbsolutePath().replace("\\", "//");
		            	outgoingMsgDir.setText(outgoingMsgDirFc.getSelectedFile().getAbsolutePath().replace("\\", "//"));
		            }
		   
			}
		});
		outgoingMsgDir.setEditable(false);
		outgoingMsgDirFc.setEnabled(false);
		
		final JFormattedTextField msgPropertiesFileName = ConfigTabHelper.addTextFieldRow(null, standalonePanel, 
				ConnectorProperties.OTHER_MSG_PROPERTY_FILE_NAME_LABEL, ConnectorProperties.messagePropertiesFileName,10);
		msgPropertiesFileName.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.messagePropertiesFileName = msgPropertiesFileName.getText();
			}
		});
		standalonePanel.add(new JLabel(""));
		
		SpringUtilities.makeCompactGrid(standalonePanel,
                4, 3, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
		standalonePanel.setOpaque(true);
		
		return standalonePanel;
	}

	private JPanel buildFrameworkPanel() {
		JPanel frameworkPanel = new JPanel(new SpringLayout());
		
		JLabel header = new JLabel("Connector as Framework settings:");
		header.setFont(header.getFont().deriveFont(Font.BOLD));
		frameworkPanel.add(header);
		frameworkPanel.add(new JLabel(""));

		final JFormattedTextField nbcImplClassName = ConfigTabHelper.addTextFieldRow(null, frameworkPanel, 
				ConnectorProperties.OTHER_NBC_IMPL_CLASSNAME_LABEL, ConnectorProperties.nationalBackendClientImplementationClassName, 35);
		nbcImplClassName.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.nationalBackendClientImplementationClassName = nbcImplClassName.getText();
			}
		});
		nbcImplClassName.setEditable(true);
		
		final JCheckBox contentMapperEnabled = new JCheckBox(ConnectorProperties.OTHER_CONTENT_MAPPER_ACTIVE_LABEL);
		contentMapperEnabled.setSelected(ConnectorProperties.useContentMapper);

		frameworkPanel.add(new JLabel(""));
		frameworkPanel.add(contentMapperEnabled);

		final JFormattedTextField contentMapperImplClassName = ConfigTabHelper.addTextFieldRow(null, frameworkPanel, 
				ConnectorProperties.OTHER_CONTENT_MAPPER_IMPL_CLASSNAME_LABEL, ConnectorProperties.contentMapperImplementaitonClassName, 35);
		contentMapperImplClassName.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.contentMapperImplementaitonClassName = contentMapperImplClassName.getText();
			}
		});
		contentMapperImplClassName.setEditable(ConnectorProperties.useContentMapper);
		
		contentMapperEnabled.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				ConnectorProperties.useContentMapper = contentMapperEnabled.isSelected();
				contentMapperImplClassName.setEditable(contentMapperEnabled.isSelected());
			}
		});
		
		final JCheckBox clustered = new JCheckBox(ConnectorProperties.OTHER_CLUSTERED_ACTIVE_LABEL);
		clustered.setSelected(ConnectorProperties.clustered);
		clustered.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				ConnectorProperties.clustered = clustered.isSelected();
			}
		});
		frameworkPanel.add(new JLabel(""));
		frameworkPanel.add(clustered);
		
		
		frameworkPanel.add(new JLabel(ConnectorProperties.OTHER_MONITORING_TYPE_LABEL, JLabel.TRAILING));
		final JComboBox<String> monitoringType = new JComboBox<String>(new String[]{"DB","JMX", "REST"});
		if(ConnectorProperties.monitoringType!=null){
		switch(ConnectorProperties.monitoringType){
		case "DB":monitoringType.setSelectedIndex(0);break;
		case "JMX":monitoringType.setSelectedIndex(1);break;
		case "REST":monitoringType.setSelectedIndex(2);break;
		default:monitoringType.setSelectedIndex(0);ConnectorProperties.monitoringType = "DB";
		}
		}else{
			monitoringType.setSelectedIndex(0);
			ConnectorProperties.monitoringType = "DB";
		}
		monitoringType.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("rawtypes")
				JComboBox test = (JComboBox) e.getSource();
				ConnectorProperties.monitoringType = (String) test.getSelectedItem();
			}
		});
		frameworkPanel.add(monitoringType);
		monitoringType.setEditable(true);
		
		SpringUtilities.makeCompactGrid(frameworkPanel,
                6, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
		frameworkPanel.setOpaque(true);
        
		return frameworkPanel;
	}
}
