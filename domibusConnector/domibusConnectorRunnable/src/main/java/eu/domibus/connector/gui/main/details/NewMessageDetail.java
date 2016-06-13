package eu.domibus.connector.gui.main.details;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.common.util.StringUtils;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.config.tabs.ConfigTabHelper;
import eu.domibus.connector.gui.layout.SpringUtilities;
import eu.domibus.connector.gui.main.data.Message;
import eu.domibus.connector.gui.main.tab.MessagesTab;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableConstants;
import eu.domibus.connector.runnable.util.DomibusConnectorRunnableUtil;

public class NewMessageDetail extends MessageDetail {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4437571495217520119L;

	public NewMessageDetail(Message newMessage, MessagesTab list) {
		super(newMessage, list, 0);
		
	}


	@Override
	boolean detailsEditable() {
		return true;
	}



	@Override
	int additionalDetailRows() {
		return 0;
	}



	@Override
	JButton buildActionButton() {
		JButton actionButton = new JButton();
		actionButton.setText("send message");
		actionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String missing = validateMessage();
				
				if(missing == null){
					DomibusConnectorRunnableUtil.storeMessagePropertiesToFile(message.getMessageProperties(), messagePropertiesFile);
					sendMessage();
				}else{
					JOptionPane.showMessageDialog(actionPanel, missing, "Message incomplete.", JOptionPane.ERROR_MESSAGE);
				}
			}

		});
		return actionButton;
	}


	private void sendMessage(){
		File newMessageDir = new File(ConnectorProperties.outgoingMessagesDirectory + File.separator + message.getMessageProperties().getNationalMessageId() + DomibusConnectorRunnableConstants.MESSAGE_READY_FOLDER_POSTFIX);
		try {
			FileUtils.moveDirectory(message.getMessageDir(), newMessageDir);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(actionPanel, "Could not rename message folder.", "Send Message failed.", JOptionPane.ERROR_MESSAGE);
		}
		JOptionPane.showMessageDialog(actionPanel, "Message successfully set ready for the domibusConnector to send.", "Send Message success.", JOptionPane.INFORMATION_MESSAGE);
		messageDetailFrame.setVisible(false);
		messageDetailFrame.dispose();
		if(parent!=null)
	        parent.refresh();
	}
	
	private String validateMessage() {
		boolean messageComplete = true;
		String missingFields = "The following fields must be filled to successfully send the message: \n";
		if(StringUtils.isEmpty(message.getMessageProperties().getAction())){
			messageComplete = false;
			missingFields += "   -- Action\n";
		}
		if(StringUtils.isEmpty(message.getMessageProperties().getContentPdfFileName())){
			messageComplete = false;
			missingFields += "   -- Form PDF File\n";
		}
		if(StringUtils.isEmpty(message.getMessageProperties().getContentXmlFileName())){
			messageComplete = false;
			missingFields += "   -- Form XML File\n";
		}
		if(StringUtils.isEmpty(message.getMessageProperties().getFinalRecipient())){
			messageComplete = false;
			missingFields += "   -- Final Recipient\n";
		}
		if(StringUtils.isEmpty(message.getMessageProperties().getFromPartyId())){
			messageComplete = false;
			missingFields += "   -- From Party ID\n";
		}
		if(StringUtils.isEmpty(message.getMessageProperties().getFromPartyRole())){
			messageComplete = false;
			missingFields += "   -- From Party Role\n";
		}
		if(StringUtils.isEmpty(message.getMessageProperties().getOriginalSender())){
			messageComplete = false;
			missingFields += "   -- Original Sender\n";
		}
		if(StringUtils.isEmpty(message.getMessageProperties().getService())){
			messageComplete = false;
			missingFields += "   -- Service\n";
		}
		if(StringUtils.isEmpty(message.getMessageProperties().getToPartyId())){
			messageComplete = false;
			missingFields += "   -- To Party ID\n";
		}
		if(StringUtils.isEmpty(message.getMessageProperties().getToPartyRole())){
			messageComplete = false;
			missingFields += "   -- To Party Role\n";
		}
		
		if(!messageComplete){
			return missingFields;
		}
		
		return null;
	}

	@Override
	void buildFilesPanel() {
		filesPanel = new JPanel(new SpringLayout());

		final JFileChooser fc1 = new JFileChooser();
		final JTextField log1 = new JTextField(40);

		final JButton formXMLFilePath = ConfigTabHelper.addFileChooserRow(filesPanel, "Form XML File",
				message.getFormXMLFile() != null && message.getFormXMLFile().exists()
						? message.getFormXMLFile().getName() : "",
				log1);
		formXMLFilePath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FileFilter filter = new FileNameExtensionFilter("XML File", "xml");
				fc1.setFileFilter(filter);
				int returnVal = fc1.showOpenDialog(NewMessageDetail.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						FileUtils.copyFileToDirectory(fc1.getSelectedFile(), message.getMessageDir());
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(filesPanel, "Could not copy selected file to message directory!", "File copy error.", JOptionPane.ERROR_MESSAGE);
					}
					message.getMessageProperties().setContentXmlFileName(fc1.getSelectedFile().getName());
					message.setFormXMLFile(fc1.getSelectedFile());
					DomibusConnectorRunnableUtil.storeMessagePropertiesToFile(message.getMessageProperties(), messagePropertiesFile);
					log1.setText(fc1.getSelectedFile().getName());
				}

			}
		});
		filesPanel.add(new JLabel(""));

		final JFileChooser fc2 = new JFileChooser();
		final JTextField log2 = new JTextField(40);

		final JButton formPDFFilePath = ConfigTabHelper.addFileChooserRow(filesPanel, "Form PDF File",
				message.getFormPDFFile() != null && message.getFormPDFFile().exists()
						? message.getFormPDFFile().getName() : "",
				log2);
		formPDFFilePath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FileFilter filter = new FileNameExtensionFilter("PDF File", "pdf");
				fc2.setFileFilter(filter);
				int returnVal = fc2.showOpenDialog(NewMessageDetail.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						FileUtils.copyFileToDirectory(fc2.getSelectedFile(), message.getMessageDir());
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(filesPanel, "Could not copy selected file to message directory!", "File copy error.", JOptionPane.ERROR_MESSAGE);
					}
					message.getMessageProperties().setContentPdfFileName(fc2.getSelectedFile().getName());
					message.setFormPDFFile(fc2.getSelectedFile());
					DomibusConnectorRunnableUtil.storeMessagePropertiesToFile(message.getMessageProperties(), messagePropertiesFile);
					log2.setText(fc2.getSelectedFile().getName());
				}

			}
		});
		filesPanel.add(new JLabel(""));

		int count = fillAttachments(filesPanel);

		final JFileChooser fc3 = new JFileChooser();
		final JTextField log3 = new JTextField(40);

		final JButton attachmentPath = ConfigTabHelper.addFileChooserRow(filesPanel, "Attachment " + count, "", log3);
		attachmentPath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc3.showOpenDialog(NewMessageDetail.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						FileUtils.copyFileToDirectory(fc3.getSelectedFile(), message.getMessageDir());
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(filesPanel, "Could not copy selected file to message directory!", "File copy error.", JOptionPane.ERROR_MESSAGE);
					}
					message.getAttachments().add(fc3.getSelectedFile());
					log3.setText(fc3.getSelectedFile().getName());
				}

			}
		});

		JButton addRowButton = new JButton("+");

		filesPanel.add(addRowButton);
		addRowButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				messageDetailFrame.setVisible(false);
				messageDetailFrame.dispose();
				new NewMessageDetail(message, parent);
			}
		});

		SpringUtilities.makeCompactGrid(filesPanel, (2 + count), 4, // rows,
																	// cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad

		filesPanel.setOpaque(true);
	}

	private int fillAttachments(JPanel filesPanel) {
		int count = 1;
		for (final File attachment : message.getAttachments()) {
			final JTextField log = new JTextField(40);

			JLabel label = new JLabel("Attachment " + count, JLabel.TRAILING);
			filesPanel.add(label);

			JButton deleteRowButton = new JButton("-");

			log.setText(attachment.getName());
			log.setEditable(false);

			filesPanel.add(log);
			filesPanel.add(deleteRowButton);
			deleteRowButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					message.getAttachments().remove(attachment);
					attachment.delete();
					messageDetailFrame.setVisible(false);
					messageDetailFrame.dispose();
					new NewMessageDetail(message, parent);
				}
			});
			filesPanel.add(new JLabel(""));
			count++;
		}
		return count;
	}

	
}
