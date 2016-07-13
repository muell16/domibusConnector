package eu.domibus.connector.gui.main.tab;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.config.tabs.ConfigTabHelper;
import eu.domibus.connector.gui.main.data.Message;
import eu.domibus.connector.gui.main.details.NewMessageDetail;
import eu.domibus.connector.runnable.util.DomibusConnectorMessageProperties;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableConstants;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableUtil;

public class SendNewMessageTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9030411701417070566L;
	private File messageFolder;
	private String nationalMessageId;
	
	public SendNewMessageTab(){
//		JPanel helpPanel = ConfigTabHelper.buildHelpPanel("Send new message Help", "DatabaseConfigurationHelp.htm");
//		BorderLayout mgr = new BorderLayout();
//		setLayout(mgr);
//		add(helpPanel, BorderLayout.EAST);

		
		JPanel disp = new JPanel();
		
		if(messageFolder==null || !messageFolder.exists() || !messageFolder.isDirectory()){
			JPanel createMessageFolderPanel = buildCreateMessageFolderPanel();
			createMessageFolderPanel.setVisible(true);
			disp.add(createMessageFolderPanel);
		}else{
			
		}
		
		
		add(disp);
	}
	
	private JPanel buildCreateMessageFolderPanel(){
		JPanel textPanel = createEmptyPanel();

		JLabel importLabel = new JLabel(
				"To be able to send a new message the connector first needs to create a message folder.");

		importLabel.setVisible(true);

		textPanel.add(importLabel);
		
		JButton createMessageFolderButton = new JButton();
		createMessageFolderButton.setText("Create message folder");
		createMessageFolderButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				nationalMessageId = DomibusConnectorRunnableUtil.generateNationalMessageId(ConnectorProperties.gatewayNameValue, new Date());
				messageFolder = new File(ConnectorProperties.outgoingMessagesDirectory + File.separator + nationalMessageId + DomibusConnectorRunnableConstants.MESSAGE_NEW_FOLDER_POSTFIX);
				if(!messageFolder.mkdir()){
					JOptionPane.showMessageDialog(SendNewMessageTab.this, "The Folder "+messageFolder.getAbsolutePath()+" could not be created!", "Exception", JOptionPane.ERROR_MESSAGE);
				}
				Message newMessage = new Message();
				DomibusConnectorMessageProperties messageProperties = new DomibusConnectorMessageProperties();
				newMessage.setMessageProperties(messageProperties);
				newMessage.getMessageProperties().setNationalMessageId(nationalMessageId);
				newMessage.setMessageDir(messageFolder);
				newMessage.getMessageProperties().setFromPartyId(ConnectorProperties.gatewayNameValue);
				newMessage.getMessageProperties().setFromPartyRole(ConnectorProperties.gatewayRoleValue);
				File messagePropertiesFile = new File(messageFolder, ConnectorProperties.messagePropertiesFileName);
				DomibusConnectorRunnableUtil.storeMessagePropertiesToFile(messageProperties, messagePropertiesFile);
				new NewMessageDetail(newMessage, null);
			}
		});
		
		createMessageFolderButton.setVisible(true);
		
		textPanel.add(createMessageFolderButton);
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
