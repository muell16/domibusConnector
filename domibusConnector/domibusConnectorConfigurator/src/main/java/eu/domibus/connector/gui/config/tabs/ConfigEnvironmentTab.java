package eu.domibus.connector.gui.config.tabs;

import java.awt.BorderLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.layout.SpringUtilities;

public class ConfigEnvironmentTab extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3440660197604197891L;

	public ConfigEnvironmentTab(){
		JPanel helpPanel = ConfigTabHelper.buildHelpPanel("Environment Configuration Help", "EnvironmentConfigurationHelp.htm");
		BorderLayout mgr = new BorderLayout();
		setLayout(mgr);
		add(helpPanel, BorderLayout.EAST);
		
		JPanel disp = new JPanel();
		
		JPanel gwConfigPanel = buildGwConfigPanel();
		
		disp.add(gwConfigPanel);
		
		JPanel generalConfigPanel = buildGeneralConfigPanel();
		
		disp.add(generalConfigPanel);
		
		JPanel proxyConfigPanel = buildProxyConfigPanel();
		
		disp.add(proxyConfigPanel);
		
		JPanel dynamicDiscoveryPanel = buildDynamicDiscoveryPanel();
		
		disp.add(dynamicDiscoveryPanel);
		
		add(disp);
	}

	private JPanel buildDynamicDiscoveryPanel() {
		JPanel dynamicDiscoveryPanel = new JPanel(new SpringLayout());
		
		JLabel header = new JLabel("Dynamic Discovery configuration: ");
		header.setFont(header.getFont().deriveFont(Font.BOLD));
		dynamicDiscoveryPanel.add(header);
		dynamicDiscoveryPanel.add(new JLabel(""));
		
		final JCheckBox dynamicDiscoveryEnabled = new JCheckBox(ConnectorProperties.DYNAMIC_DISCOVERY_ACTIVE_LABEL);
		dynamicDiscoveryEnabled.setSelected(ConnectorProperties.useDynamicDiscovery);
		
		dynamicDiscoveryPanel.add(new JLabel(""));
		dynamicDiscoveryPanel.add(dynamicDiscoveryEnabled);
		
		final JFormattedTextField smlResolver = ConfigTabHelper.addTextFieldRow(null, dynamicDiscoveryPanel, ConnectorProperties.DYNAMIC_DISCOVERY_SML_RESOLVER_LABEL, ConnectorProperties.dynamicDiscoverySMLResolverAddress, 35);
		smlResolver.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.dynamicDiscoverySMLResolverAddress = smlResolver.getText();
			}
		});
		smlResolver.setEditable(ConnectorProperties.useDynamicDiscovery);
		
		final JFormattedTextField community = ConfigTabHelper.addTextFieldRow(null, dynamicDiscoveryPanel, ConnectorProperties.DYNAMIC_DISCOVERY_COMMUNITY_LABEL, ConnectorProperties.dynamicDiscoveryCommunity, 10);
		community.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.dynamicDiscoveryCommunity = community.getText();
			}
		});
		community.setEditable(ConnectorProperties.useDynamicDiscovery);
		
		final JFormattedTextField environment = ConfigTabHelper.addTextFieldRow(null, dynamicDiscoveryPanel, ConnectorProperties.DYNAMIC_DISCOVERY_ENVIRONMENT_LABEL, ConnectorProperties.dynamicDiscoveryEnvironment, 10);
		environment.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.dynamicDiscoveryEnvironment = environment.getText();
			}
		});
		environment.setEditable(ConnectorProperties.useDynamicDiscovery);
		
		final JFormattedTextField normalisation = ConfigTabHelper.addTextFieldRow(null, dynamicDiscoveryPanel, ConnectorProperties.DYNAMIC_DISCOVERY_NORM_ALGORITHM_LABEL, ConnectorProperties.dynamicDiscoveryNormalisationAlgorithm, 10);
		normalisation.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.dynamicDiscoveryNormalisationAlgorithm = normalisation.getText();
			}
		});
		normalisation.setEditable(ConnectorProperties.useDynamicDiscovery);
		
		dynamicDiscoveryEnabled.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				ConnectorProperties.useDynamicDiscovery = dynamicDiscoveryEnabled.isSelected();
				smlResolver.setEditable(dynamicDiscoveryEnabled.isSelected());
				community.setEditable(dynamicDiscoveryEnabled.isSelected());
				environment.setEditable(dynamicDiscoveryEnabled.isSelected());
				normalisation.setEditable(dynamicDiscoveryEnabled.isSelected());
			}
		});
		
        SpringUtilities.makeCompactGrid(dynamicDiscoveryPanel,
                6, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
        dynamicDiscoveryPanel.setOpaque(true);
		return dynamicDiscoveryPanel;
	}

	private JPanel buildProxyConfigPanel() {
		JPanel proxyConfigPanel = new JPanel(new SpringLayout());
		
		JLabel header = new JLabel("Proxy configuration: ");
		header.setFont(header.getFont().deriveFont(Font.BOLD));
		proxyConfigPanel.add(header);
		proxyConfigPanel.add(new JLabel(""));
		
		final JCheckBox proxyEnabled = new JCheckBox(ConnectorProperties.PROXY_ACTIVE_LABEL);
		proxyEnabled.setSelected(ConnectorProperties.proxyEnabled);
		
		proxyConfigPanel.add(new JLabel(""));
		proxyConfigPanel.add(proxyEnabled);
		
		final JFormattedTextField proxyHost = ConfigTabHelper.addTextFieldRow(null, proxyConfigPanel, ConnectorProperties.PROXY_HOST_LABEL, ConnectorProperties.proxyHost, 50);
		proxyHost.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.proxyHost = proxyHost.getText();
			}
		});
		proxyHost.setEditable(ConnectorProperties.proxyEnabled);
		
		final JFormattedTextField proxyPort = ConfigTabHelper.addTextFieldRow(null, proxyConfigPanel, ConnectorProperties.PROXY_PORT_LABEL, ConnectorProperties.proxyPort, 10);
		proxyPort.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.proxyPort = proxyPort.getText();
			}
		});
		proxyPort.setEditable(ConnectorProperties.proxyEnabled);
		
		final JFormattedTextField proxyUsername = ConfigTabHelper.addTextFieldRow(null, proxyConfigPanel, ConnectorProperties.PROXY_USERNAME_LABEL, ConnectorProperties.proxyUser, 10);
		proxyUsername.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.proxyUser = proxyUsername.getText();
			}
		});
		proxyUsername.setEditable(ConnectorProperties.proxyEnabled);
		
		final JFormattedTextField proxyPassword = ConfigTabHelper.addTextFieldRow(null, proxyConfigPanel, ConnectorProperties.PROXY_PASSWORD_LABEL, ConnectorProperties.proxyPassword, 10);
		proxyPassword.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.proxyPassword = proxyPassword.getText();
			}
		});
		proxyPassword.setEditable(ConnectorProperties.proxyEnabled);
		
		proxyEnabled.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				ConnectorProperties.proxyEnabled = proxyEnabled.isSelected();
				proxyHost.setEditable(proxyEnabled.isSelected());
				proxyPort.setEditable(proxyEnabled.isSelected());
				proxyUsername.setEditable(proxyEnabled.isSelected());
				proxyPassword.setEditable(proxyEnabled.isSelected());
			}
		});
		
        SpringUtilities.makeCompactGrid(proxyConfigPanel,
                6, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
        proxyConfigPanel.setOpaque(true);
		return proxyConfigPanel;
	}

	private JPanel buildGeneralConfigPanel() {
		JPanel generalConfigPanel = new JPanel(new SpringLayout());
		
		JLabel header = new JLabel("General configuration: ");
		header.setFont(header.getFont().deriveFont(Font.BOLD));
		generalConfigPanel.add(header);
		generalConfigPanel.add(new JLabel(""));
		
		final JFormattedTextField checkMessagesPeriod = ConfigTabHelper.addTextFieldRow(null, generalConfigPanel, 
				ConnectorProperties.GENERAL_CHECK_MESSAGES_PERIOD_LABEL, ConnectorProperties.checkMessagesPeriod, 40);
		checkMessagesPeriod.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.checkMessagesPeriod = checkMessagesPeriod.getText();
			}
		});
				
		
        SpringUtilities.makeCompactGrid(generalConfigPanel,
                2, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
        generalConfigPanel.setOpaque(true);
		return generalConfigPanel;
	}

	private JPanel buildGwConfigPanel() {
		JPanel gwConfigPanel = new JPanel(new SpringLayout());
		
		JLabel header = new JLabel("Gateway configuration: ");
		header.setFont(header.getFont().deriveFont(Font.BOLD));
		gwConfigPanel.add(header);
		gwConfigPanel.add(new JLabel(""));
		
		final JFormattedTextField gatewayEndpoint = ConfigTabHelper.addTextFieldRow(null, gwConfigPanel, ConnectorProperties.GATEWAY_ENDPOINT_LABEL, ConnectorProperties.gatewayEndpointValue, 40);
		gatewayEndpoint.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.gatewayEndpointValue = gatewayEndpoint.getText();
			}
		});
		
		final JFormattedTextField gatewayName = ConfigTabHelper.addTextFieldRow(null, gwConfigPanel, ConnectorProperties.GATEWAY_NAME_LABEL, ConnectorProperties.gatewayNameValue, 10);
		gatewayName.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.gatewayNameValue = gatewayName.getText();
			}
		});
		
		final JFormattedTextField gatewayRole = ConfigTabHelper.addTextFieldRow(null, gwConfigPanel, ConnectorProperties.GATEWAY_ROLE_LABEL, ConnectorProperties.gatewayRoleValue, 10);
		gatewayRole.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.gatewayRoleValue = gatewayRole.getText();
			}
		});
		
		
        SpringUtilities.makeCompactGrid(gwConfigPanel,
                4, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
        gwConfigPanel.setOpaque(true);
		return gwConfigPanel;
	}
}
