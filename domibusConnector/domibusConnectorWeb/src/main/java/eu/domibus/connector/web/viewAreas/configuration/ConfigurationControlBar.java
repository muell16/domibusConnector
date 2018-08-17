package eu.domibus.connector.web.viewAreas.configuration;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.UIScope;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@Component
@UIScope
public class ConfigurationControlBar extends HorizontalLayout {

	public ConfigurationControlBar() {
		Button reloadConfiguration = new Button(
				new Icon(VaadinIcon.REFRESH));
		reloadConfiguration.setText("Reload Configuration from properties");
		
		Button saveConfiguration = new Button(
				new Icon(VaadinIcon.EDIT));
		saveConfiguration.setText("Save Configuration to properties");
		
		this.add(reloadConfiguration);
		this.add(saveConfiguration);
		this.setWidth("100vw");
		this.setHeight("20px");
	}

	
}
