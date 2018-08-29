package eu.domibus.connector.web.viewAreas.configuration.util;

import java.util.HashMap;
import java.util.Properties;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.enums.UserRole;

@org.springframework.stereotype.Component
@UIScope
public class ConfigurationProperties {
	
	private static final Properties properties = new Properties();
	
	private static final HashMap<String, Object> initialProperties = new HashMap<String, Object>();
	
	private static final HashMap<String, Component> configurationComponents = new HashMap<>();
	
	private static final HashMap<String, Component> changedComponents = new HashMap<>();
	
	public static void registerComponent(Component component, Object initialValue) {
		configurationComponents.put(component.getId().get(),component);
		initialProperties.put(component.getId().get(), initialValue);
	}
	
	public static void unregisterComponent(Component component) {
		configurationComponents.remove(component.getId().get());
		initialProperties.remove(component.getId().get());
	}
	
	public static void changeComponentValue(Component component, String newStringValue) {
		changedComponents.put(component.getId().get(),component);
		properties.setProperty(component.getId().get(), newStringValue);
	}
	
	public static void clearChanges() {
		changedComponents.clear();
		properties.clear();
	}

	public static Properties getProperties() {
		return properties;
	}
	
	public static void clearProperties() {
		properties.clear();
		
	}

	public static HashMap<String, Object> getInitialproperties() {
		return initialProperties;
	}

	public static HashMap<String, Component> getConfigurationcomponents() {
		return configurationComponents;
	}

	public static HashMap<String, Component> getChangedcomponents() {
		return changedComponents;
	}
	
	public static void updateOnRole(UserRole userRole) {
		for(String componentId: ConfigurationProperties.getConfigurationcomponents().keySet()) {
			com.vaadin.flow.component.Component c = ConfigurationProperties.getConfigurationcomponents().get(componentId);
			boolean readonly = !userRole.equals(UserRole.ADMIN);
			if(c instanceof ComboBox<?>) {
				((ComboBox<String>)c).setReadOnly(readonly);
			}else if(c instanceof Checkbox) {
					((Checkbox)c).setReadOnly(readonly);
			}else if (c instanceof TextField) {
					((TextField)c).setReadOnly(readonly);
			}
		}
	}
	
	public static void updateConfigurationComponentsOnProperties(Properties properties) {
		for(String componentId: ConfigurationProperties.getConfigurationcomponents().keySet()) {
			if(properties.containsKey(componentId)) {
			com.vaadin.flow.component.Component c = ConfigurationProperties.getConfigurationcomponents().get(componentId);
			if(c instanceof ComboBox<?>) {
				((ComboBox<String>)c).setValue(properties.getProperty(componentId));
			}else if(c instanceof Checkbox) {
					((Checkbox)c).setValue(Boolean.valueOf(properties.getProperty(componentId)));
			}else if (c instanceof TextField) {
					((TextField)c).setValue(properties.getProperty(componentId));
			}
			}
		}
	}
}
