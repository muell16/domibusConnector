package eu.domibus.connector.web.viewAreas.configuration.util;

import java.util.HashMap;
import java.util.Properties;

import com.vaadin.flow.component.Component;


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
}
