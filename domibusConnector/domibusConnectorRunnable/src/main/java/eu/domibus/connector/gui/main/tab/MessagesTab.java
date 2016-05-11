package eu.domibus.connector.gui.main.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import eu.domibus.connector.gui.config.tabs.ConfigImportTab;
import eu.domibus.connector.gui.main.data.Message;
import eu.domibus.connector.gui.main.details.MessageDetail;
import eu.domibus.connector.gui.main.reader.ReceivedMessagesReader;
import eu.domibus.connector.gui.utils.ButtonColumn;
import eu.domibus.connector.gui.utils.ButtonRenderer;
import eu.domibus.connector.gui.utils.ReceivedMessageSelectionListener;

public abstract class MessagesTab extends JPanel {

	JScrollPane messages;
	JTable messagesResultTable;
	List<Message> messagesList;
	JFrame messageDetailFrame;
	
	public MessagesTab(){
		messages = new JScrollPane();
		
		
		messages.setPreferredSize(new java.awt.Dimension(975, 600));
		messagesList = new ArrayList<Message>();
		try {
			messagesList = loadMessages();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
		}
		fillList();
		
		this.add(messages, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	public abstract List<Message> loadMessages() throws Exception;
	
	public abstract String getTableHeader5();
	
	public void fillList(){
    	Object[][] disp = new Object[messagesList.size()][1];
		for(int i = 0;i<messagesList.size();++i) {
			Message msg = messagesList.get(i);
			Object[] oArr = new Object[9];
			oArr[0] = msg.getFromPartyId();
			oArr[1] = msg.getToPartyId();
			oArr[2] = msg.getOriginalSender();
			oArr[3] = msg.getFinalRecipient();
			oArr[4] = msg.getService();
			oArr[5] = msg.getAction();
			oArr[6] = msg.getReceivedTimestamp();
			oArr[7] = "Details";
			oArr[8] = msg;
			disp[i] = oArr;
		}
		TableModel jTable1Model = 
				new DefaultTableModel(
						disp,
						new Object[] { "From", "To", "OriginalSender", "FinalRecipient", "Service", "Action", getTableHeader5(),"Details", "Data" });
		messagesResultTable = new JTable();
		
		messages.setViewportView(messagesResultTable);
		messagesResultTable.setPreferredScrollableViewportSize(new Dimension(1000, 70));
		messagesResultTable.setFillsViewportHeight(true);
		messagesResultTable.setModel(jTable1Model);
		messagesResultTable.removeColumn(messagesResultTable.getColumn("Data"));
		messagesResultTable.getTableHeader().setVisible(true);
		messagesResultTable.setAutoCreateRowSorter(true);
		Action details = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        Message selected = (Message) ((DefaultTableModel)table.getModel()).getValueAt(modelRow, 8);
		        messageDetailFrame = new JFrame("Message Details");
				messageDetailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				messageDetailFrame.setContentPane(new MessageDetail(selected));
				messageDetailFrame.setSize(new Dimension(500, 800));
				messageDetailFrame.pack();
				messageDetailFrame.setVisible(true);
		    }
		};
		 
		new ButtonColumn(messagesResultTable, details, 7);
        messages.setVisible(messagesList.size()>0);
    }
}
