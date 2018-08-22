package eu.domibus.connector.web.viewAreas.configuration.util;

import com.vaadin.flow.component.textfield.TextField;

public class ConfigurationItemTextFieldDiv extends ConfigurationItemDiv {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final TextField configurationItemTextField;

	public ConfigurationItemTextFieldDiv(TextField configurationItem, ConfigurationLabel labels) {
		super(configurationItem,labels);
		this.configurationItemTextField = configurationItem;
		this.configurationItemTextField.setLabel(labels.CONFIGURATION_ELEMENT_LABEL);
	}

	public TextField getConfigurationItem() {
		return configurationItemTextField;
	}
	
	public void setConfigurationItemValue(String value) {
		this.configurationItemTextField.setValue(value);
	}

}
