package eu.domibus.connector.web.viewAreas.configuration.util;

import com.vaadin.flow.component.textfield.TextField;

public class ConfigurationItemTextFieldDiv extends ConfigurationItemDiv {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfigurationItemTextFieldDiv(TextField configurationItem, ConfigurationLabel labels, String initialValue) {
		super(configurationItem,labels, initialValue);
		configurationItem.setValue(initialValue);
		configurationItem.setLabel(labels.CONFIGURATION_ELEMENT_LABEL);
		configurationItem.addValueChangeListener(e -> {
			ConfigurationProperties.changeComponentValue(configurationItem, e.getValue());
		});
	}

}
