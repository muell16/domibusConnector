package eu.domibus.connector.gui.main.details;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;

import eu.domibus.connector.common.enums.EvidenceType;
import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.config.tabs.ConfigTabHelper;
import eu.domibus.connector.gui.layout.SpringUtilities;
import eu.domibus.connector.gui.main.data.Message;
import eu.domibus.connector.gui.main.tab.MessagesTab;
import eu.domibus.connector.runnable.util.DomibusConnectorMessageProperties;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableConstants;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableUtil;

public class StoredMessageDetail extends MessageDetail {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7470963454138533680L;

	public StoredMessageDetail(final Message selected, final int messageType, MessagesTab list){
		super(selected, list, messageType);
		
		
	}
	

	@Override
	void buildFilesPanel() {
		filesPanel = new JPanel(new SpringLayout());

		int rowIndex = 1;
		JLabel header = new JLabel("Files in message directory:");
		filesPanel.add(header);
		filesPanel.add(new JLabel(""));
		filesPanel.add(new JLabel(""));

		File[] contents = message.getMessageDir().listFiles();

		if(contents!=null && contents.length>0){
			for(final File subFile:contents){
				if(subFile.exists()){
					final JLabel label = new JLabel();
					if(subFile.getName().equals(message.getMessageProperties().getContentXmlFileName())){
						label.setText("Form XML File: ");
					}else if (subFile.getName().equals(message.getMessageProperties().getContentPdfFileName())){
						label.setText("Form PDF File: ");
					}else{
						label.setText("Attachment: ");
					}
					filesPanel.add(label);
					final JLabel log = new JLabel(subFile.getName());
					log.setSize(200, 10);
					filesPanel.add(log);

					JButton openButton2 = new JButton(UIManager.getIcon("FileView.directoryIcon"));
					openButton2.setToolTipText("Open file");
					openButton2.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								Desktop.getDesktop().open(subFile);
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(StoredMessageDetail.this, e1.getMessage(), "Open file failed!", JOptionPane.ERROR_MESSAGE);
							}

						}
					});
					filesPanel.add(openButton2);
					rowIndex++;
				}
			}
		}


		SpringUtilities.makeCompactGrid(filesPanel,
				rowIndex, 3, //rows, cols
				6, 6,        //initX, initY
				6, 6);       //xPad, yPad

		filesPanel.setOpaque(true);
	}

	@Override
	JButton buildActionButton() {
		
		JButton actionButton = new JButton();
		if(this.messageType==DomibusConnectorRunnableConstants.MESSAGE_TYPE_INCOMING){
			actionButton.setText("send response message");
			actionButton.setActionCommand("respond");
		}else if (this.messageType==DomibusConnectorRunnableConstants.MESSAGE_TYPE_OUTGOING){
			actionButton.setText("re-send message");
			actionButton.setActionCommand("resend");
		}
		actionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String nationalMessageId = DomibusConnectorRunnableUtil.generateNationalMessageId(ConnectorProperties.gatewayNameValue, new Date());
				File messageFolder = new File(ConnectorProperties.outgoingMessagesDirectory + File.separator + nationalMessageId + DomibusConnectorRunnableConstants.MESSAGE_NEW_FOLDER_POSTFIX);
				if(!messageFolder.mkdir()){
					JOptionPane.showMessageDialog(StoredMessageDetail.this, "The Folder "+messageFolder.getAbsolutePath()+" could not be created!", "Exception", JOptionPane.ERROR_MESSAGE);
				}
				Message newMessage = new Message();
				DomibusConnectorMessageProperties messageProperties = new DomibusConnectorMessageProperties();
				newMessage.setMessageProperties(messageProperties);
				newMessage.getMessageProperties().setNationalMessageId(nationalMessageId);
				newMessage.setMessageDir(messageFolder);
				newMessage.getMessageProperties().setService(message.getMessageProperties().getService());
				if(e.getActionCommand().equals("respond")){
					newMessage.getMessageProperties().setFromPartyId(message.getMessageProperties().getToPartyId());
					newMessage.getMessageProperties().setFromPartyRole(message.getMessageProperties().getToPartyRole());
					newMessage.getMessageProperties().setToPartyId(message.getMessageProperties().getFromPartyId());
					newMessage.getMessageProperties().setToPartyRole(message.getMessageProperties().getFromPartyRole());
					newMessage.getMessageProperties().setFinalRecipient(message.getMessageProperties().getOriginalSender());
					newMessage.getMessageProperties().setOriginalSender(message.getMessageProperties().getFinalRecipient());
					if(message.getFormPDFFile()!=null){
						try {
							FileUtils.copyFileToDirectory(message.getFormPDFFile(), messageFolder);
							newMessage.getAttachments().add(new File(messageFolder, message.getFormPDFFile().getName()));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}else if(e.getActionCommand().equals("resend")){
					newMessage.getMessageProperties().setFromPartyId(message.getMessageProperties().getFromPartyId());
					newMessage.getMessageProperties().setFromPartyRole(message.getMessageProperties().getFromPartyRole());
					newMessage.getMessageProperties().setToPartyId(message.getMessageProperties().getToPartyId());
					newMessage.getMessageProperties().setToPartyRole(message.getMessageProperties().getToPartyRole());
					newMessage.getMessageProperties().setFinalRecipient(message.getMessageProperties().getFinalRecipient());
					newMessage.getMessageProperties().setOriginalSender(message.getMessageProperties().getOriginalSender());
					newMessage.getMessageProperties().setAction(message.getMessageProperties().getAction());
					if(message.getFormPDFFile()!=null){
						try {
							FileUtils.copyFileToDirectory(message.getFormPDFFile(), messageFolder);
							newMessage.getMessageProperties().setContentPdfFileName(message.getFormPDFFile().getName());
							newMessage.setFormPDFFile(new File(messageFolder, message.getFormPDFFile().getName()));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					if(message.getFormXMLFile()!=null){
						try {
							FileUtils.copyFileToDirectory(message.getFormXMLFile(), messageFolder);
							newMessage.getMessageProperties().setContentXmlFileName(message.getFormXMLFile().getName());
							newMessage.setFormXMLFile(new File(messageFolder, message.getFormXMLFile().getName()));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				if(!message.getAttachments().isEmpty()){
					for(File attachment:message.getAttachments()){
						try{
						String name = attachment.getName().substring(0, attachment.getName().indexOf("."));
						EvidenceType evidence = EvidenceType.valueOf(EvidenceType.class, name);
						if(evidence!=null){
						}
						}catch(IllegalArgumentException iae){
							try {
								FileUtils.copyFileToDirectory(attachment, messageFolder);
								newMessage.getAttachments().add(new File(messageFolder, attachment.getName()));
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						
						}
					}
				}
				File messagePropertiesFile = new File(messageFolder, ConnectorProperties.messagePropertiesFileName);
				DomibusConnectorRunnableUtil.storeMessagePropertiesToFile(messageProperties, messagePropertiesFile);
				messageDetailFrame.setVisible(false);
				messageDetailFrame.dispose();
				new NewMessageDetail(newMessage, parent);
		        
			}
		});
		
		return actionButton;
	}
	
	@Override
	boolean detailsEditable() {
		return false;
	}

	@Override
	int additionalDetailRows() {
		if(this.messageType==DomibusConnectorRunnableConstants.MESSAGE_TYPE_INCOMING){
			final JFormattedTextField msgReceivedTime = ConfigTabHelper.addTextFieldRow(null, detailsPanel,
					"Message received time", message.getMessageProperties().getMessageReceivedDatetime(), 40);
			msgReceivedTime.setEditable(detailsEditable());
		}else if (this.messageType==DomibusConnectorRunnableConstants.MESSAGE_TYPE_OUTGOING){
			final JFormattedTextField msgReceivedTime = ConfigTabHelper.addTextFieldRow(null, detailsPanel,
					"Message sent time", message.getMessageProperties().getMessageSentDatetime(), 40);
			msgReceivedTime.setEditable(detailsEditable());
		}
		detailsPanel.add(new JLabel(""));
		
		return 1;
	}

	
}
