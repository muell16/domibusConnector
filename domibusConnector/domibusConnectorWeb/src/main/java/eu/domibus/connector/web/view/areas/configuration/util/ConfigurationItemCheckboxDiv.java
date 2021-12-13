package eu.domibus.connector.web.view.areas.configuration.util;

import com.vaadin.flow.component.checkbox.Checkbox;

import eu.domibus.connector.web.component.LumoCheckbox;

public class ConfigurationItemCheckboxDiv extends ConfigurationItemDiv {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ConfigurationItemCheckboxDiv(LumoCheckbox configurationItem, ConfigurationLabel labels, Boolean initialValue, ConfigurationProperties configurationProperties) {
		super(configurationItem,labels, initialValue, configurationProperties);
		configurationItem.setValue(initialValue);
		configurationItem.setLabel(labels.CONFIGURATION_ELEMENT_LABEL);
		configurationItem.addValueChangeListener(e -> {
			configurationProperties.changeComponentValue(configurationItem, e.getValue().toString().toLowerCase());
		});
	}
	
}