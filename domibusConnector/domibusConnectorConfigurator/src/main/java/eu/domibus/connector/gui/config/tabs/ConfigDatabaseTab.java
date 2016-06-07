package eu.domibus.connector.gui.config.tabs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import eu.domibus.connector.gui.config.properties.ConnectorProperties;
import eu.domibus.connector.gui.layout.SpringUtilities;

public class ConfigDatabaseTab extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7142865084301801902L;
	
	
	public ConfigDatabaseTab() {
		JPanel helpPanel = ConfigTabHelper.buildHelpPanel("Database Configuration Help", "DatabaseConfigurationHelp.htm");
		BorderLayout mgr = new BorderLayout();
		setLayout(mgr);
		add(helpPanel, BorderLayout.EAST);

		JPanel dbConnectionPanel = buildDBConnectionPanel();
		JPanel connectionPoolPanel = buildConnectionPoolPanel();
		
		JPanel disp = new JPanel();
		disp.setPreferredSize(new Dimension(975, 450));
		
		disp.add(dbConnectionPanel);
		disp.add(connectionPoolPanel);
		
		dbConnectionPanel.setVisible(true);
		
		
		add(disp);
		
	}
	
	private JPanel buildConnectionPoolPanel() {
		JPanel connectionPoolPanel = new JPanel(new SpringLayout());
		
		final JFormattedTextField c3p0acquireIncrement = ConfigTabHelper.addTextFieldRow(ConfigTabHelper.numberFormat, connectionPoolPanel, ConnectorProperties.C3P0_ACQUIRE_INCREMENT_LABEL, ConnectorProperties.c3p0acquireIncrementValue, 5);
		c3p0acquireIncrement.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.c3p0acquireIncrementValue = c3p0acquireIncrement.getText();
			}
		});
		
		final JFormattedTextField c3p0minPoolSize = ConfigTabHelper.addTextFieldRow(ConfigTabHelper.numberFormat, connectionPoolPanel, ConnectorProperties.C3P0_MIN_POOL_SIZE_LABEL, ConnectorProperties.c3p0minPoolSizeValue, 5);
		c3p0minPoolSize.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.c3p0minPoolSizeValue = c3p0minPoolSize.getText();
			}
		});
		
		final JFormattedTextField c3p0maxPoolSize = ConfigTabHelper.addTextFieldRow(ConfigTabHelper.numberFormat, connectionPoolPanel, ConnectorProperties.C3P0_MAX_POOL_SIZE_LABEL, ConnectorProperties.c3p0maxPoolSizeValue, 5);
		c3p0maxPoolSize.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.c3p0maxPoolSizeValue = c3p0maxPoolSize.getText();
			}
		});
		
		final JFormattedTextField c3p0maxIdleTime = ConfigTabHelper.addTextFieldRow(ConfigTabHelper.numberFormat, connectionPoolPanel, ConnectorProperties.C3P0_MAX_IDLE_TIME_LABEL, ConnectorProperties.c3p0maxIdleTimeValue, 10);
		c3p0maxIdleTime.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.c3p0maxIdleTimeValue = c3p0maxIdleTime.getText();
			}
		});
		
		
        SpringUtilities.makeCompactGrid(connectionPoolPanel,
                4, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
        connectionPoolPanel.setOpaque(true);
		return connectionPoolPanel;
	}


	private JPanel buildDBConnectionPanel() {
		JPanel springPanel = new JPanel(new SpringLayout());
		
		final JFormattedTextField dbDialectValue = ConfigTabHelper.addTextFieldRow(null, springPanel, ConnectorProperties.DATABASE_DIALECT_LABEL, ConnectorProperties.databaseDialectValue, 40);
		dbDialectValue.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.databaseDialectValue = dbDialectValue.getText();
			}
		});
		
		final JFormattedTextField dbDriverValue = ConfigTabHelper.addTextFieldRow(null, springPanel, ConnectorProperties.DATABASE_DRIVERCLASSNAME_LABEL, ConnectorProperties.databaseDriverClassNameValue, 40);
		dbDriverValue.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.databaseDriverClassNameValue = dbDriverValue.getText();
			}
		});
		
		final JFormattedTextField dbUrlValue = ConfigTabHelper.addTextFieldRow(null, springPanel, ConnectorProperties.DATABASE_URL_LABEL, ConnectorProperties.databaseUrlValue, 40);
		dbUrlValue.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.databaseUrlValue = dbUrlValue.getText();
			}
		});
		
		final JFormattedTextField dbUsernameValue = ConfigTabHelper.addTextFieldRow(null, springPanel, ConnectorProperties.DATABASE_USERNAME_LABEL, ConnectorProperties.databaseUsernameValue, 40);
		dbUsernameValue.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.databaseUsernameValue = dbUsernameValue.getText();
			}
		});
		
		final JFormattedTextField dbPasswordValue = ConfigTabHelper.addTextFieldRow(null, springPanel, ConnectorProperties.DATABASE_PASSWORD_LABEL, ConnectorProperties.databasePasswordValue, 40);
		dbPasswordValue.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ConnectorProperties.databasePasswordValue = dbPasswordValue.getText();
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
