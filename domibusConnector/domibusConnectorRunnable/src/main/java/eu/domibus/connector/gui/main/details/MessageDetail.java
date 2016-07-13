package eu.domibus.connector.gui.main.details;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.config.tabs.ConfigTabHelper;
import eu.domibus.connector.gui.layout.SpringUtilities;
import eu.domibus.connector.gui.main.data.Message;
import eu.domibus.connector.gui.main.tab.MessagesTab;

public abstract class MessageDetail extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8325814604685434797L;
	JFrame messageDetailFrame;
	MessagesTab parent;
	Message message;
	int messageType;
	File messagePropertiesFile;
	
	JPanel detailsPanel;
	JPanel filesPanel;
	JPanel actionPanel;
	
	public MessageDetail(final Message message, MessagesTab list, int messageType){
		this.message=message;
		this.parent = list;
		this.messageType = messageType;
		messagePropertiesFile = new File(message.getMessageDir(), ConnectorProperties.messagePropertiesFileName);

		messageDetailFrame = new JFrame("Message "+message.getMessageProperties().getNationalMessageId());
		messageDetailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		messageDetailFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	if(parent!=null)
		        parent.refresh();
		    }
		});
		
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		buildDetailsPanel();
		buildFilesPanel();
		
		buildActionPanel();

		c.gridx = 0;
		c.gridy = 0;
		gridPanel.add(detailsPanel, c);
		c.gridx = 0;
		c.gridy = 1;
		gridPanel.add(filesPanel, c);
		c.fill=GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 3;
		gridPanel.add(actionPanel, c);
		
		add(gridPanel);
		
		messageDetailFrame.setContentPane(this);
		messageDetailFrame.setSize(new Dimension(500, 800));
		messageDetailFrame.pack();
		messageDetailFrame.setLocationRelativeTo(null);
		messageDetailFrame.setVisible(true);
	}
	
	void buildDetailsPanel() {
		detailsPanel = new JPanel(new SpringLayout());

		final JFormattedTextField nationalMessageId = ConfigTabHelper.addTextFieldRow(null, detailsPanel,
				"National Message ID", message.getMessageProperties().getNationalMessageId(), 40);
		nationalMessageId.setEditable(detailsEditable());
		nationalMessageId.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				message.getMessageProperties().setNationalMessageId(nationalMessageId.getText());
			}
		});
		detailsPanel.add(new JLabel(""));
		
		final JFormattedTextField ebmsId = ConfigTabHelper.addTextFieldRow(null, detailsPanel,
				"EBMS Message ID", message.getMessageProperties().getEbmsMessageId(), 40);
		ebmsId.setEditable(false);
		ebmsId.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				message.getMessageProperties().setEbmsMessageId(ebmsId.getText());
			}
		});
		detailsPanel.add(new JLabel(""));
		
		final JFormattedTextField conversationId = ConfigTabHelper.addTextFieldRow(null, detailsPanel,
				"Conversation ID", message.getMessageProperties().getConversationId(), 40);
		conversationId.setEditable(detailsEditable());
		conversationId.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				message.getMessageProperties().setConversationId(conversationId.getText());
			}
		});
		detailsPanel.add(new JLabel(""));

		addEmptyRowToDetails();

		final JFormattedTextField fromPartyId = ConfigTabHelper.addTextFieldRow(null, detailsPanel,
				"From Party ID", message.getMessageProperties().getFromPartyId(), 10);
		fromPartyId.setEditable(detailsEditable());
		fromPartyId.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				message.getMessageProperties().setFromPartyId(fromPartyId.getText());
			}
		});
		detailsPanel.add(new JLabel(""));

		final JFormattedTextField fromPartyRole = ConfigTabHelper.addTextFieldRow(null, detailsPanel,
				"From Party Role", message.getMessageProperties().getFromPartyRole(), 10);
		fromPartyRole.setEditable(detailsEditable());
		fromPartyRole.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				message.getMessageProperties().setFromPartyRole(fromPartyRole.getText());
			}
		});
		detailsPanel.add(new JLabel(""));

		final JFormattedTextField originalSender = ConfigTabHelper.addTextFieldRow(null, detailsPanel,
				"Original Sender", message.getMessageProperties().getOriginalSender(), 40);
		originalSender.setEditable(detailsEditable());
		originalSender.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				message.getMessageProperties().setOriginalSender(originalSender.getText());
			}
		});
		detailsPanel.add(new JLabel(""));

		addEmptyRowToDetails();

		final JFormattedTextField toPartyId = ConfigTabHelper.addTextFieldRow(null, detailsPanel, "To Party ID",
				message.getMessageProperties().getToPartyId(), 10);
		toPartyId.setEditable(detailsEditable());
		toPartyId.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				message.getMessageProperties().setToPartyId(toPartyId.getText());
			}
		});
		detailsPanel.add(new JLabel(""));

		final JFormattedTextField toPartyRole = ConfigTabHelper.addTextFieldRow(null, detailsPanel,
				"To Party Role", message.getMessageProperties().getToPartyRole(), 10);
		toPartyRole.setEditable(detailsEditable());
		toPartyRole.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				message.getMessageProperties().setToPartyRole(toPartyRole.getText());
			}
		});
		detailsPanel.add(new JLabel(""));

		final JFormattedTextField finalRecipient = ConfigTabHelper.addTextFieldRow(null, detailsPanel,
				"Final Recipient", message.getMessageProperties().getFinalRecipient(), 40);
		finalRecipient.setEditable(detailsEditable());
		finalRecipient.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				message.getMessageProperties().setFinalRecipient(finalRecipient.getText());
			}
		});
		detailsPanel.add(new JLabel(""));

		addEmptyRowToDetails();
		
		final JFormattedTextField service = ConfigTabHelper.addTextFieldRow(null, detailsPanel,
				"Service", message.getMessageProperties().getService(), 40);
		service.setEditable(detailsEditable());
		service.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				message.getMessageProperties().setService(service.getText());
			}
		});
		detailsPanel.add(new JLabel(""));

		final JFormattedTextField action = ConfigTabHelper.addTextFieldRow(null, detailsPanel,
				"Action", message.getMessageProperties().getAction(), 40);
		action.setEditable(detailsEditable());
		action.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				message.getMessageProperties().setAction(action.getText());
			}
		});
		detailsPanel.add(new JLabel(""));
		
		addEmptyRowToDetails();
		
		int additionalRows = additionalDetailRows();
		
		addEmptyRowToDetails();
		
		final JFormattedTextField msgDir = ConfigTabHelper.addTextFieldRow(null, detailsPanel,
				"Message directory", message.getMessageDir().getAbsolutePath().replace('\\', '/'), 40);
		msgDir.setEditable(false);
		
		JButton openButton = new JButton(UIManager.getIcon("FileView.directoryIcon"));
		openButton.setToolTipText("Open directory");
		openButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(message.getMessageDir());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(MessageDetail.this, e1.getMessage(), "Open directory failed!", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		detailsPanel.add(openButton);
		
		SpringUtilities.makeCompactGrid(detailsPanel, 
				(17+additionalRows), 3, // rows, // cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad

		detailsPanel.setOpaque(true);
	}
	
	abstract void buildFilesPanel();

	private void addEmptyRowToDetails() {
		detailsPanel.add(new JLabel(""));
		detailsPanel.add(new JLabel(""));
		detailsPanel.add(new JLabel(""));
	}
	
	void buildActionPanel(){
		actionPanel = new JPanel();
		
		JButton actionButton = buildActionButton();
				
		actionPanel.add(actionButton);
	}
	
	

	abstract boolean detailsEditable();
	
	abstract int additionalDetailRows();
	
	abstract JButton buildActionButton();
	
}
