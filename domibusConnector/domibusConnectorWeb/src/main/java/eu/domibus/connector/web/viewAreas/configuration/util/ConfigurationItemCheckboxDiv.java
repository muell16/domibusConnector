package eu.domibus.connector.web.viewAreas.configuration.util;

import com.vaadin.flow.component.checkbox.Checkbox;

public class ConfigurationItemCheckboxDiv extends ConfigurationItemDiv {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Checkbox configurationItemCheckbox;
	
	public ConfigurationItemCheckboxDiv(Checkbox configurationItem, ConfigurationLabel labels) {
		super(configurationItem,labels);
		this.configurationItemCheckbox = configurationItem;
		this.configurationItemCheckbox.setLabel(labels.CONFIGURATION_ELEMENT_LABEL);
	}

	
	public Checkbox getConfigurationItemCheckbox() {
		return configurationItemCheckbox;
	}


	public void setConfigurationItemValue(Boolean value) {
		this.configurationItemCheckbox.setValue(value);
	}
}
