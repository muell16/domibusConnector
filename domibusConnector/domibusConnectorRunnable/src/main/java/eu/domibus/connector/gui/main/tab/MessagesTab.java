package eu.domibus.connector.gui.main.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.io.FileUtils;

import eu.domibus.connector.common.enums.EvidenceType;
import eu.domibus.connector.gui.main.data.Message;
import eu.domibus.connector.gui.main.details.ExportMessagesDetail;
import eu.domibus.connector.gui.main.details.MessageDetail;
import eu.domibus.connector.gui.utils.ButtonColumn;

public abstract class MessagesTab extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5376067458274748172L;
	private static final String AC_DELETE = "delete";
	private static final String AC_EXPORT = "export";
	private static final String AC_REFRESH = "refresh";
	JScrollPane messages;
	JTable messagesResultTable;
	List<Message> messagesList;
	JFrame messageDetailFrame;
	JFrame exportFrame;
	File exportFolder = null;
	boolean exportAsZip = false;
	
	public MessagesTab(){
		buildAndAddButtonPanel();
		
		buildAndAddMessageTable();
		this.setVisible(true);
	}

	private void buildAndAddButtonPanel() {
		JPanel buttonPanel = new JPanel();
		
		JButton refreshButton = new JButton(AC_REFRESH);
		refreshButton.setActionCommand(AC_REFRESH);
		refreshButton.addActionListener(this);
		buttonPanel.add(refreshButton);
		
		JButton exportButton = new JButton("export selected");
		exportButton.setActionCommand(AC_EXPORT);
		exportButton.addActionListener(this);
		buttonPanel.add(exportButton);
		
		JButton deleteButton = new JButton("delete selected");
		deleteButton.setActionCommand(AC_DELETE);
		deleteButton.addActionListener(this);
		buttonPanel.add(deleteButton);
		
		this.add(buttonPanel, BorderLayout.NORTH);
	}
	
	private void refresh(){
		loadMessagesAndFillList();
		messages.invalidate();
		messages.repaint();
	}

	private void buildAndAddMessageTable() {
		messages = new JScrollPane();
		
		messages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		messages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
		messages.setPreferredSize(new java.awt.Dimension(975, 500));
		messages.setOpaque(true);
		messagesList = new ArrayList<Message>();
		loadMessagesAndFillList();
		
		this.add(messages, BorderLayout.CENTER);
	}

	private void loadMessagesAndFillList() {
		try {
			messagesList = loadMessages();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
		}
		fillList();
	}
	
	public abstract List<Message> loadMessages() throws Exception;
	
	public abstract String getTableHeader5();
	
	public abstract String getMessageStatus(Message msg);
	
	public String getLastEvidence(Message msg){
		EvidenceType lastEvidenceType = null;
		File[] contents = msg.getMessageDir().listFiles();

		if(contents!=null && contents.length>0){
			for(final File subFile:contents){
				if(subFile.exists()){
					try{
						String name = subFile.getName().substring(0, subFile.getName().indexOf("."));
					EvidenceType evidence = EvidenceType.valueOf(EvidenceType.class, name);
					if(evidence!=null){
						if(lastEvidenceType==null || lastEvidenceType.getPriority()<evidence.getPriority())
							lastEvidenceType=evidence;
					}
					}catch(IllegalArgumentException iae){
					}
				}
			}
		}
		 return lastEvidenceType!=null?lastEvidenceType.name():"unknown";
	}
	
	public void fillList(){
    	Object[][] disp = new Object[messagesList.size()][1];
		for(int i = 0;i<messagesList.size();++i) {
			Message msg = messagesList.get(i);
			Object[] oArr = new Object[11];
			oArr[0] = msg.getFromPartyId();
			oArr[1] = msg.getToPartyId();
			oArr[2] = msg.getOriginalSender();
			oArr[3] = msg.getFinalRecipient();
			oArr[4] = msg.getService();
			oArr[5] = msg.getAction();
			oArr[6] = msg.getReceivedTimestamp();
			oArr[7] = getLastEvidence(msg);
			oArr[8] = getMessageStatus(msg);
			oArr[9] = "Details";
			oArr[10] = msg;
			disp[i] = oArr;
		}
		TableModel jTable1Model = 
				new DefaultTableModel(
						disp,
						new Object[] { "From", "To", "OriginalSender", "FinalRecipient", "Service", "Action", getTableHeader5(), "Last evidence", "Status", "Details", "Data" });
		messagesResultTable = new JTable();
		
		messages.setViewportView(messagesResultTable);
		messagesResultTable.setPreferredScrollableViewportSize(new Dimension(965, 500));
		messagesResultTable.setFillsViewportHeight(true);
		messagesResultTable.setModel(jTable1Model);
		messagesResultTable.removeColumn(messagesResultTable.getColumn("Data"));
		messagesResultTable.getTableHeader().setVisible(true);
		messagesResultTable.setAutoCreateRowSorter(true);
		Action details = new AbstractAction()
		{
		    /**
			 * 
			 */
			private static final long serialVersionUID = 7515485337770703925L;

			public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        Message selected = (Message) ((DefaultTableModel)table.getModel()).getValueAt(modelRow, 10);
		        messageDetailFrame = new JFrame("Message Details");
				messageDetailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				messageDetailFrame.setContentPane(new MessageDetail(selected));
				messageDetailFrame.setSize(new Dimension(500, 800));
				messageDetailFrame.pack();
				messageDetailFrame.setVisible(true);
		    }
		};
		 
		new ButtonColumn(messagesResultTable, details, 9);
        messages.setVisible(messagesList.size()>0);
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(messagesResultTable.getSelectedRows().length<1){
			JOptionPane.showMessageDialog(this, "No rows selected.", "No selection", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		switch(e.getActionCommand()){
		case AC_REFRESH:refresh();System.out.println(AC_REFRESH);break;
		case AC_EXPORT:
		case AC_DELETE:handleSelected(e.getActionCommand());break;
		}
	}
	
	private void handleSelected(String actionCommand){
		
		if(actionCommand.equals(AC_DELETE)){
			if (JOptionPane.showConfirmDialog(this, 
	        		"All selected messages will be deleted and cannot be restored. \n "
	        		+ "An Export for those messages for archiving is adviced. \n"
	        		+ "Do you really want to continue?", "Delete messages", 
	        		JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.CANCEL_OPTION){
	            return;
	        }
		}
		
		
		if(actionCommand.equals(AC_EXPORT)){
			
			exportFrame = new JFrame("Export Messages");
			exportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			exportFrame.setContentPane(new ExportMessagesDetail(messagesResultTable, exportFrame));
			exportFrame.setSize(new Dimension(500, 800));
			exportFrame.pack();
			exportFrame.setVisible(true);
			
		}
		
		if(actionCommand.equals(AC_DELETE)){
			for(int msgIndex:messagesResultTable.getSelectedRows()){
				Message selected = (Message) ((DefaultTableModel)messagesResultTable.getModel()).getValueAt(msgIndex, 10);
				File msgDir = selected.getMessageDir();
				try {
					FileUtils.deleteDirectory(msgDir);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, "Message Folder "+msgDir.getAbsolutePath()+ " could not be deleted.\n "+e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		
		refresh();
	}
}
