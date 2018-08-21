package eu.domibus.connector.web.viewAreas.configuration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.viewAreas.configuration.environment.EnvironmentConfiguration;
import eu.domibus.connector.web.viewAreas.configuration.evidences.EvidenceBuilderConfiguration;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@UIScope
@org.springframework.stereotype.Component
public class Configuration extends VerticalLayout {
	
	protected final static Logger LOGGER = LoggerFactory.getLogger(Configuration.class);
	
	@Autowired
	Environment env;
	
	Div areaBackendConfig = null;
	Div areaEvidencesConfig = null;
	Div areaSecurityConfig = null;
	Div areaEnvironmentConfig = null;
	Div areaConfigControl = null;
	
	Tab backendConfigTab = new Tab("Backend Configuration");
	Tab evidencesConfigTab = new Tab("Evidence Builder Configuration");
	Tab securityConfigTab = new Tab("Security Toolkit Configuration");
	Tab environmentConfigTab = new Tab("Environment Configuration");
	
	Tabs configMenu = new Tabs();

	public Configuration(@Autowired SecurityConfiguration secConfig, @Autowired EnvironmentConfiguration envConfig, 
			@Autowired BackendConfiguration backendConfig, @Autowired EvidenceBuilderConfiguration evidencesConfig) {
		
		areaBackendConfig = new Div();
		areaBackendConfig.add(backendConfig);
		areaBackendConfig.setVisible(false);
		
		areaEvidencesConfig = new Div();
		areaEvidencesConfig.add(evidencesConfig);
		areaEvidencesConfig.setVisible(false);
		
		areaSecurityConfig = new Div();
		areaSecurityConfig.add(secConfig);
		areaSecurityConfig.setVisible(false);
		
		areaEnvironmentConfig = new Div();
		areaEnvironmentConfig.add(envConfig);
		areaEnvironmentConfig.setVisible(true);
		
		Map<Tab, Component> tabsToPages = new HashMap<>();
		tabsToPages.put(environmentConfigTab, areaEnvironmentConfig);
		tabsToPages.put(securityConfigTab, areaSecurityConfig);
		tabsToPages.put(backendConfigTab, areaBackendConfig);
		tabsToPages.put(evidencesConfigTab, areaEvidencesConfig);
		
		configMenu.add(environmentConfigTab, securityConfigTab, backendConfigTab, evidencesConfigTab);
		
		
		Div pages = new Div(areaEnvironmentConfig, areaSecurityConfig, areaBackendConfig, areaEvidencesConfig);
		
		Set<Component> pagesShown = Stream.of(areaEnvironmentConfig)
		        .collect(Collectors.toSet());
		
	
		configMenu.addSelectedChangeListener(event -> {
		    pagesShown.forEach(page -> page.setVisible(false));
		    pagesShown.clear();
		    Component selectedPage = tabsToPages.get(configMenu.getSelectedTab());
		    selectedPage.setVisible(true);
		    pagesShown.add(selectedPage);
		});

		add(createConfigurationButtonBar());
		add(configMenu,pages);
		
		
		this.expand(pages);
		this.setHeight("80vh");
	}
	
//	private void properties() {
//		for(Iterator it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext(); ) {
//            PropertySource propertySource = (PropertySource) it.next();
//            propertySource.
//            if (propertySource instanceof MapPropertySource) {
//                map.putAll(((MapPropertySource) propertySource).getSource());
//            }
//        }
//	}

	private HorizontalLayout createConfigurationButtonBar() {
		HorizontalLayout configurationButtonBar = new HorizontalLayout();
		Button loadConfiguration = new Button(
				new Icon(VaadinIcon.REFRESH));
		loadConfiguration.setText("Load Configuration");
		
		Button saveConfiguration = new Button(
				new Icon(VaadinIcon.EDIT));
		saveConfiguration.setText("Save Configuration");
		
		configurationButtonBar.add(loadConfiguration);
		configurationButtonBar.add(saveConfiguration);
		configurationButtonBar.setWidth("100vw");
		configurationButtonBar.setHeight("20px");
		configurationButtonBar.setPadding(true);
		configurationButtonBar.getStyle().set("padding-bottom", "30px");
		
		return configurationButtonBar;
	}

}
