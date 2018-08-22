package eu.domibus.connector.web.viewAreas.configuration.util;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;

@Component
public class ConfigurationUtil {

	@Autowired
	Environment env;
	
	public String getPropertyValue(String propertyName) {
		return env.getProperty(propertyName);
	}
	
	
	public Div createConfigurationItemCheckboxDiv(ConfigurationLabel labels, Checkbox configCheckbox) {
		ConfigurationItemCheckboxDiv checkboxDiv = new ConfigurationItemCheckboxDiv(configCheckbox, labels);
		checkboxDiv.setConfigurationItemValue(Boolean.valueOf(getPropertyValue(labels.PROPERTY_NAME_LABEL)));
		return checkboxDiv;
	}

	public <T> Div createConfigurationItemComboBoxDiv(ConfigurationLabel labels, ComboBox<T> configComboBox, T value, Collection<T> items) {
		ConfigurationItemComboBoxDiv<T> comboBoxDiv = new ConfigurationItemComboBoxDiv<T>(configComboBox, labels, items);
		comboBoxDiv.setConfigurationItemValue(value);
		return comboBoxDiv;
	}
	
	public Div createConfigurationItemTextFieldDiv(ConfigurationLabel labels, TextField configTextField) {
		ConfigurationItemTextFieldDiv textFieldDiv = new ConfigurationItemTextFieldDiv(configTextField, labels);
		textFieldDiv.setConfigurationItemValue(getPropertyValue(labels.PROPERTY_NAME_LABEL));
		return textFieldDiv;
	}
	
	
}
