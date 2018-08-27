package eu.domibus.connector.web.viewAreas.configuration.util;

import com.vaadin.flow.component.checkbox.Checkbox;

public class ConfigurationItemCheckboxDiv extends ConfigurationItemDiv {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ConfigurationItemCheckboxDiv(Checkbox configurationItem, ConfigurationLabel labels, Boolean initialValue) {
		super(configurationItem,labels, initialValue);
		configurationItem.setValue(initialValue);
		configurationItem.setLabel(labels.CONFIGURATION_ELEMENT_LABEL);
		configurationItem.addValueChangeListener(e -> {
			ConfigurationProperties.changeComponentValue(configurationItem, e.getValue().toString().toLowerCase());
		});
	}
	
}
