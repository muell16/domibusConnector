package eu.domibus.connector.gui.main.details;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.FileUtils;

import eu.domibus.connector.gui.config.tabs.ConfigTabHelper;
import eu.domibus.connector.gui.main.data.Message;

public class ExportMessagesDetail extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 358992022287987529L;
	
	JFrame exportFrame;
	File exportFolder = null;
	boolean exportAsZip = false;

	static final int BUFFER = 2048;

	public ExportMessagesDetail(final JTable messagesResultTable){
		exportFrame = new JFrame("Export Messages");
		exportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		exportFrame.setSize(new Dimension(500, 800));
		
		final JPanel exportPanel = new JPanel();

		exportPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		JPanel fileChooserPanel = new JPanel();
		final JFileChooser exportDirFc = new JFileChooser();
		exportDirFc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		final JTextField exportDir = new JTextField(35);
		final JButton exportDirPath = ConfigTabHelper.addFileChooserRow(fileChooserPanel, "Please select export folder:", 
				null, exportDir);
		exportDirPath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = exportDirFc.showOpenDialog(exportPanel);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					exportDir.setText(exportDirFc.getSelectedFile().getAbsolutePath().replace('\\', '/'));
					exportFolder = exportDirFc.getSelectedFile();
				}

			}
		});
		exportDir.setEditable(false);
		exportDirFc.setEnabled(false);

		c.gridx = 0;
		c.gridy = 0;
		exportPanel.add(fileChooserPanel, c);

		final JCheckBox exportAsZipBox = new JCheckBox("Export Message Directories as ZIP archive");
		exportAsZipBox.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				exportAsZip = exportAsZipBox.isSelected();
			}
		});

		c.gridx = 0;
		c.gridy = 1;
		exportPanel.add(exportAsZipBox, c);

		JButton exportButton = new JButton("Export");
		exportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				export(messagesResultTable);
				exportFrame.dispose();
				exportFrame.setVisible(false);

			}
		});

		c.fill=GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 3;
		exportPanel.add(exportButton, c);

		this.add(exportPanel);
		this.setVisible(true);
		
		exportFrame.setContentPane(this);
		exportFrame.pack();
		exportFrame.setLocationRelativeTo(null);
		exportFrame.setVisible(true);
	}

	private void export(JTable messagesResultTable){
		for(int msgIndex:messagesResultTable.getSelectedRows()){
			Message selected = (Message) ((DefaultTableModel)messagesResultTable.getModel()).getValueAt(msgIndex, 10);
			File msgDir = selected.getMessageDir();
			try {
				if(exportAsZip){
					createZip(msgDir);
				}else{
					File msgFolder = new File(exportFolder.getAbsolutePath()+File.separator+msgDir.getName());
					msgFolder.mkdir();
					FileUtils.copyDirectory(msgDir, msgFolder);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Message Folder "+msgDir.getAbsolutePath()+ " could not be exported.\n "+e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void createZip(File msgDir) throws IOException {
		String archiveName = exportFolder.getAbsolutePath()+File.separator+msgDir.getName()+".zip";
		BufferedInputStream origin = null;
		FileOutputStream dest = new 
				FileOutputStream(archiveName);
		ZipOutputStream out = new ZipOutputStream(new 
				BufferedOutputStream(dest));
		//out.setMethod(ZipOutputStream.DEFLATED);
		byte data[] = new byte[BUFFER];
		// get a list of files from current directory
		File[] files = msgDir.listFiles();

		for (int i=0; i<files.length; i++) {
			FileInputStream fi = new 
					FileInputStream(files[i]);
			origin = new 
					BufferedInputStream(fi, BUFFER);
			ZipEntry entry = new ZipEntry(files[i].getName());
			out.putNextEntry(entry);
			int count;
			while((count = origin.read(data, 0, 
					BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();
		}
		out.close();

	}
}
