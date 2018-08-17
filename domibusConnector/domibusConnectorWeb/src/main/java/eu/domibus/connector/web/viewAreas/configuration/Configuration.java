package eu.domibus.connector.web.viewAreas.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.spring.annotation.UIScope;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@UIScope
@org.springframework.stereotype.Component
public class Configuration extends VerticalLayout {
	
	Div areaSecurityConfig = null;
	Div areaEnvironmentConfig = null;
	Div areaConfigControl = null;
	
	Tab securityConfigTab = new Tab("Security Configuration");
	Tab environmentConfigTab = new Tab("Environment Configuration");
	
	Tabs configMenu = new Tabs();

	public Configuration(@Autowired ConfigurationControlBar configCtrlBar, @Autowired SecurityConfiguration secConfig, @Autowired EnvironmentConfiguration envConfig) {
		
		
		areaSecurityConfig = new Div();
		areaSecurityConfig.add(secConfig);
		areaSecurityConfig.setVisible(false);
		
		areaEnvironmentConfig = new Div();
		areaEnvironmentConfig.add(envConfig);
		areaEnvironmentConfig.setVisible(true);
		
		Map<Tab, Component> tabsToPages = new HashMap<>();
		tabsToPages.put(securityConfigTab, areaSecurityConfig);
		tabsToPages.put(environmentConfigTab, areaEnvironmentConfig);
		
		configMenu.add(securityConfigTab, environmentConfigTab);
		
		
		Div pages = new Div(areaSecurityConfig, areaEnvironmentConfig);
		
		Set<Component> pagesShown = Stream.of(areaSecurityConfig)
		        .collect(Collectors.toSet());
		
	
		configMenu.addSelectedChangeListener(event -> {
		    pagesShown.forEach(page -> page.setVisible(false));
		    pagesShown.clear();
		    Component selectedPage = tabsToPages.get(configMenu.getSelectedTab());
		    selectedPage.setVisible(true);
		    pagesShown.add(selectedPage);
		});

		add(configMenu,pages);
		
		add(configCtrlBar);
		this.expand(pages);
		this.setHeight("80vh");
	}

}
