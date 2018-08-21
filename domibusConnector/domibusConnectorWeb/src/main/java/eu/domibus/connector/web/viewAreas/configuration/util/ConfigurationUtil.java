package eu.domibus.connector.web.viewAreas.configuration.util;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;

@Component
public class ConfigurationUtil {

	@Autowired
	Environment env;
	
	public String getPropertyValue(String propertyName) {
		return env.getProperty(propertyName);
	}
	
	public Div createChapterDiv(String label) {
		Div chapterDiv = new Div();
		
		Label chapterLabel = new Label(label);
		chapterLabel.getStyle().set("font-size", "20px");
		
		chapterLabel.getStyle().set("font-style", "italic");
		
		chapterDiv.add(chapterLabel);
		
		return chapterDiv;
	}
	
	public Div createConfigurationCheckboxWithLabels(ConfigurationLabel labels, Checkbox configCheckbox) {
		configCheckbox.setLabel(labels.CONFIGURATION_ELEMENT_LABEL);
		configCheckbox.setValue(Boolean.valueOf(env.getProperty(labels.PROPERTY_NAME_LABEL)));
		return createConfigurationElementDiv(labels, configCheckbox);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Div createConfigurationComboBoxWithItemsAndLabels(ConfigurationLabel labels, ComboBox configComboBox, Collection items, Object value) {
		configComboBox.setLabel(labels.CONFIGURATION_ELEMENT_LABEL);
		configComboBox.setItems(items);
		configComboBox.setValue(value);
		configComboBox.setWidth("600px");
		return createConfigurationElementDiv(labels, configComboBox);
	}
	
	public Div createConfigurationTextFieldWithLabels(ConfigurationLabel labels, TextField configTextField) {
		configTextField.setLabel(labels.CONFIGURATION_ELEMENT_LABEL);
		configTextField.setValue(env.getProperty(labels.PROPERTY_NAME_LABEL));
		return createConfigurationElementDiv(labels, configTextField);
	}
	
	private Div createConfigurationElementDiv(ConfigurationLabel labels, com.vaadin.flow.component.Component component) {
		Div configDiv = new Div();
		
		configDiv.add(component);
		Button infoButton = createInfoButton(labels);
		configDiv.add(infoButton);
		return configDiv;
	}

	private Button createInfoButton(ConfigurationLabel labels) {
		Button infoButton = new Button(new Icon(VaadinIcon.INFO_CIRCLE_O));
		Dialog dialog = new Dialog();
		
		Div headerContent = new Div();
		Label header = new Label(labels.CONFIGURATION_ELEMENT_LABEL);
		header.getStyle().set("font-weight", "bold");
		header.getStyle().set("font-style", "italic");
		headerContent.getStyle().set("text-align", "center");
		headerContent.getStyle().set("padding", "10px");
		headerContent.add(header);
		dialog.add(headerContent);
		
		Div infoContent = new Div();
		for(String info:labels.INFO_LABEL) {
			Div infoLine = new Div();
			infoLine.add(new Label(info));
			infoContent.add(infoLine);
		}
		infoContent.getStyle().set("padding", "10px");
		dialog.add(infoContent);
		
		Div propertyContent = new Div();
		Label correspondingProperty = new Label("\n Corresponding property: ");
		correspondingProperty.getStyle().set("font-weight", "bold");
		propertyContent.add(correspondingProperty);
		propertyContent.add(new Label(labels.PROPERTY_NAME_LABEL));
		propertyContent.getStyle().set("padding", "10px");
		dialog.add(propertyContent);
		
		Div closeButtonContent = new Div();
		closeButtonContent.getStyle().set("text-align", "center");
		Button closeButton = new Button("Close", event -> {
		    dialog.close();
		});
		closeButtonContent.add(closeButton);
		closeButtonContent.getStyle().set("padding", "10px");
		dialog.add(closeButtonContent);

		infoButton.addClickListener(event -> dialog.open());
		return infoButton;
	}
}
