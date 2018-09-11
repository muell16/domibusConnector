package eu.domibus.connector.web.viewAreas.configuration.util;

import java.util.Collection;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;

import eu.domibus.connector.persistence.service.DomibusConnectorPropertiesPersistenceService;
import eu.domibus.connector.web.component.LumoCheckbox;
import eu.domibus.connector.web.enums.UserRole;

@Component
public class ConfigurationUtil {

	@Autowired
	Environment env;
	
	@Autowired
	DomibusConnectorPropertiesPersistenceService propertiesPersistenceService;
	
	@Autowired
	ConfigurationProperties configurationProperties;
	
	public String getPropertyValue(String propertyName) {
		return env.getProperty(propertyName);
	}
	
	public Div createConfigurationItemDiv(ConfigurationLabel labels, com.vaadin.flow.component.Component c, Object initialValue) {
		return new ConfigurationItemDiv(c, labels, initialValue, configurationProperties);
	}
	
	public Div createConfigurationItemCheckboxDivWithValue(ConfigurationLabel labels, LumoCheckbox configCheckbox, Boolean initialValue) {
		ConfigurationItemCheckboxDiv checkboxDiv = new ConfigurationItemCheckboxDiv(configCheckbox, labels, initialValue, configurationProperties);
		return checkboxDiv;
	}
	
	public Div createConfigurationItemCheckboxDiv(ConfigurationLabel labels, LumoCheckbox configCheckbox) {
		ConfigurationItemCheckboxDiv checkboxDiv = new ConfigurationItemCheckboxDiv(configCheckbox, labels, Boolean.valueOf(getPropertyValue(labels.PROPERTY_NAME_LABEL)), configurationProperties);
		return checkboxDiv;
	}

	public Div createConfigurationItemComboBoxDivEmpty(ConfigurationLabel labels, ComboBox<String> configComboBox, Collection<String> items) {
		ConfigurationItemComboBoxDiv comboBoxDiv = new ConfigurationItemComboBoxDiv(configComboBox, labels, items, "", configurationProperties);
		return comboBoxDiv;
	}
	
	public Div createConfigurationItemComboBoxDiv(ConfigurationLabel labels, ComboBox<String> configComboBox, Collection<String> items) {
		ConfigurationItemComboBoxDiv comboBoxDiv = new ConfigurationItemComboBoxDiv(configComboBox, labels, items, getPropertyValue(labels.PROPERTY_NAME_LABEL), configurationProperties);
		return comboBoxDiv;
	}
	
	
	public Div createConfigurationItemTextFieldDivEmpty(ConfigurationLabel labels, TextField configTextField) {
		ConfigurationItemTextFieldDiv textFieldDiv = new ConfigurationItemTextFieldDiv(configTextField, labels, "", configurationProperties);
		return textFieldDiv;
	}
	
	public Div createConfigurationItemTextFieldDiv(ConfigurationLabel labels, TextField configTextField) {
		ConfigurationItemTextFieldDiv textFieldDiv = new ConfigurationItemTextFieldDiv(configTextField, labels, getPropertyValue(labels.PROPERTY_NAME_LABEL), configurationProperties);
		return textFieldDiv;
	}
	
	public void reloadConfiguration() {
		for(String componentId: configurationProperties.getConfigurationcomponents().keySet()) {
			com.vaadin.flow.component.Component c = configurationProperties.getConfigurationcomponents().get(componentId);
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
		propertiesPersistenceService.resetProperties(configurationProperties.getProperties());
		configurationProperties.clearChanges();
	}
	
	public void resetConfiguration() {
		for(String componentId: configurationProperties.getChangedcomponents().keySet()) {
			com.vaadin.flow.component.Component c = configurationProperties.getChangedcomponents().get(componentId);
			Object initialValue = configurationProperties.getInitialproperties().get(componentId);
			if(c instanceof ComboBox<?>) {
				((ComboBox<String>)c).setValue((String) initialValue);
			}else if(c instanceof Checkbox) {
				((Checkbox)c).setValue((Boolean) initialValue);
			}else if (c instanceof TextField) {
				((TextField)c).setValue(getPropertyValue((String) initialValue));
			}
		}
		configurationProperties.clearChanges();
	}
	
	public void saveConfiguration() {
		propertiesPersistenceService.saveProperties(configurationProperties.getProperties());
		for(String componentId: configurationProperties.getChangedcomponents().keySet()) {
			com.vaadin.flow.component.Component c = configurationProperties.getChangedcomponents().get(componentId);
			Object value = null;
			if(c instanceof ComboBox<?>) {
				value = ((ComboBox<String>)c).getValue();
			}else if(c instanceof Checkbox) {
				value = ((Checkbox)c).getValue();
			}else if (c instanceof TextField) {
				value = ((TextField)c).getValue();
			}
			if(value!=null)
				configurationProperties.getInitialproperties().put(componentId, value);
		}
		configurationProperties.clearChanges();
	}

	public void updateOnRole(UserRole role) {
		configurationProperties.updateOnRole(role);
		
	}
	
	public void unregisterComponent(com.vaadin.flow.component.Component c) {
		configurationProperties.unregisterComponent(c);
	}
	
	public void updateConfigurationComponentsOnProperties(Properties properties) {
		configurationProperties.updateConfigurationComponentsOnProperties(properties);
	}
	
}
