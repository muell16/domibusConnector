package eu.domibus.connector.gui.config;

import javax.swing.JTabbedPane;

import eu.domibus.connector.gui.config.tabs.ConfigDatabaseTab;
import eu.domibus.connector.gui.config.tabs.ConfigEnvironmentTab;
import eu.domibus.connector.gui.config.tabs.ConfigEvidencesTab;
import eu.domibus.connector.gui.config.tabs.ConfigOtherTab;
import eu.domibus.connector.gui.config.tabs.ConfigSecurityTab;
import eu.domibus.connector.gui.config.tabs.ConfigStoresTab;

public class ConfigTab extends JTabbedPane {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2075857119811383553L;

	public ConfigTab(){
		
		this.addTab("Database", new ConfigDatabaseTab());
		this.addTab("Environment", new ConfigEnvironmentTab());
		this.addTab("Stores", new ConfigStoresTab());
		this.addTab("Evidences", new ConfigEvidencesTab());
		this.addTab("Security", new ConfigSecurityTab());
		this.addTab("Other", new ConfigOtherTab());
	}
}
