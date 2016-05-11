package eu.domibus.connector.gui.config.tabs;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.Format;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

import eu.domibus.connector.gui.config.help.ConfigHelp;
import eu.domibus.connector.gui.layout.SpringUtilities;

public class ConfigTabHelper {
	
	static final NumberFormat numberFormat = NumberFormat.getNumberInstance();

	public static JFormattedTextField addTextFieldRow(Format format, JPanel panel, String labelValue, String tfValue, int cols){
		JLabel label = new JLabel(labelValue, JLabel.TRAILING);
		panel.add(label);
        JFormattedTextField value = null;
        if(format!=null)
        	value= new JFormattedTextField(format);
        else
        	value= new JFormattedTextField();
        value.setText(tfValue);
        value.setColumns(cols);
        label.setLabelFor(value);
        panel.add(value);
        
        return value;
	}
	
	public static JPanel buildHelpPanel(final String helpWindowName, final String helpPath){
		JPanel helpPanel = new JPanel(new SpringLayout());
		
		JLabel helpLabel = new JLabel(UIManager.getIcon("OptionPane.questionIcon"),JLabel.RIGHT);
		helpPanel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				ConfigHelp help = new ConfigHelp(helpWindowName, helpPath);
				help.setVisible(true);
			}
		});
		helpPanel.add(helpLabel, BorderLayout.EAST);
		
		SpringUtilities.makeCompactGrid(helpPanel,
                1, 1, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
		helpPanel.setOpaque(true);
        
		return helpPanel;
	}
	
	
	public static JButton addFileChooserRow(JPanel panel, String labelValue, String tfValue, JTextField log){
		
		JLabel label = new JLabel(labelValue, JLabel.TRAILING);
		panel.add(label);
		
		JButton openButton = new JButton(UIManager.getIcon("FileView.directoryIcon"));
		
		log.setText(tfValue);
		log.setToolTipText(tfValue);
		log.setEditable(false);
		
		panel.add(log);
		panel.add(openButton);
		
		return openButton;
	}
}
