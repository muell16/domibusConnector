package eu.domibus.connector.gui.config.tabs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.layout.SpringUtilities;

public class ConfigSecurityTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2137980240416785554L;

	public ConfigSecurityTab(){
		JPanel helpPanel = ConfigTabHelper.buildHelpPanel("Security Configuration Help", "SecurityConfigurationHelp.htm");
		BorderLayout mgr = new BorderLayout();
		setLayout(mgr);
		add(helpPanel, BorderLayout.EAST);
		
		JPanel disp = new JPanel();
		
		JPanel springPanel = buildSecurityPanel();
		
		disp.add(springPanel);
		
		add(disp);
		
	}

	private JPanel buildSecurityPanel() {
		JPanel springPanel = new JPanel(new SpringLayout());
		
		final JCheckBox securityToolkitEnabled = new JCheckBox(ConnectorProperties.SECURITY_ACTIVE_LABEL);
		securityToolkitEnabled.setSelected(ConnectorProperties.useSecurityToolkit);
		
		springPanel.add(new JLabel(""));
		springPanel.add(securityToolkitEnabled);
		
		final JFormattedTextField tokenIssuerCountry = ConfigTabHelper.addTextFieldRow(null, springPanel, ConnectorProperties.SECURITY_TOKEN_ISSUER_COUNTRY_LABEL, ConnectorProperties.tokenIssuerCountry, 35);
		tokenIssuerCountry.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.tokenIssuerCountry = tokenIssuerCountry.getText();
			}
		});
		tokenIssuerCountry.setEditable(ConnectorProperties.useSecurityToolkit);
		
		final JFormattedTextField tokenIssuerServiceProvider = ConfigTabHelper.addTextFieldRow(null, springPanel, ConnectorProperties.SECURITY_TOKEN_ISSUER_PROVIDER_LABEL, ConnectorProperties.tokenIssuerServiceProvider,10);
		tokenIssuerServiceProvider.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.tokenIssuerServiceProvider = tokenIssuerServiceProvider.getText();
			}
		});
		tokenIssuerServiceProvider.setEditable(ConnectorProperties.useSecurityToolkit);
		
		springPanel.add(new JLabel(ConnectorProperties.SECURITY_TOKEN_ISSUER_AES_LABEL, JLabel.TRAILING));
		final JComboBox<String> tokenIssuerAES2 = new JComboBox<String>(new String[]{"AUTHENTICATION_BASED","SIGNATURE_BASED"});
		tokenIssuerAES2.setSelectedIndex(1);
		tokenIssuerAES2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("rawtypes")
				JComboBox test = (JComboBox) e.getSource();
				ConnectorProperties.tokenIssuerAESValue = (String) test.getSelectedItem();
			}
		});
		springPanel.add(tokenIssuerAES2);
		
		final JFormattedTextField implementationClassName = ConfigTabHelper.addTextFieldRow(null, springPanel, ConnectorProperties.SECURITY_IMPL_CLASSNAME_LABEL, ConnectorProperties.securityToolkitImplementationClassName,10);
		implementationClassName.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.securityToolkitImplementationClassName = implementationClassName.getText();
			}
		});
		implementationClassName.setEditable(ConnectorProperties.useSecurityToolkit);
		
		securityToolkitEnabled.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				ConnectorProperties.useSecurityToolkit = securityToolkitEnabled.isSelected();
				tokenIssuerCountry.setEditable(securityToolkitEnabled.isSelected());
				tokenIssuerServiceProvider.setEditable(securityToolkitEnabled.isSelected());
				tokenIssuerAES2.setEditable(securityToolkitEnabled.isSelected());
				implementationClassName.setEditable(securityToolkitEnabled.isSelected());
			}
		});
		
        SpringUtilities.makeCompactGrid(springPanel,
                5, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
        springPanel.setOpaque(true);
		return springPanel;
	}
}
