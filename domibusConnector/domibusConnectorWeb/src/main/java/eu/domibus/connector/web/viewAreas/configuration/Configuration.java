package eu.domibus.connector.web.viewAreas.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.persistence.service.DomibusConnectorPropertiesPersistenceService;
import eu.domibus.connector.web.viewAreas.configuration.backend.BackendConfiguration;
import eu.domibus.connector.web.viewAreas.configuration.environment.EnvironmentConfiguration;
import eu.domibus.connector.web.viewAreas.configuration.evidences.EvidenceBuilderConfiguration;
import eu.domibus.connector.web.viewAreas.configuration.security.SecurityConfiguration;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationProperties;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationUtil;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@UIScope
@org.springframework.stereotype.Component
public class Configuration extends VerticalLayout {
	
	protected final static Logger LOGGER = LoggerFactory.getLogger(Configuration.class);
	
	DomibusConnectorPropertiesPersistenceService propertiesPersistenceService;
	ConfigurationUtil util;
	
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
			@Autowired BackendConfiguration backendConfig, @Autowired EvidenceBuilderConfiguration evidencesConfig,
			@Autowired DomibusConnectorPropertiesPersistenceService propertiesPersistenceService, @Autowired ConfigurationUtil util) {
		this.propertiesPersistenceService = propertiesPersistenceService;
		this.util = util;
		
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
	
	private HorizontalLayout createConfigurationButtonBar() {
		HorizontalLayout configurationButtonBar = new HorizontalLayout();
		
		String reloadActionText = "Reload Configuration from context";
		Button reloadConfiguration = new Button(
				new Icon(VaadinIcon.FILE_REFRESH));
		reloadConfiguration.setText(reloadActionText);
		reloadConfiguration.addClickListener(e -> {
			Button confirmButton = new Button("Confirm");
			Dialog confirmDialog = createConfigurationConfirmDialog(
					reloadActionText, 
					confirmButton, 
					"All configuration properties will be reloaded from the application context.",
					"Be aware that this also effects configuration properties that have already been changed and saved since the last start of the domibusConnector!",
					"If there are changed properties that are already saved, those changes will be reset in the database to the status of the last startup as well."
					);
			confirmButton.addClickListener(e2 -> {
				util.reloadConfiguration();
				confirmDialog.close();
			});
			
			confirmDialog.open();
		});
		configurationButtonBar.add(reloadConfiguration);
		
		String resetActionText = "Discard Changes";
		Button resetConfiguration = new Button(
				new Icon(VaadinIcon.REFRESH));
		resetConfiguration.setText(resetActionText);
		resetConfiguration.addClickListener(e -> {
			Button confirmButton = new Button("Confirm");
			Dialog confirmDialog = createConfigurationConfirmDialog(
					reloadActionText, 
					confirmButton, 
					"All changes since the last time of saving will be discarded."
					);
			confirmButton.addClickListener(e2 -> {
				util.resetConfiguration();
				confirmDialog.close();
			});
			
			confirmDialog.open();
		});
		configurationButtonBar.add(resetConfiguration);
		
		
		String saveActionText = "Save Configuration";
		Button saveConfiguration = new Button(
				new Icon(VaadinIcon.EDIT));
		saveConfiguration.setText(saveActionText);
		saveConfiguration.addClickListener(e -> {
			Button confirmButton = new Button("Confirm");
			Dialog confirmDialog = createConfigurationConfirmDialog(
					saveActionText, 
					confirmButton, 
					"All changed configuration properties will be saved into the database table DOMIBUS_CONNECTOR_PROPERTIES.",
					"Be aware that changes to the configuration except backend client configuration will only take effect after restart of the domibusConnector.",
					"Also take note that the configured properties in the property files will NOT be changed!"
					);
			confirmButton.addClickListener(e2 -> {
				util.saveConfiguration();
				confirmDialog.close();
			});
			
			confirmDialog.open();
		});
		configurationButtonBar.add(saveConfiguration);
		
		configurationButtonBar.setWidth("100vw");
		configurationButtonBar.setHeight("20px");
		configurationButtonBar.setPadding(true);
		configurationButtonBar.getStyle().set("padding-bottom", "30px");
		
		return configurationButtonBar;
	}
	
	private Dialog createConfigurationConfirmDialog(String headerString, Button confirmButton, String...infoStrings) {
		Dialog confirmDialog = new Dialog();
		
		Div headerContent = new Div();
		Label header = new Label(headerString);
		header.getStyle().set("font-weight", "bold");
		header.getStyle().set("font-style", "italic");
		header.getStyle().set("margin-bottom", "10px");
		headerContent.getStyle().set("text-align", "center");
		headerContent.getStyle().set("padding", "10px");
		headerContent.getStyle().set("margin-bottom", "10px");
		headerContent.add(header);
		confirmDialog.add(headerContent);
		
		Div content = new Div();
		VerticalLayout contentLayout = new VerticalLayout();
		for(String infoString:infoStrings) {
			Label infoLabel = new Label(infoString);
			infoLabel.getStyle().set("margin-top", "0px");
			contentLayout.add(infoLabel);
		}
		contentLayout.getStyle().set("text-align", "center");
		contentLayout.getStyle().set("padding", "0px");
		contentLayout.setAlignItems(Alignment.CENTER);
		content.add(contentLayout);
		content.getStyle().set("text-align", "center");
		confirmDialog.add(content);
		
		Div confirmCancelButtonContent = new Div();
		confirmCancelButtonContent.getStyle().set("text-align", "center");
		confirmCancelButtonContent.getStyle().set("padding", "10px");
		confirmCancelButtonContent.add(confirmButton);
		
		Button cancelButton = new Button("Cancel");
		cancelButton.addClickListener(e3 -> {
			confirmDialog.close();
		});
		confirmCancelButtonContent.add(cancelButton);
		confirmDialog.add(confirmCancelButtonContent);
		
		return confirmDialog;
	}

}
