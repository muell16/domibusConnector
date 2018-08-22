package eu.domibus.connector.web.viewAreas.configuration.util;

import java.util.Collection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;

public class ConfigurationItemComboBoxDiv<T> extends ConfigurationItemDiv {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ComboBox<T> configurationItem;

	@SuppressWarnings("unchecked")
	public ConfigurationItemComboBoxDiv(Component component, ConfigurationLabel labels, Collection<T> items) {
		super(component, labels);
		this.configurationItem = (ComboBox<T>) component;
		this.configurationItem.setItems(items);
		this.configurationItem.setWidth("600px");
		this.configurationItem.setLabel(labels.CONFIGURATION_ELEMENT_LABEL);
	}

	public ComboBox<T> getConfigurationItem() {
		return configurationItem;
	}
	
	public void setConfigurationItemValue(T value) {
		this.configurationItem.setValue(value);
	}

}
