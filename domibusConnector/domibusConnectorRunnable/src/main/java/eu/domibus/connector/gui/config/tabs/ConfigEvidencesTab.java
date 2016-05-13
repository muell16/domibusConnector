package eu.domibus.connector.gui.config.tabs;

import java.awt.BorderLayout;
import java.awt.Font;
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

public class ConfigEvidencesTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4028262694256604322L;
	private JComboBox<String> hashAlgorithm;
	private JCheckBox evidenceToolkitEnabled;
	private JCheckBox evidenceTimeoutEnabled;
	private JFormattedTextField relayremmdTimeout;
	private JFormattedTextField deliveryTimeout;
	private JFormattedTextField retrievalTimeout;
	private JFormattedTextField postalAddressStreet;
	private JFormattedTextField postalAddressLocality;
	private JFormattedTextField postalAddressPostalCode;
	private JFormattedTextField postalAddressCountry;

	public ConfigEvidencesTab(){
		JPanel helpPanel = ConfigTabHelper.buildHelpPanel("Evidences Configuration Help", "EvidencesConfigurationHelp.htm");
		BorderLayout mgr = new BorderLayout();
		setLayout(mgr);
		add(helpPanel, BorderLayout.EAST);
		
		JPanel disp = new JPanel();
		
		JPanel evidencesPanel = buildEvidencesPanel();

		disp.add(evidencesPanel);
		
		JLabel header = new JLabel("Configuration of address for evidences generating:");
		header.setFont(header.getFont().deriveFont(Font.BOLD));
		disp.add(header);
		
		JPanel addressPanel = buildAddressPanel();
		
		disp.add(addressPanel);
		
		add(disp);
	}

	private JPanel buildAddressPanel() {
		JPanel addressPanel = new JPanel(new SpringLayout());
		
		postalAddressStreet = ConfigTabHelper.addTextFieldRow(null, addressPanel, 
				ConnectorProperties.EVIDENCES_ADDRESS_STREET_LABEL, ConnectorProperties.postalAddressStreet,40);
		postalAddressStreet.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.postalAddressStreet = postalAddressStreet.getText();
			}
		});
		postalAddressStreet.setEditable(ConnectorProperties.useEvidencesToolkit);
		
		postalAddressLocality = ConfigTabHelper.addTextFieldRow(null, addressPanel, 
				ConnectorProperties.EVIDENCES_ADDRESS_LOCALITY_LABEL, ConnectorProperties.postalAddressLocality,5);
		postalAddressLocality.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.postalAddressLocality = postalAddressLocality.getText();
			}
		});
		postalAddressLocality.setEditable(ConnectorProperties.useEvidencesToolkit);
		
		postalAddressPostalCode = ConfigTabHelper.addTextFieldRow(null, addressPanel, 
				ConnectorProperties.EVIDENCES_ADDRESS_POSTAL_CODE_LABEL, ConnectorProperties.postalAddressPostalCode, 5);
		postalAddressPostalCode.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.postalAddressPostalCode = postalAddressPostalCode.getText();
			}
		});
		postalAddressPostalCode.setEditable(ConnectorProperties.useEvidencesToolkit);
		
		postalAddressCountry = ConfigTabHelper.addTextFieldRow(null, addressPanel, 
				ConnectorProperties.EVIDENCES_ADDRESS_COUNTRY_LABEL, ConnectorProperties.postalAddressCountry, 10);
		postalAddressCountry.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.postalAddressCountry = postalAddressCountry.getText();
			}
		});
		postalAddressCountry.setEditable(ConnectorProperties.useEvidencesToolkit);
		
		
        SpringUtilities.makeCompactGrid(addressPanel,
                4, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
        addressPanel.setOpaque(true);
		return addressPanel;
	}

	private JPanel buildEvidencesPanel() {
		JPanel evidencesPanel = new JPanel(new SpringLayout());

		evidenceToolkitEnabled = new JCheckBox(ConnectorProperties.EVIDENCES_TOOLKIT_ACTIVE_LABEL);
		evidenceToolkitEnabled.setSelected(ConnectorProperties.useEvidencesToolkit);
		evidenceToolkitEnabled.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				ConnectorProperties.useEvidencesToolkit = evidenceToolkitEnabled.isSelected();
				hashAlgorithm.setEditable(evidenceToolkitEnabled.isSelected());
				evidenceTimeoutEnabled.setEnabled(evidenceToolkitEnabled.isSelected());
				relayremmdTimeout.setEditable(evidenceToolkitEnabled.isSelected() && ConnectorProperties.useEvidencesTimeout);
				deliveryTimeout.setEditable(evidenceToolkitEnabled.isSelected() && ConnectorProperties.useEvidencesTimeout);
				retrievalTimeout.setEditable(evidenceToolkitEnabled.isSelected() && ConnectorProperties.useEvidencesTimeout);
				postalAddressStreet.setEditable(evidenceToolkitEnabled.isSelected());
				postalAddressLocality.setEditable(evidenceToolkitEnabled.isSelected());
				postalAddressPostalCode.setEditable(evidenceToolkitEnabled.isSelected());
				postalAddressCountry.setEditable(evidenceToolkitEnabled.isSelected());
			}
		});

		evidencesPanel.add(new JLabel(""));
		evidencesPanel.add(evidenceToolkitEnabled);

		evidencesPanel.add(new JLabel(ConnectorProperties.EVIDENCES_HASH_ALGORITHM_LABEL, JLabel.TRAILING));
		hashAlgorithm = new JComboBox<String>(new String[]{"MD5","SHA1", "SHA256"});
		if(ConnectorProperties.hashAlgorithm!=null){
		switch(ConnectorProperties.hashAlgorithm){
		case "MD5":hashAlgorithm.setSelectedIndex(0);break;
		case "SHA1":hashAlgorithm.setSelectedIndex(1);break;
		case "SHA256":hashAlgorithm.setSelectedIndex(2);break;
		default:hashAlgorithm.setSelectedIndex(0);ConnectorProperties.hashAlgorithm = "MD5";
		}
		}else{
			hashAlgorithm.setSelectedIndex(0);
			ConnectorProperties.hashAlgorithm = "MD5";
		}
		hashAlgorithm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("rawtypes")
				JComboBox test = (JComboBox) e.getSource();
				ConnectorProperties.hashAlgorithm = (String) test.getSelectedItem();
			}
		});
		evidencesPanel.add(hashAlgorithm);

		hashAlgorithm.setEditable(ConnectorProperties.useEvidencesToolkit);

		evidenceTimeoutEnabled = new JCheckBox(ConnectorProperties.EVIDENCES_TIMEOUT_ACTIVE_LABEL);
		evidenceTimeoutEnabled.setSelected(ConnectorProperties.useEvidencesTimeout);
		evidenceTimeoutEnabled.setEnabled(ConnectorProperties.useEvidencesToolkit);
		evidenceTimeoutEnabled.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				ConnectorProperties.useEvidencesTimeout = evidenceTimeoutEnabled.isSelected();
				relayremmdTimeout.setEditable(ConnectorProperties.useEvidencesToolkit && evidenceTimeoutEnabled.isSelected());
				deliveryTimeout.setEditable(ConnectorProperties.useEvidencesToolkit && evidenceTimeoutEnabled.isSelected());
				retrievalTimeout.setEditable(ConnectorProperties.useEvidencesToolkit && evidenceTimeoutEnabled.isSelected());
			}
		});
		evidencesPanel.add(new JLabel(""));
		evidencesPanel.add(evidenceTimeoutEnabled);

		relayremmdTimeout = ConfigTabHelper.addTextFieldRow(null, evidencesPanel, 
				ConnectorProperties.EVIDENCES_TIMEOUT_RELAYREMMD_LABEL, ConnectorProperties.evidenceRelayREMMDTimeout, 35);
		relayremmdTimeout.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.evidenceRelayREMMDTimeout = relayremmdTimeout.getText();
			}
		});
		relayremmdTimeout.setEditable(ConnectorProperties.useEvidencesToolkit && ConnectorProperties.useEvidencesTimeout);

		deliveryTimeout = ConfigTabHelper.addTextFieldRow(null, evidencesPanel, 
				ConnectorProperties.EVIDENCES_TIMEOUT_DELIVERY_LABEL, ConnectorProperties.evidenceDeliveryTimeout, 35);
		deliveryTimeout.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.evidenceDeliveryTimeout = deliveryTimeout.getText();
			}
		});
		deliveryTimeout.setEditable(ConnectorProperties.useEvidencesToolkit && ConnectorProperties.useEvidencesTimeout);

		retrievalTimeout = ConfigTabHelper.addTextFieldRow(null, evidencesPanel, 
				ConnectorProperties.EVIDENCES_TIMEOUT_RETRIEVAL_LABEL, ConnectorProperties.evidenceRetrievalTimeout, 35);
		retrievalTimeout.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.evidenceRetrievalTimeout = retrievalTimeout.getText();
			}
		});
		retrievalTimeout.setEditable(ConnectorProperties.useEvidencesToolkit && ConnectorProperties.useEvidencesTimeout);

		SpringUtilities.makeCompactGrid(evidencesPanel,
				6, 2, //rows, cols
				6, 6,        //initX, initY
				6, 6);       //xPad, yPad

		evidencesPanel.setOpaque(true);
		return evidencesPanel;
	}
}
