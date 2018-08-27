package eu.domibus.connector.web.viewAreas.configuration.util;

import java.util.Collection;

import com.vaadin.flow.component.combobox.ComboBox;

public class ConfigurationItemComboBoxDiv extends ConfigurationItemDiv {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ConfigurationItemComboBoxDiv(ComboBox<String> comboBox, ConfigurationLabel labels, Collection<String> items, String initialValue) {
		super(comboBox, labels, initialValue);
		comboBox.setItems(items);
		comboBox.setWidth("600px");
		comboBox.setLabel(labels.CONFIGURATION_ELEMENT_LABEL);
		comboBox.setValue(initialValue);
		comboBox.addValueChangeListener(e -> {
			ConfigurationProperties.changeComponentValue(comboBox, e.getValue());
		});
	}

	
}
