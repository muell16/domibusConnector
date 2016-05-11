package eu.domibus.connector.gui.main.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.config.tabs.ConfigTabHelper;
import eu.domibus.connector.gui.main.details.MessageDetail;
import eu.domibus.connector.gui.main.details.SendNewMessageDetail;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableConstants;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableUtil;

public class SendNewMessageTab extends JPanel {

	private File messageFolder;
	private String nationalMessageId;
	JFrame messageDetailFrame;
	
	public SendNewMessageTab(){
		JPanel helpPanel = ConfigTabHelper.buildHelpPanel("Send new message Help", "DatabaseConfigurationHelp.htm");
		BorderLayout mgr = new BorderLayout();
		setLayout(mgr);
		add(helpPanel, BorderLayout.EAST);

		
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
				messageDetailFrame = new JFrame("Send New Message "+nationalMessageId);
				messageDetailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				messageDetailFrame.setContentPane(new SendNewMessageDetail());
				messageDetailFrame.setSize(new Dimension(500, 800));
				messageDetailFrame.pack();
				messageDetailFrame.setVisible(true);
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
