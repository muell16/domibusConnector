package eu.domibus.connector.gui.config;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;

import eu.domibus.connector.gui.config.listener.ConfigSaveCloseListener;

public class ConfigButtonBar extends JPanel implements ActionListener  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7016040529383497974L;

	public ConfigButtonBar(){
		
		FlowLayout panelLayout = new FlowLayout();
		setLayout(panelLayout);
        panelLayout.setAlignment(FlowLayout.RIGHT);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(panelLayout);
		
        buttonPanel.add(createButton("save", ConfigSaveCloseListener.SAVE, "save"));
        buttonPanel.add(createButton("saveExit", ConfigSaveCloseListener.SAVEEXIT, "saveExit"));
        buttonPanel.add(createButton("exit", ConfigSaveCloseListener.EXIT, "exit"));
	    
        add(buttonPanel, BorderLayout.PAGE_END);
		setVisible(true);
		
    }
	
	private JButton createButton(String actionCommand, String text, String name){
		JButton button = new JButton();
	    button.setText(text);
	    button.setName(name);
	    button.addActionListener(this);
	    
	    button.setUI((ButtonUI) UIManager.getUI(button));
	    button.updateUI();
	    button.setVisible(true);
	    
	    
	    return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ConfigSaveCloseListener.actionPerformed(e, this);
	}
}
