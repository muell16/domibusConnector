package eu.domibus.connector.web.viewAreas.configuration.util;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;

import eu.domibus.connector.persistence.service.DomibusConnectorPropertiesPersistenceService;

@Component
public class ConfigurationUtil {

	@Autowired
	Environment env;
	
	@Autowired
	DomibusConnectorPropertiesPersistenceService propertiesPersistenceService;
	
	public String getPropertyValue(String propertyName) {
		return env.getProperty(propertyName);
	}
	
	public Div createConfigurationItemCheckboxDiv(ConfigurationLabel labels, Checkbox configCheckbox) {
		ConfigurationItemCheckboxDiv checkboxDiv = new ConfigurationItemCheckboxDiv(configCheckbox, labels, Boolean.valueOf(getPropertyValue(labels.PROPERTY_NAME_LABEL)));
		return checkboxDiv;
	}

	public Div createConfigurationItemComboBoxDiv(ConfigurationLabel labels, ComboBox<String> configComboBox, Collection<String> items) {
		ConfigurationItemComboBoxDiv comboBoxDiv = new ConfigurationItemComboBoxDiv(configComboBox, labels, items, getPropertyValue(labels.PROPERTY_NAME_LABEL));
		return comboBoxDiv;
	}
	
	public Div createConfigurationItemTextFieldDiv(ConfigurationLabel labels, TextField configTextField) {
		ConfigurationItemTextFieldDiv textFieldDiv = new ConfigurationItemTextFieldDiv(configTextField, labels, getPropertyValue(labels.PROPERTY_NAME_LABEL));
		return textFieldDiv;
	}
	
	public void reloadConfiguration() {
		for(String componentId: ConfigurationProperties.getConfigurationcomponents().keySet()) {
			com.vaadin.flow.component.Component c = ConfigurationProperties.getConfigurationcomponents().get(componentId);
			String contextValue = getPropertyValue(componentId);
			String componentValue = null;
			if(c instanceof ComboBox<?>) {
				componentValue = ((ComboBox<String>)c).getValue();
				if(componentValue!=null && contextValue!=null && !componentValue.equals(contextValue))
					((ComboBox<String>)c).setValue(contextValue);
			}else if(c instanceof Checkbox) {
				componentValue = ((Checkbox)c).getValue().toString().toLowerCase();
				if(componentValue!=null && contextValue!=null && !componentValue.equals(contextValue.toLowerCase()))
					((Checkbox)c).setValue(Boolean.valueOf(contextValue));
			}else if (c instanceof TextField) {
				componentValue = ((TextField)c).getValue();
				if(componentValue!=null && contextValue!=null && !componentValue.equals(contextValue))
					((TextField)c).setValue(contextValue);
			}
		}
		propertiesPersistenceService.resetProperties(ConfigurationProperties.getProperties());
		ConfigurationProperties.clearChanges();
	}
	
	public void resetConfiguration() {
		for(String componentId: ConfigurationProperties.getChangedcomponents().keySet()) {
			com.vaadin.flow.component.Component c = ConfigurationProperties.getChangedcomponents().get(componentId);
			Object initialValue = ConfigurationProperties.getInitialproperties().get(componentId);
			if(c instanceof ComboBox<?>) {
				((ComboBox<String>)c).setValue((String) initialValue);
			}else if(c instanceof Checkbox) {
				((Checkbox)c).setValue((Boolean) initialValue);
			}else if (c instanceof TextField) {
				((TextField)c).setValue(getPropertyValue((String) initialValue));
			}
		}
		ConfigurationProperties.clearChanges();
	}
	
	public void saveConfiguration() {
		propertiesPersistenceService.saveProperties(ConfigurationProperties.getProperties());
		for(String componentId: ConfigurationProperties.getChangedcomponents().keySet()) {
			com.vaadin.flow.component.Component c = ConfigurationProperties.getChangedcomponents().get(componentId);
			Object value = null;
			if(c instanceof ComboBox<?>) {
				value = ((ComboBox<String>)c).getValue();
			}else if(c instanceof Checkbox) {
				value = ((Checkbox)c).getValue();
			}else if (c instanceof TextField) {
				value = ((TextField)c).getValue();
			}
			if(value!=null)
				ConfigurationProperties.getInitialproperties().put(componentId, value);
		}
		ConfigurationProperties.clearChanges();
	}
	
}
