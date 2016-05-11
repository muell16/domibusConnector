package eu.domibus.connector.gui.main.details;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

import eu.domibus.connector.gui.layout.SpringUtilities;
import eu.domibus.connector.gui.main.data.Message;

public class MessageDetail extends JPanel {

	Message selected;

	public MessageDetail(final Message selected){
		this.selected=selected;
		
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JPanel detailsPanel = buildDetailsPanel(selected);
		JPanel filesPanel = buildFilesPanel(selected);

		c.gridx = 0;
		c.gridy = 0;
		gridPanel.add(detailsPanel, c);
		c.gridx = 0;
		c.gridy = 1;
		gridPanel.add(filesPanel, c);
//		c.fill=GridBagConstraints.VERTICAL;
//		c.gridx = 0;
//		c.gridy = 3;
//		gridPanel.add(importPanel, c);
		
		add(gridPanel);
		
	}

	private JPanel buildFilesPanel(final Message selected) {
		JPanel filesPanel = new JPanel(new SpringLayout());

		int rowIndex = 1;
		JLabel header = new JLabel("Files in message directory:");
		filesPanel.add(header);
		filesPanel.add(new JLabel(""));

		File[] contents = selected.getMessageDir().listFiles();

		if(contents!=null && contents.length>0){
			for(final File subFile:contents){
				if(subFile.exists()){
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
								JOptionPane.showMessageDialog(MessageDetail.this, e1.getMessage(), "Open file failed!", JOptionPane.ERROR_MESSAGE);
							}

						}
					});
					filesPanel.add(openButton2);
					rowIndex++;
				}
			}
		}


		SpringUtilities.makeCompactGrid(filesPanel,
				rowIndex, 2, //rows, cols
				6, 6,        //initX, initY
				6, 6);       //xPad, yPad

		filesPanel.setOpaque(true);
		return filesPanel;
	}

	private JPanel buildDetailsPanel(final Message selected) {
		JPanel detailsPanel = new JPanel(new SpringLayout());

		createLabelValuePair(detailsPanel, "EBMS-ID:", selected.getEbmsMessageId());
		createLabelValuePair(detailsPanel, "", "");
		createLabelValuePair(detailsPanel, "National Message ID:", selected.getNationalMessageId());
		createLabelValuePair(detailsPanel, "", "");
		createLabelValuePair(detailsPanel, "From Party ID:", selected.getFromPartyId());
		createLabelValuePair(detailsPanel, "From Party Role:", selected.getFromPartyRole());
		createLabelValuePair(detailsPanel, "Original Sender:", selected.getOriginalSender());
		createLabelValuePair(detailsPanel, "", "");
		createLabelValuePair(detailsPanel, "To Party ID:", selected.getToPartyId());
		createLabelValuePair(detailsPanel, "To Party Role:", selected.getToPartyRole());
		createLabelValuePair(detailsPanel, "Final Recipient:", selected.getFinalRecipient());
		createLabelValuePair(detailsPanel, "", "");
		createLabelValuePair(detailsPanel, "Service:", selected.getService());
		createLabelValuePair(detailsPanel, "Action:", selected.getAction());
		createLabelValuePair(detailsPanel, "", "");
		createLabelValuePair(detailsPanel, "Message received time:", selected.getReceivedTimestamp());

		JLabel key = new JLabel("Message directory:");
		detailsPanel.add(key);

		final JLabel log3 = new JLabel(selected.getMessageDir().getAbsolutePath().replace('\\', '/'));
		detailsPanel.add(log3);

		JButton openButton = new JButton(UIManager.getIcon("FileView.directoryIcon"));
		openButton.setToolTipText("Open directory");
		openButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(selected.getMessageDir());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(MessageDetail.this, e1.getMessage(), "Open directory failed!", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		detailsPanel.add(openButton);

		SpringUtilities.makeCompactGrid(detailsPanel,
				17, 3, //rows, cols
				6, 6,        //initX, initY
				6, 6);       //xPad, yPad

		detailsPanel.setOpaque(true);
		return detailsPanel;
	}

	private void createLabelValuePair(JPanel detailsPanel, String label, String value) {
		JLabel key = new JLabel(label);
		detailsPanel.add(key);

		JLabel vLabel = new JLabel(value);
		detailsPanel.add(vLabel);

		detailsPanel.add(new JLabel(""));
	}
}
